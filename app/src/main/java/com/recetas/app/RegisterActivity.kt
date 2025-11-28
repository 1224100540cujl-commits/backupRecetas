package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.recetas.app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Botón registrarse
        binding.registerButton.setOnClickListener {
            register()
        }

        // Link a login
        binding.loginLink.setOnClickListener {
            finish()
        }
    }

    private fun register() {
        val name = binding.nameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val confirmPassword = binding.confirmPasswordInput.text.toString().trim()

        // Validaciones
        if (name.isEmpty()) {
            binding.nameInput.error = "El nombre es requerido"
            return
        }

        if (email.isEmpty()) {
            binding.emailInput.error = "El correo es requerido"
            return
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "La contraseña es requerida"
            return
        }

        if (password.length < 6) {
            binding.passwordInput.error = "Mínimo 6 caracteres"
            return
        }

        if (password != confirmPassword) {
            binding.confirmPasswordInput.error = "Las contraseñas no coinciden"
            return
        }

        // Guardar datos en SharedPreferences
        val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("name", name)
            putString("email", email)
            putString("password", password)
            putBoolean("isLoggedIn", true)
            apply()
        }

        Toast.makeText(this, "Cuenta creada exitosamente 🎉", Toast.LENGTH_SHORT).show()

        // Ir a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}