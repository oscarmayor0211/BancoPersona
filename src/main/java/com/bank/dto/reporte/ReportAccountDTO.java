package com.bank.dto.reporte;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportAccountDTO {

  private String numeroCuenta;
  private String tipoCuenta;
  private Double saldo;
  private Double totalRetiros;
  private Double totalDepositos;
  private List<ReportMovementDTO> movimientos;

}
