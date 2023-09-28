package com.bank.dto;

import com.bank.utility.enumerator.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {

  private Long clienteId;

  @NotEmpty
  @NotBlank
  @Pattern(regexp = "^[A-Za-z]+(?:\\s[A-Za-z]+)*$", message = "Verificar el nombre")
  private String nombre;

  @Enumerated(EnumType.STRING)
  private Gender genero;

  @NotNull
  @Min(1)
  @Max(120)
  private Integer edad;

  @NotEmpty
  @NotBlank
  @Pattern(regexp = "^[0-9]{6,13}$", message = "Verificar el número de identificación")
  private String identificacion;

  @NotEmpty
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9\\s#-]+$", message = "Verificar la dirección")
  private String direccion;

  @NotEmpty
  @NotBlank
  @Pattern(regexp = "^[0-9]{6,13}$", message = "Verificar el número de teléfono")
  private String telefono;

  private Boolean estado;

  @NotEmpty
  @NotBlank
  private String password;
}