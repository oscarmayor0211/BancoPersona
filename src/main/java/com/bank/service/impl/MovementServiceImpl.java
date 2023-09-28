package com.bank.service.impl;

import com.bank.dto.MovementDTO;
import com.bank.entity.Account;
import com.bank.entity.Movement;
import com.bank.repository.CustomerRepository;
import com.bank.repository.AccountRepository;
import com.bank.repository.MovementRepository;
import com.bank.service.MovementService;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.exceptions.PeticionErronea;
import com.bank.utility.mapper.MovimientoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

  private static final Logger logger = LoggerFactory.getLogger(MovementServiceImpl.class);
  private final String SND = "Saldo no disponible";
  private final String CDE = "Cupo diario excedido";
  private final MovementRepository movimientoRepository;
  private final MovimientoMapper movimientoMapper;
  private final AccountRepository cuentaRepository;
  private final CustomerRepository clienteRepository;


  @Autowired
  public MovementServiceImpl(MovementRepository movimientoRepository,
                             MovimientoMapper movimientoMapper, AccountRepository cuentaRepository,
                             CustomerRepository clienteRepository) {
    this.movimientoRepository = movimientoRepository;
    this.movimientoMapper = movimientoMapper;
    this.cuentaRepository = cuentaRepository;
    this.clienteRepository = clienteRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MovementDTO> getMovimientos() {
    List<Movement> movimientos = movimientoRepository.findAll();
    return movimientos.stream().map(movimientoMapper::movimientoToMovimientoDTO).collect(
        Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public MovementDTO getMovimiento(Long id) {
    return movimientoRepository.findById(id).map(movimientoMapper::movimientoToMovimientoDTO)
        .orElse(null);
  }

  @Override
  @Transactional
  public MovementDTO createMovimiento(MovementDTO movimientoDTO) {
    Optional<Account> cuentaOpt = cuentaRepository.findByNumeroCuenta(
        movimientoDTO.getCuenta().getNumeroCuenta());
    double saldoTotal = 0;
    if (cuentaOpt.isPresent()) {
      if (cuentaOpt.get().getMovimientos().size() == 0) {
        saldoTotal = hacerMovimiento(movimientoDTO.getTipoMovimiento().getValue(),
            cuentaOpt.get().getSaldoInicial(), movimientoDTO.getValor());
      } else {
        double saldoUltimoMovimiento = getUltimoMovimiento(cuentaOpt.get().getMovimientos());
        saldoTotal = hacerMovimiento(movimientoDTO.getTipoMovimiento().toString(),
            saldoUltimoMovimiento, movimientoDTO.getValor());
      }
      if (saldoTotal < 0) {
        logger.warn(SND);
        throw new PeticionErronea(SND);
      }

      Movement movimiento = movimientoMapper.movimientoDTOToMovimiento(movimientoDTO);
      movimiento.setFecha(LocalDate.now());
      movimiento.setCuenta(cuentaOpt.get());
      movimiento.setSaldo(saldoTotal);

      if (validarSaldoExcedido(cuentaOpt.get().getMovimientos(), movimiento)) {
        logger.warn(CDE);
        throw new PeticionErronea(CDE);
      }

      final Movement nuevoMovimiento = movimientoRepository.save(movimiento);
      logger.info("Movimiento realizado!");
      return movimientoMapper.movimientoToMovimientoDTO(nuevoMovimiento);
    }
    logger.warn("Cuenta no encontrada");
    throw new NoEncontrado("Cuenta no encontrada");
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Optional<Movement> movimientoOpt = movimientoRepository.findById(id);
    if (movimientoOpt.isPresent()) {
      movimientoRepository.deleteById(id);
      logger.info("Movimiento eliminado!");
    } else {
      logger.warn("Movimiento no encontrado");
      throw new NoEncontrado("Movimiento no encontrado");
    }
  }

  @Override
  @Transactional
  public MovementDTO updateMovimiento(Long id, MovementDTO movimientoDTO) {
    Optional<Movement> movimientoOpt = movimientoRepository.findById(id);
    if (movimientoOpt.isPresent()) {
      Movement movimientoActual = movimientoOpt.get();
      double saldoTotal = 0;
      double saldo = getSaldo(movimientoDTO.getCuenta().getNumeroCuenta(), movimientoActual);

      saldoTotal = hacerMovimiento(movimientoDTO.getTipoMovimiento().getValue(), saldo,
          movimientoDTO.getValor());

      if (saldoTotal < 0) {
        throw new PeticionErronea("Fondos insuficientes");
      }

      movimientoActual.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
      movimientoActual.setValor(movimientoDTO.getValor());
      movimientoActual.setSaldo(saldoTotal);

      movimientoRepository.save(movimientoActual);
      logger.info("Movimiento actualizado");
      return movimientoMapper.movimientoToMovimientoDTO(movimientoActual);
    }
    logger.warn("Movimiento no encontrado");
    throw new NoEncontrado("Movimiento no encontrado");
  }

  @Override
  @Transactional
  public MovementDTO actualizacionParcialByFields(Long id, Map<String, Object> fields) {
    Optional<Movement> movimientoOpt = movimientoRepository.findById(id);
    if (movimientoOpt.isPresent()) {
      Movement movimientoActual = movimientoOpt.get();
      double saldoTotal = 0;
      double saldo = getSaldo(movimientoActual.getCuenta().getNumeroCuenta(), movimientoActual);
      populateMapToMovimiento(movimientoActual, fields);
      saldoTotal = hacerMovimiento(movimientoActual.getTipoMovimiento().getValue(), saldo,
          movimientoActual.getValor());
      if (saldoTotal < 0) {
        throw new PeticionErronea("Fondos insuficientes");
      }

      movimientoActual.setSaldo(saldoTotal);
      movimientoRepository.save(movimientoActual);
      logger.info("Movimiento actualizado");
      return movimientoMapper.movimientoToMovimientoDTO(movimientoActual);
    } else {
      logger.warn("Movimiento no encontrado");
      throw new NoEncontrado("Movimiento no encontrado");
    }
  }

  private double getSaldo(String numeroCuenta, Movement movimientoActual1) {
    Account cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta).get();
    double saldo = getUltimoMovimiento(cuenta.getMovimientos());
    if (movimientoActual1.getTipoMovimiento().getValue().equals("Retiro")) {
      saldo += movimientoActual1.getValor();
    } else if (movimientoActual1.getTipoMovimiento().getValue().equals("Deposito")) {
      saldo -= movimientoActual1.getValor();
    }
    return saldo;
  }

  private void populateMapToMovimiento(Movement movimientoActual, Map<String, Object> fields) {
    fields.forEach((k, v) -> {
      switch (k) {
        case "tipoMovimiento":
          movimientoActual.setTipoMovimiento(movimientoMapper.stringToTipoMovimiento(v.toString()));
          break;
        case "valor":
          movimientoActual.setValor(Double.parseDouble(v + ""));
          break;
      }
    });
  }

  private double hacerMovimiento(String tipoMovimiento, double saldo, double valor) {
    double saldoTotal = 0;
    if (tipoMovimiento.equalsIgnoreCase("Retiro")) {
      saldoTotal = saldo - valor;
    } else if (tipoMovimiento.equalsIgnoreCase("Deposito")) {
      saldoTotal = saldo + valor;
    }
    return saldoTotal;
  }

  public double getUltimoMovimiento(List<Movement> movimientos) {
    Movement movimiento = movimientos.stream().reduce((first, second) -> second).orElse(null);
    return Objects.nonNull(movimiento) ? movimiento.getSaldo() : 0;
  }

  private boolean validarSaldoExcedido(List<Movement> movimientoList, Movement movimiento) {
    AtomicReference<Double> saldoMaxDia = new AtomicReference<>((double) 0);
    List<Movement> movimientoLista = movimientoList.stream()
        .filter(m -> m.getFecha().isEqual(movimiento.getFecha())
            && m.getTipoMovimiento().getValue().equalsIgnoreCase("Retiro")).toList();

    movimientoLista.forEach(mov -> {
      saldoMaxDia.set(saldoMaxDia.get() + mov.getValor());
    });

    if (movimiento.getTipoMovimiento().getValue().equalsIgnoreCase("Retiro")) {
      saldoMaxDia.set(saldoMaxDia.get() + movimiento.getValor());
    }
    return saldoMaxDia.get() >= 1000 ? Boolean.TRUE : Boolean.FALSE;
  }
}