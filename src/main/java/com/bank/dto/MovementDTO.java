package com.bank.dto;

import com.bank.utility.enumerator.MovementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovementDTO {

  private Long id;

  @Enumerated(EnumType.STRING)
  private MovementType tipoMovimiento;

  @NotNull
  private Double valor;

  private Double saldo;

  private AccountDTO cuenta;
}