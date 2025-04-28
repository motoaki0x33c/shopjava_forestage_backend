CREATE TABLE products (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(191) NOT NULL,
    price INT NOT NULL,
    sku VARCHAR(191),
    route VARCHAR(191) NOT NULL UNIQUE,
    description TEXT,
    quantity INT NOT NULL,
    status BOOLEAN DEFAULT TRUE NOT NULL,
    first_photo VARCHAR(191),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);