package com.bank.utility.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AccountType {

  AHORRO("Ahorro"), CORRIENTE("Corriente");

  private final String value;

  AccountType(String value) {
    this.value = value;
  }

  public static AccountType fromValue(String value) {
    for (AccountType tipoCuenta : AccountType.values()) {
      if (tipoCuenta.getValue().equals(value)) {
        return tipoCuenta;
      }
    }
    throw new IllegalArgumentException("Tipo de cuenta inválido: " + value);
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}