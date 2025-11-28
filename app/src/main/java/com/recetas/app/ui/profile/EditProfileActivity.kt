package com.recetas.app.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.recetas.app.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val avatarEmojis = listOf("👨‍🍳", "👩‍🍳", "🧑‍🍳", "👨", "👩", "🧑", "😊", "😎", "🤓", "😋")
    private var selectedAvatar = "👨‍🍳"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos actuales
        loadCurrentData()

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Click en avatar
        binding.avatarEmoji.setOnClickListener {
            showAvatarPicker()
        }

        // Botón guardar
        binding.saveChangesButton.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadCurrentData() {
        val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
        val name = prefs.getString("name", "") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val avatar = prefs.getString("avatar", "👨‍🍳") ?: "👨‍🍳"

        binding.nameInput.setText(name)
        binding.emailInput.setText(email)
        binding.avatarEmoji.text = avatar
        selectedAvatar = avatar
    }

    private fun showAvatarPicker() {
        AlertDialog.Builder(this)
            .setTitle("Elige tu avatar")
            .setItems(avatarEmojis.toTypedArray()) { _, which ->
                selectedAvatar = avatarEmojis[which]
                binding.avatarEmoji.text = selectedAvatar
            }
            .show()
    }

    private fun saveChanges() {
        val name = binding.nameInput.text.toString().trim()
        val newPassword = binding.newPasswordInput.text.toString().trim()
        val confirmPassword = binding.confirmNewPasswordInput.text.toString().trim()

        // Validaciones
        if (name.isEmpty()) {
            binding.nameInput.error = "El nombre es requerido"
            return
        }

        // Si quiere cambiar la contraseña
        if (newPassword.isNotEmpty()) {
            if (newPassword.length < 6) {
                binding.newPasswordInput.error = "Mínimo 6 caracteres"
                return
            }

            if (newPassword != confirmPassword) {
                binding.confirmNewPasswordInput.error = "Las contraseñas no coinciden"
                return
            }
        }

        // Guardar cambios
        val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("name", name)
            putString("avatar", selectedAvatar)
            if (newPassword.isNotEmpty()) {
                putString("password", newPassword)
            }
            apply()
        }

        Toast.makeText(this, "Perfil actualizado ✅", Toast.LENGTH_SHORT).show()
        finish()
    }
}