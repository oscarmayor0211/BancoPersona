package com.bank.entity;

import com.bank.utility.enumerator.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("null")
@Table(name = "personas")
public class Person implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cliente_id")
  private Long clienteId;

  @NotEmpty
  @Pattern(regexp = "^[A-Za-z]+(?:\\s[A-Za-z]+)*$", message = "Verificar el nombre")
  private String nombre;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Gender genero;

  @NotNull
  @Min(0)
  @Max(120)
  private Integer edad;

  @NotEmpty
  @Column(unique = true)
  @Pattern(regexp = "^[0-9]{6,13}$", message = "Verificar el número de identificación")
  private String identificacion;

  @NotEmpty
  @Pattern(regexp = "^[A-Za-z0-9\\s#-]+$", message = "Verificar la dirección")
  private String direccion;

  @NotEmpty
  @Pattern(regexp = "^[0-9]{6,13}$", message = "Verificar el número de teléfono")
  private String telefono;

}