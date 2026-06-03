package com.homefood.report.controller;

import com.homefood.report.entity.SalesReport;
import com.homefood.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/seller")
    public ResponseEntity<List<SalesReport>> getSellerReports(@RequestHeader("X-User-Id") UUID sellerId) {
        return ResponseEntity.ok(reportService.getSellerReports(sellerId));
    }

    @GetMapping("/seller/range")
    public ResponseEntity<List<SalesReport>> getSellerReportsByRange(
            @RequestHeader("X-User-Id") UUID sellerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(reportService.getSellerReportsByDateRange(sellerId, from, to));
    }
}
