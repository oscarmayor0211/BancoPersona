package com.bank.utility.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PeticionErronea extends RuntimeException {

  public PeticionErronea(String message) {
    super(message);
  }
}