package com.untidar.waafstock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)

        val etKodePegawai: EditText = findViewById(R.id.et_kode_pegawai)
        val etNamaPegawai: EditText = findViewById(R.id.et_nama_pegawai)
        val etUsername: EditText = findViewById(R.id.et_username_register)
        val etPassword: EditText = findViewById(R.id.et_password_register)
        val etEmail: EditText = findViewById(R.id.et_email_register)
        val btnRegister: Button = findViewById(R.id.btn_register)

        val tvAlreadyHaveAccount: TextView = findViewById(R.id.tv_already_have_account)
        tvAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup RegisterActivity agar tidak bisa kembali ke halaman ini
        }

        btnRegister.setOnClickListener {
            val kodePegawai = etKodePegawai.text.toString()
            val namaPegawai = etNamaPegawai.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()

            if (kodePegawai.isNotEmpty() && namaPegawai.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                val result = dbHelper.addUser(kodePegawai, namaPegawai, username, password, email)
                if (result != -1L) {
                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke halaman login setelah registrasi sukses
                } else {
                    Toast.makeText(this, "Gagal registrasi", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Silakan lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
