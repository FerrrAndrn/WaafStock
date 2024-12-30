package com.untidar.waafstock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi DBHelper
        dbHelper = DBHelper(this)

        // Inisialisasi view sesuai dengan ID yang benar
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val tvSignUp: TextView = findViewById(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Silakan isi semua field", Toast.LENGTH_SHORT).show()
            } else {
                if (dbHelper.isValidLogin(username, password)) {
                    // Arahkan ke halaman dashboard setelah login sukses, dengan membawa username
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("USERNAME", username)  // Kirim username ke dashboard
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvSignUp.setOnClickListener {
            // Buka halaman registrasi
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
