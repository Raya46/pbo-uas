-- ============================================
-- DATABASE SETUP: SISTEM MANAJEMEN RESTORAN
-- Project: PBO-UAS
-- ============================================
-- 
-- INSTRUKSI IMPORT:
-- 1. Buka phpMyAdmin (http://localhost/phpmyadmin)
-- 2. Klik tab "SQL"
-- 3. Copy-paste seluruh isi file ini
-- 4. Klik "Go" atau "Execute"
-- 
-- ATAU
-- 
-- 1. Klik tab "Import"
-- 2. Choose file: setup_database_complete.sql
-- 3. Klik "Import"
-- ============================================

-- Hapus database jika sudah ada (HATI-HATI: ini akan menghapus semua data!)
-- DROP DATABASE IF EXISTS db_restoran_final;

-- Buat database baru
CREATE DATABASE IF NOT EXISTS db_restoran_final;
USE db_restoran_final;

-- ============================================
-- TABEL 1: users
-- Untuk login Admin dan Kasir
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('admin', 'kasir') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 2: customers
-- Untuk login Customer (pelanggan)
-- ============================================
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 3: menu_items
-- Daftar menu makanan/minuman
-- ============================================
CREATE TABLE IF NOT EXISTS menu_items (
    menu_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    description TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_category (category),
    INDEX idx_available (is_available),
    CHECK (price >= 0),
    CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 4: orders
-- Header transaksi/pesanan
-- ============================================
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'cooking', 'served', 'paid', 'cancelled') DEFAULT 'pending',
    payment_method ENUM('cash', 'card', 'e-wallet') DEFAULT 'cash',
    notes TEXT,
    
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    INDEX idx_customer (customer_id),
    INDEX idx_status (status),
    INDEX idx_date (order_date),
    CHECK (total_price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 5: order_details
-- Detail item dalam setiap pesanan
-- ============================================
CREATE TABLE IF NOT EXISTS order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (quantity * price) STORED,
    
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu_items(menu_id) ON DELETE RESTRICT,
    INDEX idx_order (order_id),
    INDEX idx_menu (menu_id),
    CHECK (quantity > 0),
    CHECK (price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- INSERT DATA DEFAULT
-- ============================================

-- Insert Admin dan Kasir Default
INSERT INTO users (username, password, full_name, role) VALUES
    ('admin', 'admin123', 'Administrator', 'admin'),
    ('kasir1', 'kasir123', 'Kasir Satu', 'kasir'),
    ('kasir2', 'kasir123', 'Kasir Dua', 'kasir')
ON DUPLICATE KEY UPDATE username=username;

-- Insert Customer Default untuk Testing
INSERT INTO customers (username, password, full_name, phone) VALUES
    ('customer1', 'cust123', 'John Doe', '081234567890'),
    ('customer2', 'cust123', 'Jane Smith', '081234567891'),
    ('budi', 'budi123', 'Budi Santoso', '081234567892')
ON DUPLICATE KEY UPDATE username=username;

-- Insert Menu Items (Makanan)
INSERT INTO menu_items (name, category, price, stock, description, is_available) VALUES
    -- Makanan
    ('Nasi Goreng Spesial', 'Makanan', 25000.00, 50, 'Nasi goreng dengan telur, ayam, dan sayuran', TRUE),
    ('Mie Goreng', 'Makanan', 20000.00, 50, 'Mie goreng dengan sayuran dan telur', TRUE),
    ('Ayam Bakar', 'Makanan', 30000.00, 30, 'Ayam bakar dengan bumbu kecap', TRUE),
    ('Sate Ayam', 'Makanan', 28000.00, 40, 'Sate ayam dengan bumbu kacang (10 tusuk)', TRUE),
    ('Gado-Gado', 'Makanan', 18000.00, 35, 'Sayuran dengan bumbu kacang', TRUE),
    ('Soto Ayam', 'Makanan', 22000.00, 40, 'Soto ayam dengan nasi', TRUE),
    ('Rendang', 'Makanan', 35000.00, 25, 'Rendang daging sapi dengan nasi', TRUE),
    ('Nasi Uduk', 'Makanan', 15000.00, 60, 'Nasi uduk dengan lauk pauk', TRUE),
    
    -- Minuman
    ('Es Teh Manis', 'Minuman', 5000.00, 100, 'Teh manis dingin', TRUE),
    ('Es Jeruk', 'Minuman', 7000.00, 100, 'Jeruk peras dengan es', TRUE),
    ('Jus Alpukat', 'Minuman', 12000.00, 50, 'Jus alpukat segar', TRUE),
    ('Kopi Hitam', 'Minuman', 8000.00, 80, 'Kopi hitam panas', TRUE),
    ('Cappuccino', 'Minuman', 15000.00, 60, 'Kopi cappuccino', TRUE),
    ('Teh Tarik', 'Minuman', 10000.00, 70, 'Teh tarik panas', TRUE),
    ('Air Mineral', 'Minuman', 3000.00, 200, 'Air mineral kemasan', TRUE),
    
    -- Snack/Dessert
    ('Pisang Goreng', 'Snack', 10000.00, 40, 'Pisang goreng crispy (5 pcs)', TRUE),
    ('Kentang Goreng', 'Snack', 12000.00, 50, 'Kentang goreng dengan saus', TRUE),
    ('Es Krim', 'Snack', 8000.00, 60, 'Es krim vanilla/coklat/strawberry', TRUE),
    ('Kue Lapis', 'Snack', 6000.00, 45, 'Kue lapis tradisional', TRUE)
ON DUPLICATE KEY UPDATE name=name;

-- Insert Sample Orders (untuk testing)
INSERT INTO orders (customer_id, total_price, order_date, status, payment_method) VALUES
    (1, 52000.00, '2024-12-01 10:30:00', 'paid', 'cash'),
    (1, 35000.00, '2024-12-02 14:15:00', 'paid', 'e-wallet'),
    (2, 68000.00, '2024-12-03 12:00:00', 'paid', 'card'),
    (3, 45000.00, '2024-12-05 18:30:00', 'pending', 'cash')
ON DUPLICATE KEY UPDATE id=id;

-- Insert Sample Order Details
INSERT INTO order_details (order_id, menu_id, quantity, price) VALUES
    -- Order 1 (customer1 - Nasi Goreng + Es Teh)
    (1, 1, 2, 25000.00),  -- 2x Nasi Goreng Spesial
    (1, 9, 2, 5000.00),   -- 2x Es Teh Manis
    
    -- Order 2 (customer1 - Rendang)
    (2, 7, 1, 35000.00),  -- 1x Rendang
    
    -- Order 3 (customer2 - Ayam Bakar + Sate + Jus)
    (3, 3, 1, 30000.00),  -- 1x Ayam Bakar
    (3, 4, 1, 28000.00),  -- 1x Sate Ayam
    (3, 11, 1, 12000.00), -- 1x Jus Alpukat
    
    -- Order 4 (budi - Mie Goreng + Snack)
    (4, 2, 1, 20000.00),  -- 1x Mie Goreng
    (4, 17, 1, 10000.00), -- 1x Pisang Goreng
    (4, 9, 1, 5000.00)    -- 1x Es Teh Manis
ON DUPLICATE KEY UPDATE id=id;

-- ============================================
-- VIEWS (Optional - untuk kemudahan query)
-- ============================================

-- View untuk melihat detail order lengkap
CREATE OR REPLACE VIEW vw_order_details AS
SELECT 
    o.id AS order_id,
    o.order_date,
    o.status,
    o.total_price,
    c.full_name AS customer_name,
    c.phone AS customer_phone,
    od.id AS detail_id,
    m.name AS menu_name,
    m.category,
    od.quantity,
    od.price,
    od.subtotal
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_details od ON o.id = od.order_id
JOIN menu_items m ON od.menu_id = m.menu_id;

-- View untuk laporan penjualan per menu
CREATE OR REPLACE VIEW vw_sales_by_menu AS
SELECT 
    m.menu_id,
    m.name AS menu_name,
    m.category,
    COUNT(od.id) AS total_orders,
    SUM(od.quantity) AS total_quantity_sold,
    SUM(od.subtotal) AS total_revenue
FROM menu_items m
LEFT JOIN order_details od ON m.menu_id = od.menu_id
LEFT JOIN orders o ON od.order_id = o.id AND o.status = 'paid'
GROUP BY m.menu_id, m.name, m.category
ORDER BY total_revenue DESC;

-- ============================================
-- STORED PROCEDURES (Optional)
-- ============================================

DELIMITER //

-- Procedure untuk membuat order baru dengan detail
CREATE PROCEDURE IF NOT EXISTS sp_create_order(
    IN p_customer_id INT,
    IN p_total_price DECIMAL(10,2),
    IN p_payment_method VARCHAR(20)
)
BEGIN
    INSERT INTO orders (customer_id, total_price, payment_method)
    VALUES (p_customer_id, p_total_price, p_payment_method);
    
    SELECT LAST_INSERT_ID() AS order_id;
END //

-- Procedure untuk update stock setelah order
CREATE PROCEDURE IF NOT EXISTS sp_update_stock_after_order(
    IN p_menu_id INT,
    IN p_quantity INT
)
BEGIN
    UPDATE menu_items 
    SET stock = stock - p_quantity
    WHERE menu_id = p_menu_id;
    
    -- Set unavailable jika stock habis
    UPDATE menu_items 
    SET is_available = FALSE
    WHERE menu_id = p_menu_id AND stock <= 0;
END //

DELIMITER ;

-- ============================================
-- TRIGGERS (Optional - untuk auto-update)
-- ============================================

DELIMITER //

-- Trigger untuk update stock setelah order detail dibuat
CREATE TRIGGER IF NOT EXISTS trg_after_order_detail_insert
AFTER INSERT ON order_details
FOR EACH ROW
BEGIN
    UPDATE menu_items 
    SET stock = stock - NEW.quantity
    WHERE menu_id = NEW.menu_id;
END //

DELIMITER ;

-- ============================================
-- VERIFIKASI DATA
-- ============================================

-- Tampilkan ringkasan data yang sudah diinsert
SELECT '=== VERIFIKASI DATABASE ===' AS info;

SELECT 'Users' AS table_name, COUNT(*) AS total_records FROM users
UNION ALL
SELECT 'Customers', COUNT(*) FROM customers
UNION ALL
SELECT 'Menu Items', COUNT(*) FROM menu_items
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Order Details', COUNT(*) FROM order_details;

-- Tampilkan data users
SELECT '=== DATA USERS ===' AS info;
SELECT user_id, username, full_name, role FROM users;

-- Tampilkan data customers
SELECT '=== DATA CUSTOMERS ===' AS info;
SELECT customer_id, username, full_name, phone FROM customers;

-- Tampilkan menu items per kategori
SELECT '=== MENU ITEMS BY CATEGORY ===' AS info;
SELECT category, COUNT(*) AS total_items, 
       MIN(price) AS min_price, 
       MAX(price) AS max_price
FROM menu_items
GROUP BY category;

-- ============================================
-- INFORMASI KREDENSIAL
-- ============================================

SELECT '=== LOGIN CREDENTIALS ===' AS info;
SELECT 
    'ADMIN' AS user_type,
    'admin' AS username,
    'admin123' AS password
UNION ALL
SELECT 
    'KASIR',
    'kasir1',
    'kasir123'
UNION ALL
SELECT 
    'CUSTOMER',
    'customer1',
    'cust123';

-- ============================================
-- SELESAI
-- ============================================

SELECT '✅ DATABASE SETUP COMPLETED!' AS status;
SELECT 'Database: db_restoran_final' AS info;
SELECT 'Ready to use!' AS message;
