package com.bank.service;

import com.bank.dto.MovementDTO;
import com.bank.entity.Movement;

import java.util.List;
import java.util.Map;

public interface MovementService {

  List<MovementDTO> getMovimientos();

  MovementDTO getMovimiento(Long id);

  MovementDTO createMovimiento(MovementDTO movimientoDTO);

  MovementDTO updateMovimiento(Long id, MovementDTO movimientoDTO);

  MovementDTO actualizacionParcialByFields(Long id, Map<String, Object> fields);

  void deleteById(Long id);

  double getUltimoMovimiento(List<Movement> movimientos);

}