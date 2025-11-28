package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.recetas.app.database.Recipe
import com.recetas.app.databinding.ActivityEditRecipeBinding

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private var recipeId: Int = 0
    private var currentRecipe: Recipe? = null
    private var selectedEmoji = "🍽️"

    private val categories = listOf("Mexicana", "Italiana", "Japonesa", "Americana", "Ensaladas", "Postres", "Sopas", "Bebidas")
    private val difficulties = listOf("Fácil", "Media", "Difícil")
    private val emojis = listOf("🌮", "🍕", "🍝", "🍣", "🍔", "🥗", "🍲", "🥘", "🍛", "🍜", "🥙", "🌯", "🥪", "🍱", "🍳", "🥞", "🧇", "🥓", "🍗", "🍖")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener ID de la receta
        recipeId = intent.getIntExtra("RECIPE_ID", 0)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup dropdowns
        setupCategoryDropdown()
        setupDifficultyDropdown()

        // Cargar datos de la receta
        loadRecipeData()

        // Click en el área de emoji
        binding.imageCard.setOnClickListener {
            showEmojiPicker()
        }

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Botón eliminar
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmation()
        }

        // Botón guardar cambios
        binding.saveChangesButton.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadRecipeData() {
        recipeViewModel.getRecipeById(recipeId).observe(this) { recipe ->
            if (recipe != null) {
                currentRecipe = recipe

                // Llenar los campos con los datos actuales
                selectedEmoji = recipe.imageUrl ?: "🍽️"
                binding.emojiPreview.text = selectedEmoji
                binding.recipeNameInput.setText(recipe.name)
                binding.recipeTimeInput.setText(recipe.time)
                binding.recipeServingsInput.setText(recipe.servings.toString())
                binding.recipeCategoryInput.setText(recipe.category, false)
                binding.recipeDifficultyInput.setText(recipe.difficulty, false)
                binding.recipeIngredientsInput.setText(recipe.ingredients)
                binding.recipeInstructionsInput.setText(recipe.instructions)
            }
        }
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

    private fun saveChanges() {
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

        val servings = servingsStr.toIntOrNull() ?: 0
        if (servings <= 0) {
            binding.recipeServingsInput.error = "Número inválido"
            return
        }

        // Actualizar la receta
        currentRecipe?.let { recipe ->
            val updatedRecipe = recipe.copy(
                name = name,
                category = category,
                time = time,
                servings = servings,
                difficulty = difficulty,
                ingredients = ingredients,
                instructions = instructions,
                imageUrl = selectedEmoji
            )

            recipeViewModel.update(updatedRecipe)
            Toast.makeText(this, "Receta actualizada ✅", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Receta")
            .setMessage("¿Estás seguro de que quieres eliminar esta receta?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteRecipe()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteRecipe() {
        currentRecipe?.let { recipe ->
            recipeViewModel.delete(recipe)
            Toast.makeText(this, "Receta eliminada 🗑️", Toast.LENGTH_SHORT).show()

            // Volver al inicio
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}