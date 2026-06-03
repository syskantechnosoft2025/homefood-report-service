package com.homefood.report.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homefood.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportEventConsumer {

    private final ReportService reportService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "report.event", groupId = "report-service")
    public void handleReportEvent(@Payload String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            String type = node.get("type").asText();

            switch (type) {
                case "ORDER_PLACED", "ORDER_DELIVERED", "ORDER_CANCELLED", "RATING_SUBMITTED", "FOOD_CREATED" -> {
                    if (node.has("sellerId")) {
                        UUID sellerId = UUID.fromString(node.get("sellerId").asText());
                        BigDecimal amount = node.has("totalAmount") ? new BigDecimal(node.get("totalAmount").asText()) : null;
                        reportService.recordOrderEvent(sellerId, type, amount);
                    }
                }
                default -> log.debug("Unhandled report event type: {}", type);
            }
        } catch (Exception e) {
            log.error("Error processing report event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "order.delivered", groupId = "report-service-orders")
    public void handleOrderDelivered(@Payload String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            UUID sellerId = UUID.fromString(node.get("sellerId").asText());
            reportService.recordOrderEvent(sellerId, "ORDER_DELIVERED", null);
        } catch (Exception e) {
            log.error("Error handling order.delivered in report service: {}", e.getMessage());
        }
    }
}
