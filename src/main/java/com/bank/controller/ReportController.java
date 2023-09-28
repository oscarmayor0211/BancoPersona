package com.bank.controller;

import com.bank.dto.reporte.ReportDTO;
import com.bank.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reportes")
public class ReportController {

  private ReportService reporteService;

  @Autowired
  public ReportController(ReportService reporteService) {
    this.reporteService = reporteService;
  }

  @GetMapping
  @Transient
  public ResponseEntity<ReportDTO> getReporte(@Valid @RequestParam("clienteId") Long clienteId,
                                              @RequestParam("fechaInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicial,
                                              @RequestParam("fechaFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFinal) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reporteService.getReporte(clienteId, fechaInicial, fechaFinal));
  }
}