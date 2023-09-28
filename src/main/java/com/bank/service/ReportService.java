package com.bank.service;


import com.bank.dto.reporte.ReportDTO;

import java.time.LocalDate;

public interface ReportService {

  ReportDTO getReporte(Long clienteId, LocalDate fechaInicial, LocalDate fechafinal);

}
