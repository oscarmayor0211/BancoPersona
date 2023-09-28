package com.bank.service.impl;


import com.bank.dto.CustomerDTO;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.entity.Movement;
import com.bank.repository.CustomerRepository;
import com.bank.utility.enumerator.Gender;
import com.bank.utility.exceptions.YaExiste;
import com.bank.utility.mapper.ClienteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

  @Mock
  private CustomerRepository mockClienteRepository;
  @Mock
  private ClienteMapper mockClienteMapper;

  private CustomerServiceImpl clienteServiceImplUnderTest;

  @BeforeEach
  void setUp() {
    clienteServiceImplUnderTest = new CustomerServiceImpl(mockClienteRepository, mockClienteMapper);
  }

  @Test
  void testGetClientes_ClienteRepositoryReturnsNoItems() {
    when(mockClienteRepository.findAll()).thenReturn(Collections.emptyList());
    final List<CustomerDTO> result = clienteServiceImplUnderTest.getClientes();
    assertThat(result).isEqualTo(Collections.emptyList());
  }


  @Test
  void testCreateCliente_ThrowsYaExiste() {
    final CustomerDTO clienteDTO = new CustomerDTO();
    clienteDTO.setClienteId(1L);
    clienteDTO.setNombre("Oscar Eduardo");
    clienteDTO.setGenero(Gender.MASCULINO);
    clienteDTO.setEdad(28);
    clienteDTO.setIdentificacion("identificacion");
    clienteDTO.setDireccion("Tulua Valle");
    clienteDTO.setTelefono("318274");
    clienteDTO.setPassword("52665");
    clienteDTO.setEstado(true);

    final Customer cliente = new Customer();
    cliente.setNombre("Oscar Eduardo");
    cliente.setGenero(Gender.MASCULINO);
    cliente.setEdad(18);
    cliente.setIdentificacion("identificacion");
    cliente.setDireccion("Tulua Valle");
    cliente.setTelefono("3162525");
    cliente.setPassword("52665");
    cliente.setEstado(true);

    final Account cuenta = new Account();
    final Movement movimiento = new Movement();
    movimiento.setFecha(LocalDate.of(2020, 1, 1));
    cuenta.setMovimientos(List.of(movimiento));
    cliente.setCuentas(List.of(cuenta));
    final Optional<Customer> clienteOptional = Optional.of(cliente);
    when(mockClienteRepository.findByIdentificacion("identificacion")).thenReturn(clienteOptional);

    assertThatThrownBy(() -> clienteServiceImplUnderTest.createCliente(clienteDTO))
        .isInstanceOf(YaExiste.class);
  }

  @Test
  void testDeleteById() {
    final Customer cliente = new Customer();
    cliente.setNombre("Mayra");
    cliente.setGenero(Gender.FEMENINO);
    cliente.setEdad(27);
    cliente.setIdentificacion("identificacion");
    cliente.setDireccion("Tulua");
    cliente.setTelefono("3221122");
    cliente.setPassword("mayramayor");
    cliente.setEstado(false);
    final Account cuenta = new Account();
    final Movement movimiento = new Movement();
    movimiento.setFecha(LocalDate.of(2023, 4, 8));
    cuenta.setMovimientos(List.of(movimiento));
    cliente.setCuentas(List.of(cuenta));
    final Optional<Customer> clienteOptional = Optional.of(cliente);
    when(mockClienteRepository.findById(0L)).thenReturn(clienteOptional);
    clienteServiceImplUnderTest.deleteById(0L);
    verify(mockClienteRepository).deleteById(0L);
  }

  @Test
  void testGetMovByClienteId() {
    final Customer expectedResult = new Customer();
    expectedResult.setNombre("Oscar");
    expectedResult.setGenero(Gender.MASCULINO);
    expectedResult.setEdad(28);
    expectedResult.setIdentificacion("identificacion");
    expectedResult.setDireccion("direccion");
    expectedResult.setTelefono("telefono");
    expectedResult.setPassword("password");
    expectedResult.setEstado(false);
    final Account cuenta = new Account();
    final Movement movimiento = new Movement();
    movimiento.setFecha(LocalDate.of(2020, 1, 1));
    cuenta.setMovimientos(List.of(movimiento));
    expectedResult.setCuentas(List.of(cuenta));

    final Customer cliente = new Customer();
    cliente.setNombre("Andres");
    cliente.setGenero(Gender.MASCULINO);
    cliente.setEdad(0);
    cliente.setIdentificacion("identificacion");
    cliente.setDireccion("calle 27");
    cliente.setTelefono("32424234");
    cliente.setPassword("password");
    cliente.setEstado(false);
    final Account cuenta1 = new Account();
    final Movement movimiento1 = new Movement();
    movimiento1.setFecha(LocalDate.of(2020, 1, 1));
    cuenta1.setMovimientos(List.of(movimiento1));
    cliente.setCuentas(List.of(cuenta1));
    final Optional<Customer> clienteOptional = Optional.of(cliente);
    when(mockClienteRepository.findById(0L)).thenReturn(clienteOptional);

    final Customer result = clienteServiceImplUnderTest.getMovByClienteId(0L,
        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));

    assertThat(result).isEqualTo(expectedResult);
  }
}
