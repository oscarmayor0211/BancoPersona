package com.bank.utility.mapper;

import com.bank.dto.MovementDTO;
import com.bank.entity.Movement;
import com.bank.utility.enumerator.MovementType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface MovimientoMapper {

  MovimientoMapper MAPPER = Mappers.getMapper(MovimientoMapper.class);

  Movement movimientoDTOToMovimiento(MovementDTO movimientoDTO);

  MovementDTO movimientoToMovimientoDTO(Movement movimiento);

  default String tipoMovimientoToString(MovementType tipoMovimiento) {
    return tipoMovimiento.getValue();
  }

  default MovementType stringToTipoMovimiento(String tipoMovimiento) {
    return MovementType.fromValue(tipoMovimiento);
  }

}
