# 📱 MMStudio: Multimedia Studio

**MMStudio** adalah aplikasi multimedia Android komprehensif yang dirancang untuk mendemonstrasikan integrasi sistem tingkat lanjut dalam pemrosesan audio, pemutaran video, dan manajemen gambar. Dibangun menggunakan **Jetpack Compose** dan **Material Design 3**, proyek ini menonjolkan antarmuka pengguna modern dengan desain sistem yang modular dan konsisten.

---

## 📖 Daftar Isi

* [Ringkasan](#-ringkasan)
* [Fitur Utama](#-fitur-utama)
* [Sistem Desain UI](#-sistem-desain-ui)
* [Tech Stack](#-tech-stack)
* [Instalasi](#-instalasi)
* [Izin Aplikasi](#-izin-aplikasi)

---

## 🚀 Ringkasan

Dalam pengembangan aplikasi mobile modern, efisiensi penanganan konten multimedia sangat krusial. **MMStudio** mengimplementasikan berbagai Android Multimedia API terbaru untuk memastikan pengalaman pengguna yang mulus. Proyek ini mencakup integrasi pemutar media berbasis Media3, penanganan penyimpanan melalui MediaStore API, dan pengelolaan izin runtime untuk fungsionalitas perangkat keras seperti kamera dan mikrofon.

Aplikasi ini terstruktur di sekitar **Central Dashboard** yang memberikan akses cepat ke tiga modul inti: Audio Hub, Video Player, dan Camera/Gallery.

---

## ✨ Fitur Utama

### 1. 🎥 Advanced Video Player

* **Engine:** Ditenagai oleh **AndroidX Media3 (ExoPlayer)** untuk performa pemutaran yang tangguh.
* **UI Kontrol:** Antarmuka pemutar video modern dengan kendali *playback* yang terintegrasi langsung dalam komponen Compose.
* **Pemuatan URI:** Mendukung pemuatan konten video secara dinamis dari penyimpanan perangkat.

### 2. 📸 Smart Camera & Gallery

* **Dual Input:** Menangkap gambar secara langsung melalui kamera perangkat atau memilih dari galeri sistem.
* **Integrasi MediaStore:** Menyimpan hasil jepretan secara otomatis ke direktori `Pictures/MMStudio` menggunakan **MediaStore API**.
* **Interactive Preview:** Pratinjau gambar instan setelah pengambilan untuk memastikan kualitas konten.

### 3. 🎙️ Audio Hub (Recorder & Player)

* **Recording:** Menangkap input audio berkualitas tinggi melalui mikrofon.
* **Playback:** Pemutar audio terintegrasi untuk meninjau rekaman atau memutar file musik yang ada.
* **Feedback Visual:** Indikator status rekaman yang intuitif bagi pengguna.

---

## 🎨 Sistem Desain UI

MMStudio menerapkan standar **Material Design 3** untuk estetika profesional:
* **Modern Geometry:** Penggunaan `RoundedCornerShape` yang konsisten pada tombol, kartu, dan field input untuk tampilan yang lembut dan modern.
* **Theming & Color Scheme:** Mengimplementasikan palet warna kustom yang mendukung mode terang dan gelap secara dinamis.
* **Typography Hierarchy:** Skala tipografi yang jelas (Display, Headline, Body) untuk memastikan keterbacaan maksimum.
* **Reusable Components:** Struktur folder yang rapi dengan komponen UI yang dapat digunakan kembali untuk konsistensi di seluruh aplikasi.

---

## 🛠 Tech Stack

* **Bahasa:** Kotlin.
* **UI Toolkit:** Jetpack Compose (Material 3).
* **Media Engine:** AndroidX Media3 (ExoPlayer).
* **Architecture:** ViewModel & State Management (StateFlow).
* **System APIs:** * `MediaStore` (Operasi file multimedia).
  * `ActivityResultContracts` (Manajemen Izin & Pemilihan Konten).
  * `MediaRecorder` & `MediaPlayer`.

---

## 📦 Instalasi

1. **Clone repositori**
```bash
git clone https://github.com/username/Praktikum-MP-MMStudio.git

```
2. **Buka di Android Studio**
* Pastikan Anda menggunakan versi terbaru (Ladybug atau lebih tinggi).
3. **Sinkronisasi Gradle**
* Tunggu hingga semua dependensi (Material3, Media3, dll.) selesai diunduh.
4. **Jalankan di Perangkat**
* SDK Minimum: API 24 (Android 7.0).

---

## 🔒 Izin Aplikasi

Aplikasi memerlukan izin runtime berikut untuk berfungsi secara optimal:

* `CAMERA`: Untuk mengambil foto.
* `RECORD_AUDIO`: Untuk merekam suara.
* `READ_EXTERNAL_STORAGE` / `READ_MEDIA_IMAGES`: Untuk mengakses item galeri.
* `WRITE_EXTERNAL_STORAGE` (Untuk SDK < 29): Untuk menyimpan hasil media ke penyimpanan publik.

---

<p align="center">
  Created by <b>Nouzaria</b>
</p>

---

**Apakah Anda ingin saya menambahkan bagian khusus mengenai skema navigasi (AppNavHost) yang digunakan dalam proyek ini ke dalam README?**
