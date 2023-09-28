package com.bank.controller;

import com.bank.dto.MovementDTO;
import com.bank.service.MovementService;
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
@RequestMapping("/movimientos")
public class MovementController {

  @Autowired
  private MovementService movimientoService;

  @GetMapping
  public ResponseEntity<List<MovementDTO>> getMovimientos() {
    List<MovementDTO> movimientos = movimientoService.getMovimientos();
    return new ResponseEntity<>(movimientos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovementDTO> getMovimiento(@PathVariable Long id) {
    Optional<MovementDTO> movimientoOpt = Optional.ofNullable(
        movimientoService.getMovimiento(id));
    if (movimientoOpt.isEmpty()) {
      throw new NoEncontrado("Movimiento no encontrado");
    }
    MovementDTO movimientoDTO = movimientoOpt.get();
    return ResponseEntity.ok(movimientoDTO);
  }

  @PostMapping
  public ResponseEntity<?> createMovimiento(@Valid @RequestBody MovementDTO movimientoDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new PeticionErronea(bindingResult.getAllErrors().toString());
    }
    MovementDTO movimiento = movimientoService.createMovimiento(movimientoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MovementDTO> updateMovimiento(@PathVariable Long id,
                                                      @RequestBody MovementDTO movimientoDTO) {
    MovementDTO movimientoActualizado = movimientoService.updateMovimiento(id, movimientoDTO);
    return ResponseEntity.status(HttpStatus.OK).body(movimientoActualizado);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> actualizacionParcial(@PathVariable Long id,
      @RequestBody Map<String, Object> fields) {
    MovementDTO movimientoActualizado = movimientoService.actualizacionParcialByFields(id,
        fields);
    return ResponseEntity.status(HttpStatus.OK).body(movimientoActualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MovementDTO> deleteMovimiento(@PathVariable Long id) {
    Optional<MovementDTO> movimientoOpt = Optional.ofNullable(
        movimientoService.getMovimiento(id));
    if (movimientoOpt.isEmpty()) {
      throw new NoEncontrado("Movimiento no entontrado");
    }
    movimientoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}