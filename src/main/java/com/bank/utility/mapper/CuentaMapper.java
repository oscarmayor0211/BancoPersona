package com.bank.utility.mapper;

import com.bank.dto.AccountDTO;
import com.bank.entity.Account;
import com.bank.utility.enumerator.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface CuentaMapper {

  CuentaMapper MAPPER = Mappers.getMapper(CuentaMapper.class);

  Account cuentaDTOToCuenta(AccountDTO cuentaDTO);

  AccountDTO cuentaToCuentaDTO(Account cuenta);

  default String tipoCuentaToString(AccountType tipoCuenta) {
    return tipoCuenta.getValue();
  }

  default AccountType stringToTipoCuenta(String tipoCuenta) {
    return AccountType.fromValue(tipoCuenta);
  }

  default Boolean stringToBoolean(String valor) {
    return Boolean.valueOf(valor);
  }
}