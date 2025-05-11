CREATE TABLE logistics (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    provider VARCHAR(191) NOT NULL,
    method VARCHAR(191) NOT NULL,
    cvs_code VARCHAR(191),
    name VARCHAR(191),
    setting TEXT,
    status BOOLEAN DEFAULT TRUE NOT NULL,
    shipping_cost INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);