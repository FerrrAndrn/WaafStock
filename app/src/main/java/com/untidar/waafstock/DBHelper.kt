package com.untidar.waafstock

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "waafstock_db"
        const val DATABASE_VERSION = 1

        // Tabel User
        const val TABLE_USER = "user"
        const val USER_ID = "id"
        const val USER_KODE_PEGAWAI = "kode_pegawai"
        const val USER_NAMA_PEGAWAI = "nama_pegawai"
        const val USER_USERNAME = "username"
        const val USER_PASSWORD = "password"
        const val USER_EMAIL = "email"

        // Tabel Barang
        const val TABLE_BARANG = "barang"
        const val BARANG_KODE = "kode_barang"
        const val BARANG_NAMA = "nama_barang"
        const val BARANG_MERK = "merk"
        const val BARANG_TYPE = "type"
        const val BARANG_JUMLAH = "jumlah"
        const val BARANG_SATUAN = "satuan"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Membuat tabel user
        val createUserTable = """
            CREATE TABLE $TABLE_USER (
                $USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USER_KODE_PEGAWAI TEXT NOT NULL,
                $USER_NAMA_PEGAWAI TEXT NOT NULL,
                $USER_USERNAME TEXT NOT NULL,
                $USER_PASSWORD TEXT NOT NULL,
                $USER_EMAIL TEXT NOT NULL
            )
        """

        // Membuat tabel barang
        val createBarangTable = """
            CREATE TABLE $TABLE_BARANG (
                $BARANG_KODE TEXT PRIMARY KEY,
                $BARANG_NAMA TEXT NOT NULL,
                $BARANG_MERK TEXT NOT NULL,
                $BARANG_TYPE TEXT NOT NULL,
                $BARANG_JUMLAH INTEGER NOT NULL,
                $BARANG_SATUAN TEXT NOT NULL
            )
        """

        db?.execSQL(createUserTable)
        db?.execSQL(createBarangTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Hapus tabel lama dan buat yang baru
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BARANG")
        onCreate(db)
    }

    // ================== FUNGSI UNTUK TABEL USER ==================

    // Fungsi untuk menambahkan user
    fun addUser(
        kodePegawai: String,
        namaPegawai: String,
        username: String,
        password: String,
        email: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_KODE_PEGAWAI, kodePegawai)
            put(USER_NAMA_PEGAWAI, namaPegawai)
            put(USER_USERNAME, username)
            put(USER_PASSWORD, password)
            put(USER_EMAIL, email)
        }
        return db.insert(TABLE_USER, null, values)
    }

    // Fungsi untuk mengecek apakah username dan password ada di database
    fun isValidLogin(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USER,
            null,
            "$USER_USERNAME = ? AND $USER_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    // ================== FUNGSI UNTUK TABEL BARANG ==================

    // Fungsi untuk menambahkan barang ke tabel barang
    fun addBarang(
        kodeBarang: String,
        namaBarang: String,
        merk: String,
        type: String,
        jumlah: Int,
        satuan: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(BARANG_KODE, kodeBarang)
            put(BARANG_NAMA, namaBarang)
            put(BARANG_MERK, merk)
            put(BARANG_TYPE, type)
            put(BARANG_JUMLAH, jumlah)
            put(BARANG_SATUAN, satuan)
        }
        return db.insert(TABLE_BARANG, null, values)
    }
    fun getAllBarang(): List<Barang> {
        val barangList = mutableListOf<Barang>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_BARANG"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val kodeBarang = cursor.getString(cursor.getColumnIndexOrThrow(BARANG_KODE))
                val namaBarang = cursor.getString(cursor.getColumnIndexOrThrow(BARANG_NAMA))
                val merk = cursor.getString(cursor.getColumnIndexOrThrow(BARANG_MERK))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(BARANG_TYPE))
                val jumlah = cursor.getInt(cursor.getColumnIndexOrThrow(BARANG_JUMLAH))
                val satuan = cursor.getString(cursor.getColumnIndexOrThrow(BARANG_SATUAN))

                val barang = Barang(kodeBarang, namaBarang, merk, type, jumlah, satuan)
                barangList.add(barang)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return barangList
    }
    fun getBarangWithMerk(): List<Triple<String, String, Int>> {
        val dataList = mutableListOf<Triple<String, String, Int>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT nama_barang, merk, jumlah FROM barang", null)
        if (cursor.moveToFirst()) {
            do {
                val namaBarang = cursor.getString(cursor.getColumnIndexOrThrow("nama_barang"))
                val merk = cursor.getString(cursor.getColumnIndexOrThrow("merk"))
                val jumlah = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah"))
                dataList.add(Triple(namaBarang, merk, jumlah)) // Tambahkan Nama Barang, Merk, dan Jumlah ke daftar
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return dataList
    }
    fun deleteBarang(kodeBarang: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BARANG, "$BARANG_KODE = ?", arrayOf(kodeBarang))
        return result // Mengembalikan jumlah baris yang dihapus
    }
    fun getBarangByKode(kodeBarang: String): Barang {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BARANG,
            null,
            "$BARANG_KODE = ?",
            arrayOf(kodeBarang),
            null, null, null
        )
        var barang: Barang? = null
        if (cursor.moveToFirst()) {
            barang = Barang(
                cursor.getString(cursor.getColumnIndexOrThrow(BARANG_KODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(BARANG_NAMA)),
                cursor.getString(cursor.getColumnIndexOrThrow(BARANG_MERK)),
                cursor.getString(cursor.getColumnIndexOrThrow(BARANG_TYPE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(BARANG_JUMLAH)),
                cursor.getString(cursor.getColumnIndexOrThrow(BARANG_SATUAN))
            )
        }
        cursor.close()
        return barang!!
    }

    fun updateBarang(barang: Barang): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(BARANG_NAMA, barang.namaBarang)
            put(BARANG_MERK, barang.merk)
            put(BARANG_TYPE, barang.type)
            put(BARANG_JUMLAH, barang.jumlah)
            put(BARANG_SATUAN, barang.satuan)
        }
        return db.update(TABLE_BARANG, values, "$BARANG_KODE = ?", arrayOf(barang.kodeBarang))
    }

}
