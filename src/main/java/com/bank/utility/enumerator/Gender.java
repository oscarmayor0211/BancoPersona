package com.bank.utility.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Gender {

  MASCULINO("Masculino"), FEMENINO("Femenino"), NO_BINARIO("No Binario"), OTRO("Otro");

  private final String value;

  Gender(String value) {
    this.value = value;
  }

  public static Gender fromValue(String value) {
    for (Gender genero : Gender.values()) {
      if (genero.getValue().equals(value)) {
        return genero;
      }
    }
    throw new IllegalArgumentException("Genero inválido: " + value);
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}