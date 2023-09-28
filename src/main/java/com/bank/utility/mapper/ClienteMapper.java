package com.bank.utility.mapper;


import com.bank.dto.CustomerDTO;
import com.bank.entity.Customer;
import com.bank.utility.enumerator.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface ClienteMapper {

  ClienteMapper MAPPER = Mappers.getMapper(ClienteMapper.class);

  Customer clienteDTOToCliente(CustomerDTO clienteDTO);

  CustomerDTO clienteToClienteDTO(Customer cliente);

  default String generoToString(Gender genero) {
    return genero.getValue();
  }

  default Gender stringToGenero(String genero) {
    return Gender.fromValue(genero);
  }

  default String booleanToString(Boolean estado) {
    return estado.toString();
  }

  default Boolean stringToBoolean(String estado) {
    return Boolean.valueOf(estado);
  }

}