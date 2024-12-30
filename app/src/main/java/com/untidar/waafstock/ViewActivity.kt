package com.untidar.waafstock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DBHelper
    private lateinit var barangAdapter: BarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi DBHelper
        dbHelper = DBHelper(this)

        // Mengambil data barang dari database dan mengatur adapter
        val barangList = dbHelper.getAllBarang()

        // Inisialisasi adapter dengan data barang
        barangAdapter = BarangAdapter(
            barangList.toMutableList(), // MutableList agar bisa dimodifikasi
            onEditClick = { barang ->
                editBarang(barang) // Fungsi untuk beralih ke EditActivity
            },
            onDeleteClick = { barang ->
                deleteBarang(barang) // Fungsi untuk menghapus barang
            }
        )

        // Set adapter ke RecyclerView
        recyclerView.adapter = barangAdapter
    }

    // Fungsi untuk menghapus barang dari database dan memperbarui RecyclerView
    private fun deleteBarang(barang: Barang) {
        // Menghapus data barang dari tabel menggunakan DBHelper
        val rowsDeleted = dbHelper.deleteBarang(barang.kodeBarang)

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Berhasil dihapus: ${barang.namaBarang}", Toast.LENGTH_SHORT).show()
            // Hapus item dari daftar barang dan beri tahu adapter
            barangAdapter.removeBarang(barang.kodeBarang) // Kirim kodeBarang, bukan objek Barang
        } else {
            Toast.makeText(this, "Gagal menghapus barang", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk beralih ke EditActivity dan mengirim data barang
    private fun editBarang(barang: Barang) {
        val intent = Intent(this, EditActivity::class.java)
        // Kirim data barang ke EditActivity menggunakan Intent
        intent.putExtra("KODE_BARANG", barang.kodeBarang)
        intent.putExtra("NAMA_BARANG", barang.namaBarang)
        intent.putExtra("MERK", barang.merk)
        intent.putExtra("TYPE", barang.type)
        intent.putExtra("JUMLAH", barang.jumlah)
        intent.putExtra("SATUAN", barang.satuan)
        startActivityForResult(intent, REQUEST_CODE_EDIT) // startActivityForResult, bukan startActivity
    }

    // Fungsi untuk menangkap hasil dari EditActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT && resultCode == Activity.RESULT_OK) {
            // Memuat ulang data dari database setelah data diedit
            val barangList = dbHelper.getAllBarang()
            barangAdapter.setBarangList(barangList) // Perbarui data pada adapter
        }
    }

    companion object {
        const val REQUEST_CODE_EDIT = 1 // Kode untuk identifikasi EditActivity
    }
}
