package com.homefood.report.repository;

import com.homefood.report.entity.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<SalesReport, UUID> {
    List<SalesReport> findBySellerIdOrderByReportDateDesc(UUID sellerId);
    Optional<SalesReport> findBySellerIdAndReportDateAndReportPeriod(UUID sellerId, LocalDate date, String period);

    @Query("SELECT r FROM SalesReport r WHERE r.sellerId = :sellerId AND r.reportDate BETWEEN :from AND :to ORDER BY r.reportDate")
    List<SalesReport> findBySellerAndDateRange(@Param("sellerId") UUID sellerId,
                                               @Param("from") LocalDate from,
                                               @Param("to") LocalDate to);
}
