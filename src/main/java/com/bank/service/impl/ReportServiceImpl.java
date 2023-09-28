package com.bank.service.impl;


import com.bank.dto.reporte.ReportAccountDTO;
import com.bank.dto.reporte.ReportMovementDTO;
import com.bank.dto.reporte.ReportDTO;
import com.bank.entity.Customer;
import com.bank.entity.Account;
import com.bank.entity.Movement;
import com.bank.service.CustomerService;
import com.bank.service.MovementService;
import com.bank.service.ReportService;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.mapper.MovimientoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

  private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
  private final CustomerService clienteService;
  private final MovementService movimientoService;
  private final MovimientoMapper movimientoMapper;


  @Autowired
  public ReportServiceImpl(CustomerService clienteService, MovementService movimientoService,
                           MovimientoMapper movimientoMapper) {
    this.clienteService = clienteService;
    this.movimientoService = movimientoService;
    this.movimientoMapper = movimientoMapper;
  }

  @Override
  public ReportDTO getReporte(Long clienteId, LocalDate fechaInicial, LocalDate fechafinal) {

    final Customer cliente = clienteService.getMovByClienteId(clienteId, fechaInicial, fechafinal);

    ReportDTO reporteDTO = new ReportDTO();
    reporteDTO.setClienteId(cliente.getClienteId());
    reporteDTO.setNombre(cliente.getNombre());
    reporteDTO.setIdentificacion(cliente.getIdentificacion());

    List<ReportAccountDTO> cuentaReporteDTOList = new ArrayList<>();

    for (Account cuenta : cliente.getCuentas()) {
      ReportAccountDTO cuentaReporteDTO = new ReportAccountDTO();
      cuentaReporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
      cuentaReporteDTO.setTipoCuenta(cuenta.getTipoCuenta().toString());
      cuentaReporteDTO.setSaldo(cuenta.getSaldoInicial());

      List<ReportMovementDTO> movimientosReporteDTO = new ArrayList<>();

      double totalRetiros = cuenta.getMovimientos().stream().filter(m -> "Retiro".equals(
          m.getTipoMovimiento().getValue())).mapToDouble(Movement::getValor).sum();

      double totalDepositos = cuenta.getMovimientos().stream().filter(m -> "Deposito".equals(
          m.getTipoMovimiento().getValue())).mapToDouble(Movement::getValor).sum();

      for (Movement m : cuenta.getMovimientos()) {
        ReportMovementDTO movimientoReporteDTO = new ReportMovementDTO();
        movimientoReporteDTO.setFecha(m.getFecha());
        movimientoReporteDTO.setTipoMovimiento(m.getTipoMovimiento());
        movimientoReporteDTO.setValor(m.getValor());
        movimientosReporteDTO.add(movimientoReporteDTO);
      }
      cuentaReporteDTO.setMovimientos(movimientosReporteDTO);
      cuentaReporteDTO.setTotalRetiros(totalRetiros);
      cuentaReporteDTO.setTotalDepositos(totalDepositos);
      cuentaReporteDTO.setSaldo(cuentaReporteDTO.getSaldo() + (totalDepositos - totalRetiros));
      cuentaReporteDTOList.add(cuentaReporteDTO);
    }
    reporteDTO.setCuentas(cuentaReporteDTOList);
    if (cliente == null) {
      logger.warn("Cliente no encontrado!");
      throw new NoEncontrado("Cliente no encontrado!");
    }
    logger.info("Reporte Generado!");
    return reporteDTO;
  }


}