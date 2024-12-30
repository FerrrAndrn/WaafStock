package com.untidar.waafstock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatsActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        barChart = findViewById(R.id.barChart)

        // Ambil data dari database
        val dbHelper = DBHelper(this)
        val dataList = dbHelper.getBarangWithMerk()

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        dataList.forEachIndexed { index, triple ->
            val (namaBarang, merk, jumlah) = triple
            entries.add(BarEntry(index.toFloat(), jumlah.toFloat()))

            // Tambahkan Nama Barang di atas dan Merk di bawah
            val label = "$namaBarang $merk" // Ini membuat dua baris di label
            labels.add(label)
        }

        val dataSet = BarDataSet(entries, "Jumlah Barang")
        dataSet.color = ContextCompat.getColor(this, R.color.navy)
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.setFitBars(true)

        // Atur sumbu X
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // Jarak antar label
        xAxis.labelRotationAngle = 0f // Putar teks jika terlalu panjang
        xAxis.textColor = ContextCompat.getColor(this, R.color.navy)
        xAxis.setDrawGridLines(false) // Hilangkan garis di sumbu X

        barChart.invalidate() // Refresh chart
    }
}
