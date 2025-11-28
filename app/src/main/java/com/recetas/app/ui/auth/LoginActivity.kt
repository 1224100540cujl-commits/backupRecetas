package com.recetas.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.recetas.app.ui.auth.RegisterActivity
import com.recetas.app.databinding.ActivityLoginBinding
import com.recetas.app.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón de login
        binding.loginButton.setOnClickListener {
            login()
        }

        // Link a registro
        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        // Validaciones simples
        if (email.isEmpty()) {
            binding.emailInput.error = "El correo es requerido"
            return
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "La contraseña es requerida"
            return
        }

        // Obtener credenciales guardadas
        val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
        val savedEmail = prefs.getString("email", "")
        val savedPassword = prefs.getString("password", "")

        // Verificar credenciales
        if (email == savedEmail && password == savedPassword) {
            // Login exitoso
            prefs.edit().putBoolean("isLoggedIn", true).apply()

            Toast.makeText(this, "Bienvenido de nuevo 👋", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}