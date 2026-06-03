package com.homefood.report.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales_reports", indexes = {
        @Index(name = "idx_report_seller_date", columnList = "sellerId, reportDate")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sellerId;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private String reportPeriod; // DAILY, WEEKLY, MONTHLY

    private int totalOrders;
    private int deliveredOrders;
    private int cancelledOrders;

    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;

    private double averageFoodRating;
    private int totalRatings;

    private int newCustomers;
    private int returningCustomers;

    private String topSellingFood;
    private int topSellingFoodQuantity;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
