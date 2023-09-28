package com.bank.controller;


import com.bank.dto.CustomerDTO;
import com.bank.service.CustomerService;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.exceptions.PeticionErronea;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class CustomerController {

  private final CustomerService clienteService;

  @GetMapping
  public ResponseEntity<List<CustomerDTO>> getClientes() {
    List<CustomerDTO> clientes = clienteService.getClientes();
    return new ResponseEntity<>(clientes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerDTO> getCliente(@PathVariable Long id) {
    Optional<CustomerDTO> clienteOpt = Optional.ofNullable(clienteService.getCliente(id));
    if (clienteOpt.isEmpty()) {
      throw new NoEncontrado("Cliente no encontrado");
    }
    CustomerDTO clienteDTO = clienteOpt.get();
    return ResponseEntity.ok(clienteDTO);
  }

  @PostMapping
  public ResponseEntity<CustomerDTO> createCliente(@Valid @RequestBody CustomerDTO clienteDTO,
                                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new PeticionErronea(bindingResult.getAllErrors().toString());
    }
    CustomerDTO cliente = clienteService.createCliente(clienteDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerDTO> updateCliente(@PathVariable Long id,
                                                   @RequestBody CustomerDTO clienteDTO) {
    CustomerDTO clienteActualizado = clienteService.updateCliente(id, clienteDTO);
    return ResponseEntity.status(HttpStatus.OK).body(clienteActualizado);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CustomerDTO> actualizacionParcial(@PathVariable Long id,
                                                          @RequestBody Map<String, Object> fields) {
    CustomerDTO clienteActualizado = clienteService.actualizacionParcialByFields(id, fields);
    return ResponseEntity.status(HttpStatus.OK).body(clienteActualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CustomerDTO> deleteCliente(@PathVariable Long id) {
    clienteService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}