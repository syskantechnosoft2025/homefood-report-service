# Report Service

Consumes events from all services and aggregates analytics/sales reports for sellers.

## Endpoints
- GET /api/v1/reports/seller — Seller's daily reports
- GET /api/v1/reports/seller/range?from=&to= — Reports by date range

## Tracked Metrics
- Total orders placed/delivered/cancelled per day
- Revenue and average order value
- Ratings count and average
