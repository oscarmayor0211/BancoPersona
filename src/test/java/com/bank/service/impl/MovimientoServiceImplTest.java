package com.bank.service.impl;

import com.bank.dto.AccountDTO;
import com.bank.dto.MovementDTO;
import com.bank.entity.Account;
import com.bank.entity.Movement;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.MovementRepository;
import com.bank.utility.enumerator.MovementType;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.mapper.MovimientoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

  @Mock
  private MovementRepository mockMovimientoRepository;
  @Mock
  private MovimientoMapper mockMovimientoMapper;
  @Mock
  private AccountRepository mockCuentaRepository;
  @Mock
  private CustomerRepository mockClienteRepository;

  private MovementServiceImpl movimientoServiceImplUnderTest;

  @BeforeEach
  void setUp() {
    movimientoServiceImplUnderTest = new MovementServiceImpl(mockMovimientoRepository,
        mockMovimientoMapper, mockCuentaRepository, mockClienteRepository);
  }

  @Test
  void testGetMovimientos() {
    // Setup
    final MovementDTO movimientoDTO = new MovementDTO();
    movimientoDTO.setId(0L);
    movimientoDTO.setTipoMovimiento(MovementType.RETIRO);
    movimientoDTO.setValor(0.0);
    final AccountDTO cuenta = new AccountDTO();
    cuenta.setNumeroCuenta("numeroCuenta");
    movimientoDTO.setCuenta(cuenta);
    final List<MovementDTO> expectedResult = List.of(movimientoDTO);
    final Movement movimiento = new Movement();
    movimiento.setFecha(LocalDate.of(2020, 1, 1));
    movimiento.setTipoMovimiento(MovementType.RETIRO);
    movimiento.setValor(0.0);
    movimiento.setSaldo(0.0);
    final Account cuenta1 = new Account();
    cuenta1.setSaldoInicial(0.0);
    cuenta1.setMovimientos(List.of(new Movement()));
    movimiento.setCuenta(cuenta1);
    final List<Movement> movimientos = List.of(movimiento);
    when(mockMovimientoRepository.findAll()).thenReturn(movimientos);
    final MovementDTO movimientoDTO1 = new MovementDTO();
    movimientoDTO1.setId(0L);
    movimientoDTO1.setTipoMovimiento(MovementType.RETIRO);
    movimientoDTO1.setValor(0.0);
    final AccountDTO cuenta2 = new AccountDTO();
    cuenta2.setNumeroCuenta("numeroCuenta");
    movimientoDTO1.setCuenta(cuenta2);
    final Movement movimiento1 = new Movement();
    movimiento1.setFecha(LocalDate.of(2020, 1, 1));
    movimiento1.setTipoMovimiento(MovementType.RETIRO);
    movimiento1.setValor(0.0);
    movimiento1.setSaldo(0.0);
    final Account cuenta3 = new Account();
    cuenta3.setSaldoInicial(0.0);
    cuenta3.setMovimientos(List.of(new Movement()));
    movimiento1.setCuenta(cuenta3);
    when(mockMovimientoMapper.movimientoToMovimientoDTO(movimiento1)).thenReturn(movimientoDTO1);
    final List<MovementDTO> result = movimientoServiceImplUnderTest.getMovimientos();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testGetMovimiento() {
    final MovementDTO expectedResult = new MovementDTO();
    expectedResult.setId(0L);
    expectedResult.setTipoMovimiento(MovementType.RETIRO);
    expectedResult.setValor(0.0);
    final AccountDTO cuenta = new AccountDTO();
    cuenta.setNumeroCuenta("numeroCuenta");
    expectedResult.setCuenta(cuenta);
    final Movement movimiento1 = new Movement();
    movimiento1.setFecha(LocalDate.of(2020, 1, 1));
    movimiento1.setTipoMovimiento(MovementType.RETIRO);
    movimiento1.setValor(0.0);
    movimiento1.setSaldo(0.0);
    final Account cuenta1 = new Account();
    cuenta1.setSaldoInicial(0.0);
    cuenta1.setMovimientos(List.of(new Movement()));
    movimiento1.setCuenta(cuenta1);
    final Optional<Movement> movimiento = Optional.of(movimiento1);
    when(mockMovimientoRepository.findById(0L)).thenReturn(movimiento);
    final MovementDTO movimientoDTO = new MovementDTO();
    movimientoDTO.setId(0L);
    movimientoDTO.setTipoMovimiento(MovementType.RETIRO);
    movimientoDTO.setValor(0.0);
    final AccountDTO cuenta2 = new AccountDTO();
    cuenta2.setNumeroCuenta("numeroCuenta");
    movimientoDTO.setCuenta(cuenta2);
    final Movement movimiento2 = new Movement();
    movimiento2.setFecha(LocalDate.of(2020, 1, 1));
    movimiento2.setTipoMovimiento(MovementType.RETIRO);
    movimiento2.setValor(0.0);
    movimiento2.setSaldo(0.0);
    final Account cuenta3 = new Account();
    cuenta3.setSaldoInicial(0.0);
    cuenta3.setMovimientos(List.of(new Movement()));
    movimiento2.setCuenta(cuenta3);
    when(mockMovimientoMapper.movimientoToMovimientoDTO(movimiento2)).thenReturn(movimientoDTO);
    final MovementDTO result = movimientoServiceImplUnderTest.getMovimiento(0L);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testCreateMovimiento_CuentaRepositoryReturnsAbsent() {
    final MovementDTO movimientoDTO = new MovementDTO();
    movimientoDTO.setId(0L);
    movimientoDTO.setTipoMovimiento(MovementType.RETIRO);
    movimientoDTO.setValor(0.0);
    final AccountDTO cuenta = new AccountDTO();
    cuenta.setNumeroCuenta("numeroCuenta");
    movimientoDTO.setCuenta(cuenta);
    when(mockCuentaRepository.findByNumeroCuenta("numeroCuenta")).thenReturn(Optional.empty());
    assertThatThrownBy(
        () -> movimientoServiceImplUnderTest.createMovimiento(movimientoDTO))
        .isInstanceOf(NoEncontrado.class);
  }


  @Test
  void testUpdateMovimiento_MovimientoRepositoryFindByIdReturnsAbsent() {
    // Setup
    final MovementDTO movimientoDTO = new MovementDTO();
    movimientoDTO.setId(0L);
    movimientoDTO.setTipoMovimiento(MovementType.RETIRO);
    movimientoDTO.setValor(0.0);
    final AccountDTO cuenta = new AccountDTO();
    cuenta.setNumeroCuenta("numeroCuenta");
    movimientoDTO.setCuenta(cuenta);
    when(mockMovimientoRepository.findById(0L)).thenReturn(Optional.empty());
    assertThatThrownBy(
        () -> movimientoServiceImplUnderTest.updateMovimiento(0L, movimientoDTO))
        .isInstanceOf(NoEncontrado.class);
  }

  @Test
  void testGetUltimoMovimiento() {
    final Movement movimiento = new Movement();
    movimiento.setFecha(LocalDate.of(2020, 1, 1));
    movimiento.setTipoMovimiento(MovementType.RETIRO);
    movimiento.setValor(0.0);
    movimiento.setSaldo(0.0);
    final Account cuenta = new Account();
    cuenta.setSaldoInicial(0.0);
    cuenta.setMovimientos(List.of(new Movement()));
    movimiento.setCuenta(cuenta);
    final List<Movement> movimientos = List.of(movimiento);
    final double result = movimientoServiceImplUnderTest.getUltimoMovimiento(movimientos);
    assertThat(result).isEqualTo(0.0, within(0.0001));
  }
}
