package com.homefood.report;

import com.homefood.report.controller.ReportController;
import com.homefood.report.entity.SalesReport;
import com.homefood.report.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerUnitTest {

    @Mock ReportService reportService;
    @InjectMocks ReportController reportController;

    @Test
    void getSellerReports_returns200() {
        UUID sellerId = UUID.randomUUID();
        SalesReport report = SalesReport.builder()
            .sellerId(sellerId)
            .reportDate(LocalDate.now())
            .reportPeriod("DAILY")
            .totalOrders(3)
            .totalRevenue(new BigDecimal("750"))
            .build();

        when(reportService.getSellerReports(sellerId)).thenReturn(List.of(report));

        ResponseEntity<List<SalesReport>> result = reportController.getSellerReports(sellerId);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(1);
    }
}
