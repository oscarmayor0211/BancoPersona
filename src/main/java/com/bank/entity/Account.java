package com.bank.entity;

import com.bank.utility.enumerator.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuentas")
public class Account implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Pattern(regexp = "^[0-9]{6,11}$", message = "Verificar el número de la cuenta")
  @Column(name = "numero_de_cuenta", unique = true)
  private String numeroCuenta;


  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_de_cuenta")
  private AccountType tipoCuenta;

  @NotNull
  @Column(name = "saldo_inicial")
  private double saldoInicial;

  private Boolean estado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id")
  private Customer cliente;

  @OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Movement> movimientos;

}