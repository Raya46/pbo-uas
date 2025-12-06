# 💰 FITUR CASHIER/KASIR - DOKUMENTASI LENGKAP

## 📋 Daftar Isi
1. [Overview](#overview)
2. [Fitur-Fitur](#fitur-fitur)
3. [File yang Dibuat](#file-yang-dibuat)
4. [Cara Menggunakan](#cara-menggunakan)
5. [Screenshot Workflow](#screenshot-workflow)
6. [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

Sistem Kasir adalah modul untuk mengelola transaksi penjualan di restoran. Kasir dapat:
- Membuat pesanan baru (POS System)
- Melihat daftar menu
- Mengelola pesanan aktif
- Memproses pembayaran
- Update status pesanan

---

## ✨ Fitur-Fitur

### 1. **Dashboard Kasir** (`CashierMainFrame.java`)

**Tampilan Utama:**
- Header dengan nama kasir yang login
- 4 Menu utama:
  - 📝 **Buat Pesanan Baru** - POS System
  - 📋 **Lihat Pesanan Aktif** - Monitor orders
  - 📊 **Riwayat Transaksi** - History (placeholder)
  - 🍽️ **Lihat Daftar Menu** - View menu items
- Tombol Logout

**Fitur:**
- ✅ Modern UI dengan color coding
- ✅ Hover effects pada tombol
- ✅ Navigation ke semua modul
- ✅ Logout confirmation

---

### 2. **Buat Pesanan Baru** (`CashierNewOrderFrame.java`)

**Layout:**
```
┌─────────────────────────────────────────────────────────┐
│  📝 BUAT PESANAN BARU                                   │
├──────────────────────┬──────────────────────────────────┤
│  DAFTAR MENU         │  KERANJANG BELANJA               │
│                      │                                  │
│  [Table: Menu Items] │  Nama Customer: [________]       │
│  - ID                │  No. HP:        [________]       │
│  - Nama              │                                  │
│  - Kategori          │  [Table: Cart Items]             │
│  - Harga             │  - Menu                          │
│  - Stok              │  - Harga                         │
│                      │  - Qty                           │
│  Jumlah: [1] [+Add]  │  - Subtotal                      │
│                      │                                  │
│                      │  TOTAL: Rp 0                     │
│                      │  [Hapus] [Kosongkan]             │
├──────────────────────┴──────────────────────────────────┤
│         [💳 CHECKOUT & BAYAR]  [← Kembali]              │
└─────────────────────────────────────────────────────────┘
```

**Fitur:**
- ✅ **Split View** - Menu list di kiri, Cart di kanan
- ✅ **Menu Selection** - Pilih dari daftar menu yang tersedia
- ✅ **Quantity Selector** - Spinner untuk pilih jumlah
- ✅ **Add to Cart** - Tambah item ke keranjang
- ✅ **Stock Validation** - Cek stok sebelum add
- ✅ **Cart Management**:
  - Lihat items di cart
  - Hapus item tertentu
  - Kosongkan semua cart
  - Auto-calculate total
- ✅ **Customer Info** - Input nama dan nomor HP
- ✅ **Checkout Process**:
  - Validasi cart tidak kosong
  - Validasi customer info
  - Konfirmasi pembayaran
  - Simpan ke database (orders + order_details)
  - Auto-create customer jika belum ada
  - Success notification dengan Order ID

**Workflow:**
1. Pilih menu dari tabel kiri
2. Set quantity
3. Klik "Tambah ke Keranjang"
4. Ulangi untuk item lain
5. Isi nama dan HP customer
6. Klik "CHECKOUT & BAYAR"
7. Konfirmasi
8. Selesai!

---

### 3. **Lihat Daftar Menu** (`CashierViewMenuFrame.java`)

**Tampilan:**
```
┌─────────────────────────────────────────────────────────┐
│  🍽️ DAFTAR MENU RESTORAN                                │
├─────────────────────────────────────────────────────────┤
│  [Table: All Menu Items]                                │
│  ID | Nama Menu | Kategori | Harga | Stok | Status     │
│  ───────────────────────────────────────────────────    │
│  1  | Nasi Goreng | Makanan | Rp 25,000 | 50 | Tersedia│
│  2  | Es Teh      | Minuman | Rp 5,000  | 100| Tersedia│
│  ...                                                     │
├─────────────────────────────────────────────────────────┤
│         [🔄 Refresh]  [← Kembali]                        │
└─────────────────────────────────────────────────────────┘
```

**Fitur:**
- ✅ Tampilkan semua menu items
- ✅ Info lengkap: ID, Nama, Kategori, Harga, Stok, Status
- ✅ Refresh data
- ✅ Read-only table
- ✅ Auto-format harga (Rp format)
- ✅ Status indicator (Tersedia/Habis)

---

### 4. **Lihat Pesanan Aktif** (`CashierViewOrdersFrame.java`)

**Tampilan:**
```
┌─────────────────────────────────────────────────────────┐
│  📋 PESANAN AKTIF                                        │
├─────────────────────────────────────────────────────────┤
│  [Table: Active Orders]                                 │
│  Order ID | Customer | Phone | Total | Tanggal | Status │
│  ──────────────────────────────────────────────────────│
│  1 | John Doe | 0812... | Rp 50,000 | 2024-12-07 | PENDING│
│  2 | Jane     | 0813... | Rp 35,000 | 2024-12-07 | COOKING│
│  ...                                                     │
├─────────────────────────────────────────────────────────┤
│  [🔍 Detail] [✏️ Update Status] [🔄 Refresh] [← Kembali]│
└─────────────────────────────────────────────────────────┘
```

**Fitur:**
- ✅ **View Active Orders** - Hanya tampilkan order dengan status:
  - pending
  - cooking
  - served
- ✅ **Order Details** - Klik untuk lihat detail items
- ✅ **Update Status** - Ubah status order:
  - pending → cooking
  - cooking → served
  - served → paid
  - any → cancelled
- ✅ **Refresh** - Reload data terbaru
- ✅ **Auto-format** - Harga dan tanggal

**Detail Order Dialog:**
```
=== DETAIL PESANAN ===

Order ID: 1
Customer: John Doe
Phone: 081234567890
Status: PENDING

=== ITEMS ===
• Nasi Goreng Spesial x2 @ Rp 25,000 = Rp 50,000
• Es Teh Manis x2 @ Rp 5,000 = Rp 10,000

TOTAL: Rp 60,000
```

---

## 📁 File yang Dibuat

### **Java Files:**

1. **`CashierMainFrame.java`** (176 lines)
   - Dashboard utama kasir
   - Navigation hub

2. **`CashierNewOrderFrame.java`** (419 lines)
   - POS System lengkap
   - Cart management
   - Checkout & payment

3. **`CashierViewMenuFrame.java`** (106 lines)
   - View all menu items
   - Stock information

4. **`CashierViewOrdersFrame.java`** (247 lines)
   - View active orders
   - Order details
   - Status management

### **Modified Files:**

5. **`LoginForm.java`**
   - Updated line 78-79
   - Redirect kasir to `CashierMainFrame`

---

## 🚀 Cara Menggunakan

### **Login sebagai Kasir:**

1. **Jalankan aplikasi** (`Main.java`)
2. **Login** dengan kredensial kasir:
   ```
   Username: kasir1
   Password: kasir123
   ```
3. **Dashboard Kasir** akan terbuka

---

### **Workflow: Membuat Pesanan**

**Skenario:** Customer pesan 2 Nasi Goreng + 2 Es Teh

1. **Klik** "📝 Buat Pesanan Baru"

2. **Pilih menu** dari tabel kiri:
   - Klik row "Nasi Goreng Spesial"
   - Set quantity: `2`
   - Klik "➕ Tambah ke Keranjang"

3. **Tambah item lain**:
   - Klik row "Es Teh Manis"
   - Set quantity: `2`
   - Klik "➕ Tambah ke Keranjang"

4. **Isi customer info**:
   - Nama: `John Doe`
   - No. HP: `081234567890`

5. **Checkout**:
   - Klik "💳 CHECKOUT & BAYAR"
   - Konfirmasi pembayaran
   - Selesai!

6. **Result:**
   - Order tersimpan di database
   - Order ID ditampilkan
   - Cart dikosongkan

---

### **Workflow: Lihat & Update Status Order**

1. **Klik** "📋 Lihat Pesanan Aktif"

2. **Lihat detail**:
   - Pilih order dari tabel
   - Klik "🔍 Lihat Detail"
   - Dialog muncul dengan detail items

3. **Update status**:
   - Pilih order
   - Klik "✏️ Update Status"
   - Pilih status baru (pending/cooking/served/paid/cancelled)
   - Konfirmasi

4. **Refresh**:
   - Klik "🔄 Refresh" untuk reload data

---

## 🎨 UI/UX Features

### **Color Scheme:**
- **Header:** Dark Blue (#2C3E50)
- **Buat Order:** Blue (#3498DB)
- **View Orders:** Green (#2ECC71)
- **History:** Purple (#9B59B6)
- **View Menu:** Yellow (#F1C40F)
- **Checkout:** Green (#2ECC71)
- **Logout:** Red (#E74C3C)

### **Interactive Elements:**
- ✅ Hover effects pada semua tombol
- ✅ Hand cursor pada clickable items
- ✅ Color feedback untuk actions
- ✅ Confirmation dialogs
- ✅ Success/Error messages

### **User Experience:**
- ✅ Intuitive navigation
- ✅ Clear labels dan icons
- ✅ Validation messages
- ✅ Auto-format currency
- ✅ Responsive layout
- ✅ Back navigation

---

## 🔧 Technical Details

### **Database Operations:**

**Create Order:**
```java
1. Insert into customers (if new)
2. Insert into orders (get order_id)
3. Insert into order_details (for each cart item)
4. Commit transaction
```

**View Orders:**
```sql
SELECT o.*, c.full_name, c.phone 
FROM orders o 
JOIN customers c ON o.customer_id = c.customer_id 
WHERE o.status IN ('pending', 'cooking', 'served')
ORDER BY o.order_date DESC
```

**Update Status:**
```sql
UPDATE orders 
SET status = ? 
WHERE id = ?
```

---

## 🐛 Troubleshooting

### **Error: "ClassNotFoundException: CashierMainFrame"**

**Penyebab:** File belum di-compile

**Solusi:**
```cmd
javac -cp ".;lib\mysql-connector-j-9.5.0.jar" src\*.java
```

---

### **Error: "Keranjang masih kosong"**

**Penyebab:** Belum add item ke cart

**Solusi:** Pilih menu dan klik "Tambah ke Keranjang"

---

### **Error: "Stok tidak cukup"**

**Penyebab:** Quantity melebihi stok tersedia

**Solusi:** Kurangi quantity atau pilih menu lain

---

### **Error: "Nama customer harus diisi"**

**Penyebab:** Field nama kosong

**Solusi:** Isi nama customer sebelum checkout

---

### **Error saat save order**

**Penyebab:** Database connection issue

**Solusi:**
1. Cek XAMPP MySQL running
2. Cek database `db_restoran_final` exists
3. Cek tabel `customers`, `orders`, `order_details` exists

---

## 📊 Database Schema (Reminder)

### **Tabel yang Digunakan:**

**customers:**
- customer_id (PK)
- username
- password
- full_name
- phone

**orders:**
- id (PK)
- customer_id (FK)
- total_price
- order_date
- status
- payment_method

**order_details:**
- id (PK)
- order_id (FK)
- menu_id (FK)
- quantity
- price

**menu_items:**
- menu_id (PK)
- name
- category
- price
- stock

---

## 🎯 Future Enhancements

Fitur yang bisa ditambahkan:

1. ✨ **Print Receipt** - Cetak struk pembayaran
2. ✨ **Order History** - Riwayat transaksi lengkap
3. ✨ **Sales Report** - Laporan penjualan harian/bulanan
4. ✨ **Discount System** - Sistem diskon/promo
5. ✨ **Table Management** - Manajemen nomor meja
6. ✨ **Split Bill** - Pembayaran terpisah
7. ✨ **Cash Drawer** - Manajemen kas
8. ✨ **Barcode Scanner** - Scan barcode menu
9. ✨ **Kitchen Display** - Tampilan untuk dapur
10. ✨ **Customer Display** - Layar untuk customer

---

## 📞 Support

Jika ada pertanyaan atau bug:
1. Cek file `DATABASE_DOCUMENTATION.md`
2. Cek file `SETUP_ANTIGRAVITY.md`
3. Cek console untuk error messages

---

## ✅ Checklist Testing

Sebelum deploy, test fitur-fitur berikut:

- [ ] Login sebagai kasir
- [ ] Dashboard tampil dengan benar
- [ ] Buat pesanan baru
  - [ ] Add item ke cart
  - [ ] Remove item dari cart
  - [ ] Clear cart
  - [ ] Checkout berhasil
  - [ ] Order tersimpan di database
- [ ] View menu
  - [ ] Semua menu tampil
  - [ ] Harga format benar
  - [ ] Status stok benar
- [ ] View orders
  - [ ] Active orders tampil
  - [ ] Detail order benar
  - [ ] Update status berhasil
- [ ] Logout berhasil

---

**Sistem Kasir siap digunakan!** 🎉

**Login Credentials:**
```
Username: kasir1
Password: kasir123
```

Selamat mencoba! 🚀
