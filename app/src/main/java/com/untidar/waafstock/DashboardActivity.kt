package com.untidar.waafstock

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class DashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Ambil username dari MainActivity
        val username = intent.getStringExtra("USERNAME")
        val usernameTextView: TextView = findViewById(R.id.tv_username)
        usernameTextView.text = username

        // Inisialisasi DrawerLayout dan tombol hamburger
        drawerLayout = findViewById(R.id.drawer_layout)
        val hamburgerButton: ImageButton = findViewById(R.id.hamburger_button)

        hamburgerButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Tombol Logout dengan konfirmasi
        val btnLogout: Button = findViewById(R.id.btn_logout)
        btnLogout.setOnClickListener {
            // Tampilkan konfirmasi dialog logout
            showLogoutConfirmationDialog()
        }

        // Sidebar Buttons
        val btnDashboard: Button = findViewById(R.id.menu_dashboard)
        val btnView: Button = findViewById(R.id.menu_view)
        val btnForm: Button = findViewById(R.id.menu_form)
        val btnStats: Button = findViewById(R.id.menu_stats)

        // Atur tombol Dashboard aktif secara default
        setActiveButton(btnDashboard)

        // Set tombol sidebar dan tutup drawer saat tombol diklik
        btnDashboard.setOnClickListener {
            setActiveButton(btnDashboard)
            drawerLayout.closeDrawer(GravityCompat.START) // Tutup sidebar
        }

        btnView.setOnClickListener {
            setActiveButton(btnView)
            startActivity(Intent(this, ViewActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START) // Tutup sidebar
        }

        btnForm.setOnClickListener {
            setActiveButton(btnForm)
            startActivity(Intent(this, FormActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START) // Tutup sidebar
        }

        btnStats.setOnClickListener {
            setActiveButton(btnStats)
            startActivity(Intent(this, StatsActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START) // Tutup sidebar
        }
    }

    // Fungsi untuk mengatur tombol sidebar yang aktif
    private fun setActiveButton(activeButton: Button) {
        val buttons = listOf(
            findViewById<Button>(R.id.menu_dashboard),
            findViewById<Button>(R.id.menu_view),
            findViewById<Button>(R.id.menu_form),
            findViewById<Button>(R.id.menu_stats)
        )

        buttons.forEach { button ->
            if (button == activeButton) {
                // Set background aktif ke navy
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.navy))
                button.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                // Set background tidak aktif ke putih
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                button.setTextColor(ContextCompat.getColor(this, R.color.navy))
            }
        }
    }

    // Fungsi untuk menampilkan dialog konfirmasi logout
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog) // Menggunakan tema kustom
        builder.setMessage("Are you sure you want to log out?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Jika Yes, logout dan kembali ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                // Jika No, tutup dialog
                dialog.dismiss()
            }

        // Set warna tombol Yes dan No
        val alertDialog = builder.create()
        alertDialog.show()

        // Mengubah warna tombol Yes dan No
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.navy))
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.red)) // Pastikan Anda mendefinisikan warna merah di resources
    }
}
