package com.recetas.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.recetas.app.databinding.ActivitySplashBinding
import com.recetas.app.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashDuration = 3000L // 3 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esperar 3 segundos y luego ir al Login
        Handler(Looper.getMainLooper()).postDelayed({
            // Verificar si el usuario ya está "logueado"
            val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

            val intent = if (isLoggedIn) {
                // Si ya está logueado, ir directo a MainActivity
                Intent(this, MainActivity::class.java)
            } else {
                // Si no está logueado, ir a LoginActivity
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, splashDuration)
    }
}