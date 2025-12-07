-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Waktu pembuatan: 27 Nov 2025 pada 08.02
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_restoran_final`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`) VALUES
(1, 'Makanan Berat'),
(2, 'Minuman'),
(3, 'Cemilan');

-- --------------------------------------------------------

--
-- Struktur dari tabel `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `customers`
--

INSERT INTO `customers` (`customer_id`, `customer_name`, `phone_number`, `email`, `password`) VALUES
(1, 'Guest Customer', '-', NULL, NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `menu_items`
--

CREATE TABLE `menu_items` (
  `menu_id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) DEFAULT 0,
  `image_path` varchar(255) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `menu_items`
--

INSERT INTO `menu_items` (`menu_id`, `category_id`, `name`, `description`, `price`, `stock`, `image_path`, `is_available`) VALUES
(1, 1, 'Nasi Goreng Spesial', 'Nasi goreng dengan telur dan ayam suwir', 25000.00, 50, 'nasigoreng.jpg', 1),
(2, 1, 'Sate Ayam Madura', 'Sate ayam 10 tusuk dengan bumbu kacang', 30000.00, 40, 'sate.jpg', 1),
(3, 2, 'Es Teh Manis', 'Teh manis dingin segar', 5000.00, 100, 'esteh.jpg', 1),
(4, 2, 'Kopi Susu Gula Aren', 'Kopi kekinian', 18000.00, 50, 'kopi.jpg', 1),
(5, 3, 'Kentang Goreng', 'French fries original', 12000.00, 30, 'kentang.jpg', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `table_id` int(11) DEFAULT NULL,
  `order_date` datetime DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `status` enum('pending','cooking','ready','served','paid','cancelled') DEFAULT 'pending',
  `payment_method` enum('cash','qris','debit') DEFAULT 'cash'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `user_id`, `table_id`, `order_date`, `total_amount`, `status`, `payment_method`) VALUES
(1, 1, 2, 1, '2025-11-25 18:47:44', 55000.00, 'paid', 'cash');

-- --------------------------------------------------------

--
-- Struktur dari tabel `order_details`
--

CREATE TABLE `order_details` (
  `detail_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `note` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `order_details`
--

INSERT INTO `order_details` (`detail_id`, `order_id`, `menu_id`, `quantity`, `subtotal`, `note`) VALUES
(1, 1, 1, 1, 25000.00, 'Pedas banget'),
(2, 1, 2, 1, 30000.00, 'Bumbu pisah');

-- --------------------------------------------------------

--
-- Struktur dari tabel `restaurant_tables`
--

CREATE TABLE `restaurant_tables` (
  `table_id` int(11) NOT NULL,
  `table_number` varchar(10) NOT NULL,
  `status` enum('available','occupied') DEFAULT 'available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `restaurant_tables`
--

INSERT INTO `restaurant_tables` (`table_id`, `table_number`, `status`) VALUES
(1, '01', 'available'),
(2, '02', 'available'),
(3, '03', 'available'),
(4, '04', 'available'),
(5, 'VIP-1', 'available');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('admin','kasir','dapur') NOT NULL DEFAULT 'kasir',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `full_name`, `role`, `created_at`) VALUES
(1, 'admin', 'admin123', 'Manager Resto', 'admin', '2025-11-25 11:47:44'),
(2, 'kasir', 'kasir123', 'Siti Kasir', 'kasir', '2025-11-25 11:47:44'),
(3, 'chef', 'chef123', 'Juna Chef', 'dapur', '2025-11-25 11:47:44');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indeks untuk tabel `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indeks untuk tabel `menu_items`
--
ALTER TABLE `menu_items`
  ADD PRIMARY KEY (`menu_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indeks untuk tabel `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `table_id` (`table_id`);

--
-- Indeks untuk tabel `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indeks untuk tabel `restaurant_tables`
--
ALTER TABLE `restaurant_tables`
  ADD PRIMARY KEY (`table_id`),
  ADD UNIQUE KEY `table_number` (`table_number`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `menu_items`
--
ALTER TABLE `menu_items`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `order_details`
--
ALTER TABLE `order_details`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `restaurant_tables`
--
ALTER TABLE `restaurant_tables`
  MODIFY `table_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `menu_items`
--
ALTER TABLE `menu_items`
  ADD CONSTRAINT `menu_items_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`table_id`) REFERENCES `restaurant_tables` (`table_id`);

--
-- Ketidakleluasaan untuk tabel `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu_items` (`menu_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
