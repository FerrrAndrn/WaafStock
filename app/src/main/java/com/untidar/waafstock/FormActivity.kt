package com.untidar.waafstock

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // Inisialisasi DBHelper
        dbHelper = DBHelper(this)

        // Inisialisasi view
        val etKodeBarang = findViewById<EditText>(R.id.etKodeBarang)
        val etNamaBarang = findViewById<EditText>(R.id.etNamaBarang)
        val etMerk = findViewById<EditText>(R.id.etMerk)
        val etType = findViewById<EditText>(R.id.etType)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)
        val etSatuan = findViewById<EditText>(R.id.etSatuan)
        val btnAddBarang = findViewById<Button>(R.id.btnAddBarang)

        btnAddBarang.setOnClickListener {
            val kodeBarang = etKodeBarang.text.toString()
            val namaBarang = etNamaBarang.text.toString()
            val merk = etMerk.text.toString()
            val type = etType.text.toString()
            val jumlah = etJumlah.text.toString().toIntOrNull() ?: 0
            val satuan = etSatuan.text.toString()

            if (kodeBarang.isNotEmpty() && namaBarang.isNotEmpty() && merk.isNotEmpty() &&
                type.isNotEmpty() && jumlah > 0 && satuan.isNotEmpty()) {

                val result = dbHelper.addBarang(kodeBarang, namaBarang, merk, type, jumlah, satuan)

                if (result != -1L) {
                    Toast.makeText(this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    clearForm()
                } else {
                    Toast.makeText(this, "Gagal menambahkan barang", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Membersihkan form setelah data berhasil ditambahkan
    private fun clearForm() {
        findViewById<EditText>(R.id.etKodeBarang).text.clear()
        findViewById<EditText>(R.id.etNamaBarang).text.clear()
        findViewById<EditText>(R.id.etMerk).text.clear()
        findViewById<EditText>(R.id.etType).text.clear()
        findViewById<EditText>(R.id.etJumlah).text.clear()
        findViewById<EditText>(R.id.etSatuan).text.clear()
    }
}
