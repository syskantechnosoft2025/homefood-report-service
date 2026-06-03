package com.homefood.report;

import com.homefood.report.entity.SalesReport;
import com.homefood.report.repository.ReportRepository;
import com.homefood.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceUnitTest {

    @Mock ReportRepository reportRepository;
    @InjectMocks ReportService reportService;

    private UUID sellerId;

    @BeforeEach
    void setUp() {
        sellerId = UUID.randomUUID();
    }

    @Test
    void recordOrderEvent_orderPlaced_incrementsCount() {
        when(reportRepository.findBySellerIdAndReportDateAndReportPeriod(eq(sellerId), any(), eq("DAILY")))
            .thenReturn(Optional.empty());
        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThatCode(() -> reportService.recordOrderEvent(sellerId, "ORDER_PLACED", null))
            .doesNotThrowAnyException();
        verify(reportRepository).save(argThat(r -> r.getTotalOrders() == 1));
    }

    @Test
    void recordOrderEvent_orderDelivered_updatesRevenue() {
        when(reportRepository.findBySellerIdAndReportDateAndReportPeriod(eq(sellerId), any(), eq("DAILY")))
            .thenReturn(Optional.empty());
        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThatCode(() -> reportService.recordOrderEvent(sellerId, "ORDER_DELIVERED", new BigDecimal("250")))
            .doesNotThrowAnyException();
        verify(reportRepository).save(argThat(r -> r.getTotalRevenue().compareTo(new BigDecimal("250")) == 0));
    }

    @Test
    void recordOrderEvent_orderCancelled_incrementsCancelledCount() {
        when(reportRepository.findBySellerIdAndReportDateAndReportPeriod(eq(sellerId), any(), eq("DAILY")))
            .thenReturn(Optional.empty());
        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThatCode(() -> reportService.recordOrderEvent(sellerId, "ORDER_CANCELLED", null))
            .doesNotThrowAnyException();
        verify(reportRepository).save(argThat(r -> r.getCancelledOrders() == 1));
    }

    @Test
    void getSellerReports_returnsList() {
        SalesReport report = SalesReport.builder()
            .sellerId(sellerId)
            .reportDate(LocalDate.now())
            .reportPeriod("DAILY")
            .totalOrders(5)
            .totalRevenue(new BigDecimal("1000"))
            .build();

        when(reportRepository.findBySellerIdOrderByReportDateDesc(sellerId))
            .thenReturn(List.of(report));

        List<SalesReport> result = reportService.getSellerReports(sellerId);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTotalOrders()).isEqualTo(5);
    }

    @Test
    void getSellerReportsByDateRange_returnsList() {
        when(reportRepository.findBySellerAndDateRange(eq(sellerId), any(), any()))
            .thenReturn(List.of(new SalesReport()));

        List<SalesReport> result = reportService.getSellerReportsByDateRange(
            sellerId, LocalDate.now().minusDays(7), LocalDate.now());
        assertThat(result).hasSize(1);
    }
}
