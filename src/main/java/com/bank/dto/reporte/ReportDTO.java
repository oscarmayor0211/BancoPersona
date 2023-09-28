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
public class ReportDTO {

  private Long clienteId;

  private String nombre;

  private String identificacion;

  private List<ReportAccountDTO> cuentas;
}