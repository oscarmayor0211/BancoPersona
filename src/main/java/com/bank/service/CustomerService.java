package com.bank.service;



import com.bank.dto.CustomerDTO;
import com.bank.entity.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomerService {

  List<CustomerDTO> getClientes();

  CustomerDTO getCliente(Long clienteId);

  CustomerDTO createCliente(CustomerDTO clienteDTO);

  CustomerDTO updateCliente(Long clienteId, CustomerDTO clienteDTO);

  CustomerDTO actualizacionParcialByFields(Long clienteId, Map<String, Object> fields);

  void deleteById(Long clienteId);

  Customer getMovByClienteId(Long clienteId, LocalDate fechaInicial,
                             LocalDate fechaFinal);

}