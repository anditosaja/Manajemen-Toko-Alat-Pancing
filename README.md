# Manajemen-Toko-Alat-Pancing
Sistem manajemen toko alat pancing berbasis Java Swing yang menghubungkan aplikasi desktop dengan database MySQL. Sistem ini dirancang untuk mengelola data master (produk & kategori) dan proses transaksi penjualan secara efisien.

## Arsitektur Sistem
Aplikasi ini menggunakan pola desain DAO (Data Access Object) untuk memisahkan logika database dari antarmuka pengguna:
`Java GUI (Swing) -> DAO Layer -> Database (MySQL)`

## Teknologi yang digunakan
1. Java (Swing GUI)
2. MySQL (Database)
3. JDBC (Java Database Connectivity)
4. Git & GitHub
5. NetBeans/IntelliJ/Eclipse (IDE)

## Instalasi
1. Pastikan telah menginstal JDK (Java Development Kit) dan MySQL Server (XAMPP/WAMP).
2. Buat database di MySQL  misalnya dengan nama toko_alat_pancing.
3. Import skema database (tabel kategori, produk, transaksi, dan detail_transaksi).
4. Tambahkan library JDBC MySQL (mysql-connector-j.jar) ke dalam folder lib project masing-masing.
5. Jalankan TokoAlatPancingApp.java untuk memulai aplikasi.

## Struktur Project
1. Model (`com.tokopancing.model`): Representasi objek (Produk, Kategori, Transaksi, Keranjang).
2. DAO (`com.tokopancing.dao`): Logika akses database (CRUD) untuk masing-masing model.
3. GUI (`com.tokopancing.gui`): Antarmuka desktop menggunakan Java Swing.
4. Database (`com.tokopancing.database`): Pengelolaan koneksi (Singleton Pattern).

## Fitur Utama

| Fitur | Deskripsi |
| :---  | :--- |
| Master Produk  | Tambah, update, hapus, dan cari produk |
| Master Kategori | Kelola pengelompokan produk |
| Keranjang Belanja | Add to cart, hapus item, dan hitung subtotal |
| Transaksi  | Checkout transaksi dengan update stok otomatis|
| Monitoring Stok  | Validasi stok tersedia saat transaksi (`StokTidakCukupException`) |

## Endpoint & Transaksi Database
Transaksi dilakukan secara transaksional menggunakan batch process di `TransaksiDAO` untuk memastikan data `transaksi` dan `detail_transaksi` konsisten saat checkout.
