package com.bank.utility.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class YaExiste extends RuntimeException {

  public YaExiste(String message) {
    super(message);
  }
}