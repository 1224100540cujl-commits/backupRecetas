package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.recetas.app.database.Recipe
import com.recetas.app.databinding.ActivityAddRecipeBinding

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private var selectedEmoji = "🍽️"

    private val categories = listOf("Mexicana", "Italiana", "Japonesa", "Americana", "Ensaladas", "Postres", "Sopas", "Bebidas")
    private val difficulties = listOf("Fácil", "Media", "Difícil")
    private val emojis = listOf("🌮", "🍕", "🍝", "🍣", "🍔", "🥗", "🍲", "🥘", "🍛", "🍜", "🥙", "🌯", "🥪", "🍱", "🍳", "🥞", "🧇", "🥓", "🍗", "🍖")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup dropdowns
        setupCategoryDropdown()
        setupDifficultyDropdown()

        // Click en el área de emoji
        binding.imageCard.setOnClickListener {
            showEmojiPicker()
        }

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Botón guardar
        binding.saveRecipeButton.setOnClickListener {
            saveRecipe()
        }

        // Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.recipeCategoryInput.setAdapter(adapter)
    }

    private fun setupDifficultyDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, difficulties)
        binding.recipeDifficultyInput.setAdapter(adapter)
    }

    private fun showEmojiPicker() {
        AlertDialog.Builder(this)
            .setTitle("Elige un emoji para tu receta")
            .setItems(emojis.toTypedArray()) { _, which ->
                selectedEmoji = emojis[which]
                binding.emojiPreview.text = selectedEmoji
            }
            .show()
    }

    private fun saveRecipe() {
        val name = binding.recipeNameInput.text.toString().trim()
        val time = binding.recipeTimeInput.text.toString().trim()
        val servingsStr = binding.recipeServingsInput.text.toString().trim()
        val category = binding.recipeCategoryInput.text.toString().trim()
        val difficulty = binding.recipeDifficultyInput.text.toString().trim()
        val ingredients = binding.recipeIngredientsInput.text.toString().trim()
        val instructions = binding.recipeInstructionsInput.text.toString().trim()

        // Validaciones
        if (name.isEmpty()) {
            binding.recipeNameInput.error = "El nombre es requerido"
            return
        }

        if (time.isEmpty()) {
            binding.recipeTimeInput.error = "El tiempo es requerido"
            return
        }

        if (servingsStr.isEmpty()) {
            binding.recipeServingsInput.error = "Las porciones son requeridas"
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
            return
        }

        if (difficulty.isEmpty()) {
            Toast.makeText(this, "Selecciona una dificultad", Toast.LENGTH_SHORT).show()
            return
        }

        if (ingredients.isEmpty()) {
            binding.recipeIngredientsInput.error = "Los ingredientes son requeridos"
            return
        }

        if (instructions.isEmpty()) {
            binding.recipeInstructionsInput.error = "La preparación es requerida"
            return
        }

        val servings = servingsStr.toIntOrNull() ?: 0
        if (servings <= 0) {
            binding.recipeServingsInput.error = "Número inválido"
            return
        }

        // Crear la receta
        val newRecipe = Recipe(
            name = name,
            category = category,
            time = time,
            servings = servings,
            difficulty = difficulty,
            ingredients = ingredients,
            instructions = instructions,
            imageUrl = selectedEmoji,
            isFavorite = false
        )

        // Guardar en la base de datos
        recipeViewModel.insert(newRecipe)

        Toast.makeText(this, "Receta guardada exitosamente ✅", Toast.LENGTH_SHORT).show()

        // Volver al inicio
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_add

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_add -> true
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}