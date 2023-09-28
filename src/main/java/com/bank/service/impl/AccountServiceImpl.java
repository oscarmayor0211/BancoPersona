package com.bank.service.impl;

import com.bank.dto.AccountDTO;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.repository.CustomerRepository;
import com.bank.repository.AccountRepository;
import com.bank.repository.MovementRepository;
import com.bank.service.AccountService;
import com.bank.utility.exceptions.ErrorInterno;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.exceptions.PeticionErronea;

import com.bank.utility.mapper.CuentaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
  private final AccountRepository cuentaRepository;
  private final CuentaMapper cuentaMapper;
  private final CustomerRepository clienteRepository;
  private final MovementRepository movimientoRepository;


  @Autowired
  public AccountServiceImpl(AccountRepository cuentaRepository,
                            CuentaMapper cuentaMapper,
                            CustomerRepository clienteRepository, MovementRepository movimientoRepository) {
    this.cuentaRepository = cuentaRepository;
    this.cuentaMapper = cuentaMapper;
    this.clienteRepository = clienteRepository;
    this.movimientoRepository = movimientoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountDTO> getCuentas() {
    List<Account> cuentas = cuentaRepository.findAll();
    return cuentas.stream().map(cuentaMapper::cuentaToCuentaDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getCuenta(Long id) {
    return cuentaRepository.findById(id).map(cuentaMapper::cuentaToCuentaDTO).orElse(null);
  }

  @Override
  @Transactional
  public AccountDTO createCuenta(AccountDTO cuentaDTO) {
    Optional<Customer> clienteOpt = clienteRepository.findByIdentificacion(
        cuentaDTO.getCliente().getIdentificacion());
    if (clienteOpt.isEmpty()) {
      logger.warn("Verificar la identificación del cliente");
      throw new PeticionErronea("Verificar la identificación del cliente");
    }
    try {
      Account cuenta = cuentaMapper.cuentaDTOToCuenta(cuentaDTO);
      cuenta.setCliente(clienteOpt.get());
      final Account nuevaCuenta = cuentaRepository.save(cuenta);
      logger.info("Cuenta creada!");
      return cuentaMapper.cuentaToCuentaDTO(nuevaCuenta);
    } catch (Exception e) {
      logger.warn("Verificar los datos de la cuenta");
      throw new ErrorInterno("Verificar los datos de la cuenta");
    }
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    //verificar saldo antes de eliminar
    try {
      cuentaRepository.deleteById(id);
      logger.info("Cuenta eliminada!");
    } catch (Exception e) {
      logger.warn("Cuenta no encontrada");
      throw new NoEncontrado("Cuenta no encontrada");
    }
  }

  @Override
  @Transactional
  public AccountDTO updateCuenta(Long id, AccountDTO cuentaDTO) {
    Optional<Account> cuentaOpt = cuentaRepository.findById(id);
    if (cuentaOpt.isPresent()) {
      Account cuenta = cuentaMapper.cuentaDTOToCuenta(cuentaDTO);
      Account cuentaActualizada = cuentaOpt.get();
      cuentaActualizada.setNumeroCuenta(cuenta.getNumeroCuenta());
      cuentaActualizada.setTipoCuenta(cuenta.getTipoCuenta());
      cuentaActualizada.setSaldoInicial(cuenta.getSaldoInicial());
      cuentaActualizada.setEstado(cuenta.getEstado());
      cuentaRepository.save(cuentaActualizada);
      logger.info("Cuenta actualizada!");
      return cuentaMapper.cuentaToCuentaDTO(cuentaActualizada);
    } else {
      logger.warn("Cuenta no encontrada!");
      throw new NoEncontrado("Cuenta no encontrada");
    }
  }

  @Override
  @Transactional
  public AccountDTO actualizacionParcialByFields(Long id, Map<String, Object> fields) {
    Optional<Account> cuentaOpt = cuentaRepository.findById(id);
    if (cuentaOpt.isPresent()) {
      fields.forEach((key, value) -> {
        if (key.equals("tipoCuenta")) {
          value = cuentaMapper.stringToTipoCuenta(value.toString());
        }
        if (key.equals("estado")) {
          value = cuentaMapper.stringToBoolean(value.toString());
        }
        Field field = ReflectionUtils.findField(Account.class, key);
        field.setAccessible(true);
        ReflectionUtils.setField(field, cuentaOpt.get(), value);
      });
      cuentaRepository.save(cuentaOpt.get());
      logger.info("Cuenta actualizada!");
      AccountDTO cuentaDTO = cuentaMapper.cuentaToCuentaDTO(cuentaOpt.get());
      return cuentaDTO;
    } else {
      logger.warn("Cuenta no encontrada!");
      throw new NoEncontrado("Cuenta no encontrada");
    }
  }
}