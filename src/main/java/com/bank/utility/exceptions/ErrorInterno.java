package com.bank.utility.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErrorInterno extends RuntimeException {

  public ErrorInterno(String message) {
    super(message);
  }
}