package com.homefood.report.service;

import com.homefood.report.entity.SalesReport;
import com.homefood.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public void recordOrderEvent(UUID sellerId, String eventType, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        SalesReport report = reportRepository
                .findBySellerIdAndReportDateAndReportPeriod(sellerId, today, "DAILY")
                .orElse(SalesReport.builder()
                        .sellerId(sellerId)
                        .reportDate(today)
                        .reportPeriod("DAILY")
                        .totalOrders(0)
                        .deliveredOrders(0)
                        .cancelledOrders(0)
                        .totalRevenue(BigDecimal.ZERO)
                        .build());

        switch (eventType) {
            case "ORDER_PLACED" -> report.setTotalOrders(report.getTotalOrders() + 1);
            case "ORDER_DELIVERED" -> {
                report.setDeliveredOrders(report.getDeliveredOrders() + 1);
                if (amount != null) {
                    report.setTotalRevenue(report.getTotalRevenue().add(amount));
                    int count = report.getDeliveredOrders();
                    report.setAverageOrderValue(report.getTotalRevenue().divide(BigDecimal.valueOf(count), 2, java.math.RoundingMode.HALF_UP));
                }
            }
            case "ORDER_CANCELLED" -> report.setCancelledOrders(report.getCancelledOrders() + 1);
            case "RATING_SUBMITTED" -> report.setTotalRatings(report.getTotalRatings() + 1);
        }

        reportRepository.save(report);
        log.debug("Report updated for seller {} event {}", sellerId, eventType);
    }

    public List<SalesReport> getSellerReports(UUID sellerId) {
        return reportRepository.findBySellerIdOrderByReportDateDesc(sellerId);
    }

    public List<SalesReport> getSellerReportsByDateRange(UUID sellerId, LocalDate from, LocalDate to) {
        return reportRepository.findBySellerAndDateRange(sellerId, from, to);
    }
}
