package com.bank.utility.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MovementType {

  RETIRO("Retiro"), DEPOSITO("Deposito");

  private final String value;

  MovementType(String value) {
    this.value = value;
  }

  public static MovementType fromValue(String value) {
    for (MovementType tipoMovimiento : MovementType.values()) {
      if (tipoMovimiento.getValue().equals(value)) {
        return tipoMovimiento;
      }
    }
    throw new IllegalArgumentException("Tipo de movimiento inválido: " + value);
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}