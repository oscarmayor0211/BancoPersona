package com.bank.controller;

import com.bank.dto.AccountDTO;
import com.bank.service.AccountService;
import com.bank.utility.exceptions.NoEncontrado;
import com.bank.utility.exceptions.PeticionErronea;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cuentas")
public class AccountController {

  @Autowired
  private AccountService cuentaService;

  @GetMapping
  public ResponseEntity<List<AccountDTO>> getCuentas() {
    List<AccountDTO> cuentas = cuentaService.getCuentas();
    return new ResponseEntity<>(cuentas, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccountDTO> getCuenta(@PathVariable Long id) {
    Optional<AccountDTO> cuentaOpt = Optional.ofNullable(cuentaService.getCuenta(id));
    if (cuentaOpt.isEmpty()) {
      throw new NoEncontrado("Cuenta no encontrada");
    }
    AccountDTO cuentaDTO = cuentaOpt.get();
    return ResponseEntity.ok(cuentaDTO);
  }

  @PostMapping
  public ResponseEntity<AccountDTO> createCuenta(@Valid @RequestBody AccountDTO cuentaDTO,
                                                 BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new PeticionErronea(bindingResult.getAllErrors().toString());
    }
    AccountDTO cuenta = cuentaService.createCuenta(cuentaDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AccountDTO> updateCuenta(@PathVariable Long id,
                                                 @RequestBody AccountDTO cuentaDTO) {
    try {
      AccountDTO cuentaActualizada = cuentaService.updateCuenta(id, cuentaDTO);
      return ResponseEntity.status(HttpStatus.OK).body(cuentaActualizada);
    } catch (Exception e) {
      throw new PeticionErronea("Verificar los datos de la cuenta");
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<AccountDTO> actualizacionParcial(@PathVariable Long id,
                                                         @RequestBody Map<String, Object> fields) {
    try {
      AccountDTO cuentaActualizada = cuentaService.actualizacionParcialByFields(id, fields);
      return ResponseEntity.status(HttpStatus.OK).body(cuentaActualizada);
    } catch (Exception e) {
      throw new PeticionErronea("Verificar los datos de la cuenta");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCuenta(@PathVariable Long id) {
    //Validar Saldo en la cuenta para devolver dinero al cliente en caso de saldo positivo
    try {
      cuentaService.deleteById(id);
      return ResponseEntity.ok("La cuenta con ID " + id + " ha sido eliminada correctamente.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Ocurrió un error al eliminar la cuenta.");
    }
  }
}