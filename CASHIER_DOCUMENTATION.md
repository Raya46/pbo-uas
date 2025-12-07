# 💰 FITUR CASHIER - DOKUMENTASI

## 📋 Overview

Dashboard Kasir All-in-One untuk operasional sehari-hari restoran.

---

## ✨ Fitur Utama

### 1. 🪑 **Dashboard Kasir - Status Meja**
Melihat status semua meja secara real-time:
- ✅ **Available** - Meja kosong, siap digunakan
- 🔴 **Occupied** - Meja sedang digunakan customer

### 2. 📋 **Order List - Pesanan Masuk**
Melihat semua pesanan yang masuk dari pelanggan:
- Order ID
- Nama Customer
- Nomor Meja (atau "Take Away")
- Total Pembayaran
- Status Pesanan
- Waktu Order

**Filter otomatis:** Hanya menampilkan pesanan hari ini

### 3. ✏️ **Update Status Pesanan**
Mengubah status pesanan sesuai workflow:

```
⏳ Pending → 🍳 Cooking → ✅ Ready → 🍽️ Served → 💰 Paid
```

**Status yang tersedia:**
- **Pending** - Pesanan baru masuk
- **Cooking** - Sedang dimasak di dapur
- **Ready** - Makanan sudah siap
- **Served** - Sudah disajikan ke customer
- **Paid** - Sudah dibayar (selesai)
- **Cancelled** - Dibatalkan

### 4. 💰 **Pembayaran & Cetak Struk**

**Fitur:**
- Lihat detail pesanan lengkap
- Generate struk pembayaran (simulasi)
- Konfirmasi pembayaran
- Auto update status ke "Paid"
- **Auto release meja** (status jadi "Available")

---

## 🖥️ Layout Dashboard

```
┌─────────────────────────────────────────────────────────┐
│  🏪 DASHBOARD KASIR - OPERASIONAL    Kasir: Siti Kasir │
├─────────────────────────────────────────────────────────┤
│  📊 Total Orders: 15  │  ⏳ Pesanan Pending: 3          │
├──────────────────┬──────────────────────────────────────┤
│  🪑 STATUS MEJA  │  📋 DAFTAR PESANAN MASUK             │
│                  │                                       │
│  Meja 01  ✅     │  #123  Budi    01  Rp60k  ⏳ 10:30  │
│  Meja 02  🔴     │  #124  Ani     02  Rp45k  🍳 10:35  │
│  Meja 03  ✅     │  #125  Citra  T/A  Rp30k  ✅ 10:40  │
│  Meja 04  ✅     │                                       │
│  VIP-1    🔴     │  [👁️ Detail] [✏️ Update] [💰 Bayar] │
└──────────────────┴──────────────────────────────────────┘
│              [🔄 Refresh Data]  [🚪 Logout]              │
└─────────────────────────────────────────────────────────┘
```

---

## 🔄 Workflow Operasional

### Skenario: Pesanan Baru Masuk

```
1. Customer order via sistem customer
   ↓
2. Order muncul di Dashboard Kasir (status: Pending)
   ↓
3. Kasir lihat detail pesanan (klik "👁️ Lihat Detail")
   ↓
4. Kasir update status → "Cooking" (klik "✏️ Update Status")
   ↓
5. Dapur menerima notifikasi (sistem dapur)
   ↓
6. Setelah selesai masak → Update status "Ready"
   ↓
7. Pelayan sajikan → Update status "Served"
   ↓
8. Customer minta bayar → Kasir proses pembayaran
   ↓
9. Klik "💰 Pembayaran" → Lihat struk → Konfirmasi
   ↓
10. Status "Paid" + Meja "Available"
```

---

## 💳 Format Struk Pembayaran

```
═══════════════════════════════════
       RESTORAN NUSANTARA
     Jl. Merdeka No. 123
      Telp: (021) 1234567
═══════════════════════════════════

No. Order : #123
Tanggal   : 07/12/2025 10:30:45
Kasir     : Siti Kasir
Customer  : Budi Santoso
Meja      : 01

───────────────────────────────────

Nasi Goreng Spesial x2
  @Rp25,000 = Rp50,000
Es Teh Manis        x2
  @Rp5,000 = Rp10,000

───────────────────────────────────
TOTAL:          Rp 60,000
═══════════════════════════════════

     Terima Kasih Atas
       Kunjungan Anda!

═══════════════════════════════════
```

---

## 📁 File yang Digunakan

### ✅ **File AKTIF:**

1. **CashierMainFrame.java** - Dashboard utama (ALL-IN-ONE)
   - Status meja
   - Order list
   - Update status
   - Pembayaran

### ❌ **File TIDAK DIGUNAKAN** (bisa dihapus):

1. **CashierNewOrderFrame.java** - Tidak dipakai (order dari customer)
2. **CashierViewMenuFrame.java** - Tidak dipakai (hanya referensi)
3. **CashierViewOrdersFrame.java** - Sudah digabung ke dashboard

---

## 🚀 Cara Menggunakan

### 1. Login
```
Username: kasir
Password: kasir123
```

### 2. Monitor Dashboard
- Panel kiri: Status meja
- Panel kanan: Daftar pesanan
- Header: Statistik hari ini

### 3. Lihat Detail Order
1. Pilih order dari tabel
2. Klik "👁️ Lihat Detail"
3. Dialog muncul dengan detail lengkap

### 4. Update Status
1. Pilih order
2. Klik "✏️ Update Status"
3. Pilih status baru
4. Konfirmasi

### 5. Proses Pembayaran
1. Pilih order yang akan dibayar
2. Klik "💰 Pembayaran"
3. Review struk
4. Klik OK untuk konfirmasi
5. Selesai!

### 6. Refresh Data
- Klik "🔄 Refresh Data"
- Atau otomatis refresh setelah update/pembayaran

---

## 💾 Database Integration

### Tables Used:
1. **orders** - Data pesanan
2. **order_details** - Detail items
3. **customers** - Data customer
4. **restaurant_tables** - Status meja
5. **menu_items** - Data menu
6. **users** - Data kasir

### Key Queries:

**Load Orders Hari Ini:**
```sql
SELECT o.order_id, c.customer_name, rt.table_number, 
       o.total_amount, o.status, o.order_date
FROM orders o
LEFT JOIN customers c ON o.customer_id = c.customer_id
LEFT JOIN restaurant_tables rt ON o.table_id = rt.table_id
WHERE DATE(o.order_date) = CURDATE()
ORDER BY o.order_date DESC
```

**Update Status:**
```sql
UPDATE orders 
SET status = ? 
WHERE order_id = ?
```

**Process Payment:**
```sql
-- Update order
UPDATE orders SET status = 'paid' WHERE order_id = ?

-- Release table
UPDATE restaurant_tables rt
JOIN orders o ON rt.table_id = o.table_id
SET rt.status = 'available'
WHERE o.order_id = ?
```

---

## ✅ Testing Checklist

- [ ] Login sebagai kasir
- [ ] Dashboard tampil dengan benar
- [ ] Status meja terload
- [ ] Daftar pesanan terload
- [ ] Statistik tampil
- [ ] Lihat detail order
- [ ] Update status order
- [ ] Proses pembayaran
- [ ] Struk generate dengan benar
- [ ] Meja auto-release setelah bayar
- [ ] Refresh data
- [ ] Logout

---

## 🎨 UI Features

### Color Coding:
- **Header:** Blue (#3498db)
- **Stats - Total:** Green (#2ecc71)
- **Stats - Pending:** Orange (#f39c12)
- **Tables Panel:** Blue border
- **Orders Panel:** Green border
- **Buttons:** Color-coded by function

### Icons:
- 🏪 Dashboard
- 🪑 Meja
- 📋 Pesanan
- 👁️ Detail
- ✏️ Update
- 💰 Pembayaran
- 🔄 Refresh
- 🚪 Logout

---

## 📝 Notes

### Keunggulan:
✅ **All-in-One** - Semua fitur dalam 1 window
✅ **Real-time** - Status update langsung
✅ **Auto-release** - Meja otomatis available
✅ **Professional Receipt** - Struk pembayaran rapi
✅ **Simple Workflow** - View → Update → Pay

### Fokus:
- ✅ Monitoring (status meja & pesanan)
- ✅ Update (ubah status pesanan)
- ✅ Payment (proses pembayaran & struk)
- ✅ Efficiency (tidak perlu banyak klik)

---

**Last Updated:** 2025-12-07  
**Version:** 4.0 - Final  
**Status:** ✅ PRODUCTION READY
