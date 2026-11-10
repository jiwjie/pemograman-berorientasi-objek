# Kalkulator CRUD (Spring Boot + PostgreSQL)

## Cara menjalankan
1. Install JDK 17+, Maven, PostgreSQL.
2. Buat database:  `CREATE DATABASE db_kalkulator;`
3. Sesuaikan username/password di `src/main/resources/application.properties`.
4. Jalankan: `mvn spring-boot:run`
5. Buka http://localhost:8080

Tabel `calculations` dibuat otomatis (kolom: id, bilangan1, opr, bilangan2, result, ekspresi, created_at).
Kolom `ekspresi` menyimpan teks utuh, misalnya `7 x 5 = 35`.

## Endpoint
| Method | URL | Fungsi |
|---|---|---|
| GET | /api/calculations | lihat semua |
| POST | /api/calculations | tambah (hitung otomatis) |
| PUT | /api/calculations/{id} | ubah + hitung ulang |
| DELETE | /api/calculations/{id} | hapus |
