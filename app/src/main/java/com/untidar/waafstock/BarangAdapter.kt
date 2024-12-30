package com.untidar.waafstock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class BarangAdapter(
    private val barangList: MutableList<Barang>, // Gunakan MutableList agar bisa menghapus item
    private val onEditClick: (Barang) -> Unit,
    private val onDeleteClick: (Barang) -> Unit
) : RecyclerView.Adapter<BarangAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barang = barangList[position]
        holder.tvKodeBarang.text = barang.kodeBarang
        holder.tvNamaBarang.text = barang.namaBarang
        holder.tvMerk.text = barang.merk
        holder.tvType.text = barang.type
        holder.tvJumlah.text = barang.jumlah.toString()
        holder.tvSatuan.text = barang.satuan

        // Set listeners untuk tombol Edit dan Delete
        holder.btnEdit.setOnClickListener {
            onEditClick(barang) // Fungsi callback saat tombol Edit ditekan
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(barang) // Fungsi callback saat tombol Delete ditekan
        }
    }

    override fun getItemCount(): Int = barangList.size

    /**
     * Fungsi untuk menghapus barang dari adapter
     * @param kodeBarang Kode barang yang ingin dihapus dari daftar
     */
    fun removeBarang(kodeBarang: String) {
        val position = barangList.indexOfFirst { it.kodeBarang == kodeBarang }
        if (position != -1) {
            barangList.removeAt(position) // Hapus item dari daftar barang
            notifyItemRemoved(position)  // Beri tahu RecyclerView bahwa item telah dihapus
        }
    }

    /**
     * Fungsi untuk memperbarui data barang pada posisi tertentu
     * @param updatedBarang Objek Barang dengan data yang sudah diperbarui
     */
    fun updateBarang(updatedBarang: Barang) {
        val position = barangList.indexOfFirst { it.kodeBarang == updatedBarang.kodeBarang }
        if (position != -1) {
            barangList[position] = updatedBarang // Perbarui item di daftar barang
            notifyItemChanged(position) // Beri tahu RecyclerView bahwa data telah diperbarui
        }
    }

    /**
     * Fungsi untuk memperbarui seluruh daftar barang
     * @param newList Daftar barang baru yang akan menggantikan daftar saat ini
     */
    fun setBarangList(newList: List<Barang>) {
        barangList.clear() // Bersihkan daftar barang saat ini
        barangList.addAll(newList) // Tambahkan data baru dari database
        notifyDataSetChanged() // Beri tahu RecyclerView bahwa semua data telah diperbarui
    }

    /**
     * ViewHolder untuk memegang tampilan item barang
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKodeBarang: TextView = itemView.findViewById(R.id.tvkodeBarang)
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvnamaBarang)
        val tvMerk: TextView = itemView.findViewById(R.id.tvMerk)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvSatuan: TextView = itemView.findViewById(R.id.tvSatuan)
        val btnEdit: AppCompatButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: AppCompatButton = itemView.findViewById(R.id.btnDelete)
    }
}
