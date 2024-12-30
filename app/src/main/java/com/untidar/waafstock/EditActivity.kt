package com.untidar.waafstock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {

    private lateinit var etKodeBarang: EditText
    private lateinit var etNamaBarang: EditText
    private lateinit var etMerk: EditText
    private lateinit var etType: EditText
    private lateinit var etJumlah: EditText
    private lateinit var etSatuan: EditText
    private lateinit var btnSaveChanges: Button

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        etKodeBarang = findViewById(R.id.etKodeBarang)
        etNamaBarang = findViewById(R.id.etNamaBarang)
        etMerk = findViewById(R.id.etMerk)
        etType = findViewById(R.id.etType)
        etJumlah = findViewById(R.id.etJumlah)
        etSatuan = findViewById(R.id.etSatuan)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)

        dbHelper = DBHelper(this)

        // Isi field dengan data yang dikirim dari ViewActivity
        etKodeBarang.setText(intent.getStringExtra("KODE_BARANG"))
        etNamaBarang.setText(intent.getStringExtra("NAMA_BARANG"))
        etMerk.setText(intent.getStringExtra("MERK"))
        etType.setText(intent.getStringExtra("TYPE"))
        etJumlah.setText(intent.getIntExtra("JUMLAH", 0).toString())
        etSatuan.setText(intent.getStringExtra("SATUAN"))

        btnSaveChanges.setOnClickListener {
            val kodeBarang = etKodeBarang.text.toString()
            val namaBarang = etNamaBarang.text.toString()
            val merk = etMerk.text.toString()
            val type = etType.text.toString()
            val jumlah = etJumlah.text.toString().toIntOrNull() ?: 0
            val satuan = etSatuan.text.toString()

            val barang = Barang(kodeBarang, namaBarang, merk, type, jumlah, satuan)

            val rowsUpdated = dbHelper.updateBarang(barang)
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK) // Kirim hasil kembali ke ViewActivity
                finish() // Tutup EditActivity
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
