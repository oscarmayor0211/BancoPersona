package com.bank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clientes")
public class Customer extends Person implements Serializable {

  /* Como la clase Persona ya tiene una PK definida con la anotación @Id, esta PK se hereda automáticamente a la clase Cliente.
  Esto significa que la clase Cliente también tendrá una PK llamada 'clienteId', que es la misma que la de la clase Persona*/
  @NotEmpty
  private String password;

  private Boolean estado;

  @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Account> cuentas;

}