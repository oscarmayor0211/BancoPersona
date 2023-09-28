package com.bank.entity;

import com.bank.utility.enumerator.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movimientos")
public class Movement implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate fecha;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_de_movimiento")
  private MovementType tipoMovimiento;

  @NotNull
  private Double valor;

  @NotNull
  private Double saldo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "numero_de_cuenta")
  private Account cuenta;

}