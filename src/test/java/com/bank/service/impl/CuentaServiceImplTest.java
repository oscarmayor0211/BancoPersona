package com.bank.service.impl;


import com.bank.dto.AccountDTO;
import com.bank.dto.CustomerDTO;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.MovementRepository;
import com.bank.utility.enumerator.AccountType;
import com.bank.utility.mapper.CuentaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

  @Mock
  private AccountRepository mockCuentaRepository;
  @Mock
  private CuentaMapper mockCuentaMapper;
  @Mock
  private CustomerRepository mockClienteRepository;
  @Mock
  private MovementRepository mockMovimientoRepository;

  private AccountServiceImpl cuentaServiceImplUnderTest;

  @BeforeEach
  void setUp() {
    cuentaServiceImplUnderTest = new AccountServiceImpl(mockCuentaRepository, mockCuentaMapper,
        mockClienteRepository, mockMovimientoRepository);
  }

  @Test
  void testGetCuenta() {
    final AccountDTO expectedResult = new AccountDTO();
    expectedResult.setId(1L);
    expectedResult.setNumeroCuenta("numeroCuenta");
    expectedResult.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente = new CustomerDTO();
    cliente.setIdentificacion("identificacion");
    expectedResult.setCliente(cliente);
    final Account cuenta1 = new Account();
    cuenta1.setNumeroCuenta("numeroCuenta");
    cuenta1.setTipoCuenta(AccountType.AHORRO);
    cuenta1.setSaldoInicial(0.0);
    cuenta1.setEstado(false);
    final Customer cliente1 = new Customer();
    cuenta1.setCliente(cliente1);
    final Optional<Account> cuenta = Optional.of(cuenta1);
    when(mockCuentaRepository.findById(1L)).thenReturn(cuenta);
    final AccountDTO cuentaDTO = new AccountDTO();
    cuentaDTO.setId(1L);
    cuentaDTO.setNumeroCuenta("numeroCuenta");
    cuentaDTO.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente2 = new CustomerDTO();
    cliente2.setIdentificacion("identificacion");
    cuentaDTO.setCliente(cliente2);
    final Account cuenta2 = new Account();
    cuenta2.setNumeroCuenta("numeroCuenta");
    cuenta2.setTipoCuenta(AccountType.AHORRO);
    cuenta2.setSaldoInicial(0.0);
    cuenta2.setEstado(false);
    final Customer cliente3 = new Customer();
    cuenta2.setCliente(cliente3);
    when(mockCuentaMapper.cuentaToCuentaDTO(cuenta2)).thenReturn(cuentaDTO);
    final AccountDTO result = cuentaServiceImplUnderTest.getCuenta(1L);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testUpdateCuenta() {
    final AccountDTO cuentaDTO = new AccountDTO();
    cuentaDTO.setId(1L);
    cuentaDTO.setNumeroCuenta("numeroCuenta");
    cuentaDTO.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente = new CustomerDTO();
    cliente.setIdentificacion("identificacion");
    cuentaDTO.setCliente(cliente);
    final AccountDTO expectedResult = new AccountDTO();
    expectedResult.setId(1L);
    expectedResult.setNumeroCuenta("numeroCuenta");
    expectedResult.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente1 = new CustomerDTO();
    cliente1.setIdentificacion("identificacion");
    expectedResult.setCliente(cliente1);
    final Account cuenta1 = new Account();
    cuenta1.setNumeroCuenta("numeroCuenta");
    cuenta1.setTipoCuenta(AccountType.AHORRO);
    cuenta1.setSaldoInicial(0.0);
    cuenta1.setEstado(false);
    final Customer cliente2 = new Customer();
    cuenta1.setCliente(cliente2);
    final Optional<Account> cuenta = Optional.of(cuenta1);
    when(mockCuentaRepository.findById(1L)).thenReturn(cuenta);
    final Account cuenta2 = new Account();
    cuenta2.setNumeroCuenta("numeroCuenta");
    cuenta2.setTipoCuenta(AccountType.AHORRO);
    cuenta2.setSaldoInicial(0.0);
    cuenta2.setEstado(false);
    final Customer cliente3 = new Customer();
    cuenta2.setCliente(cliente3);
    final AccountDTO cuentaDTO1 = new AccountDTO();
    cuentaDTO1.setId(1L);
    cuentaDTO1.setNumeroCuenta("numeroCuenta");
    cuentaDTO1.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente4 = new CustomerDTO();
    cliente4.setIdentificacion("identificacion");
    cuentaDTO1.setCliente(cliente4);
    when(mockCuentaMapper.cuentaDTOToCuenta(cuentaDTO1)).thenReturn(cuenta2);
    final AccountDTO cuentaDTO2 = new AccountDTO();
    cuentaDTO2.setId(1L);
    cuentaDTO2.setNumeroCuenta("numeroCuenta");
    cuentaDTO2.setTipoCuenta(AccountType.AHORRO);
    final CustomerDTO cliente5 = new CustomerDTO();
    cliente5.setIdentificacion("identificacion");
    cuentaDTO2.setCliente(cliente5);
    final Account cuenta3 = new Account();
    cuenta3.setNumeroCuenta("numeroCuenta");
    cuenta3.setTipoCuenta(AccountType.AHORRO);
    cuenta3.setSaldoInicial(0.0);
    cuenta3.setEstado(false);
    final Customer cliente6 = new Customer();
    cuenta3.setCliente(cliente6);
    when(mockCuentaMapper.cuentaToCuentaDTO(cuenta3)).thenReturn(cuentaDTO2);
    final AccountDTO result = cuentaServiceImplUnderTest.updateCuenta(1L, cuentaDTO);
    assertThat(result).isEqualTo(expectedResult);
    final Account entity = new Account();
    entity.setNumeroCuenta("numeroCuenta");
    entity.setTipoCuenta(AccountType.AHORRO);
    entity.setSaldoInicial(0.0);
    entity.setEstado(false);
    final Customer cliente7 = new Customer();
    entity.setCliente(cliente7);
    verify(mockCuentaRepository).save(entity);
  }
}
