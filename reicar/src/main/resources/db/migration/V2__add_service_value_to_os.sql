-- V2__add_service_value_to_os.sql

ALTER TABLE service_orders
ADD COLUMN service_value DECIMAL(10, 2) NOT NULL DEFAULT 0.00 AFTER total_value;