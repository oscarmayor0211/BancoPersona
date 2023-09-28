package com.bank.service;



import com.bank.dto.AccountDTO;

import java.util.List;
import java.util.Map;

public interface AccountService {

  List<AccountDTO> getCuentas();

  AccountDTO getCuenta(Long id);

  AccountDTO createCuenta(AccountDTO cuentaDTO);

  AccountDTO updateCuenta(Long id, AccountDTO cuentaDTO);

  AccountDTO actualizacionParcialByFields(Long id, Map<String, Object> fields);

  void deleteById(Long id);

}