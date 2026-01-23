-- V1__create_tables_os.sql

CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    city VARCHAR(100),
    state CHAR(2)
);

CREATE TABLE service_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    entry_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    service_type VARCHAR(20) NOT NULL,
    total_value DECIMAL(10, 2),
    customer_id BIGINT,

    -- Mechanic fields
    technical_diagnosis TEXT,
    vehicle_km INT,

    -- Tire Shop fields
    tire_position VARCHAR(50),

    CONSTRAINT fk_so_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE service_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    service_order_id BIGINT NOT NULL,

    CONSTRAINT fk_items_so FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
);