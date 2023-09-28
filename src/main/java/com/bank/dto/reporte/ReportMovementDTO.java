package com.bank.dto.reporte;

import com.bank.utility.enumerator.MovementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportMovementDTO {

  private MovementType tipoMovimiento;
  private LocalDate fecha;
  private Double valor;
}
