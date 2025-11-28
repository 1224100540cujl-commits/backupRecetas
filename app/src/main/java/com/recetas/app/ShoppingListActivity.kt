package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.recetas.app.databinding.ActivityShoppingListBinding

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private var recipeId: Int = 0
    private val checkedItems = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener ID de la receta
        recipeId = intent.getIntExtra("RECIPE_ID", 0)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Cargar ingredientes
        loadIngredients()

        // Botón compartir
        binding.shareButton.setOnClickListener {
            shareShoppingList()
        }

        // Botón limpiar
        binding.clearButton.setOnClickListener {
            showClearConfirmation()
        }
    }

    private fun loadIngredients() {
        recipeViewModel.getRecipeById(recipeId).observe(this) { recipe ->
            if (recipe != null) {
                binding.recipeNameSubtitle.text = "Ingredientes de: ${recipe.name}"

                // Separar ingredientes
                val ingredients = recipe.ingredients.split(",").map { it.trim() }

                // Agregar checkboxes para cada ingrediente
                binding.ingredientsContainer.removeAllViews()

                ingredients.forEach { ingredient ->
                    addIngredientCheckbox(ingredient)
                }
            }
        }
    }

    private fun addIngredientCheckbox(ingredient: String) {
        val checkboxLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 12, 0, 12)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val checkbox = CheckBox(this).apply {
            isChecked = checkedItems.contains(ingredient)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedItems.add(ingredient)
                } else {
                    checkedItems.remove(ingredient)
                }
                updateIngredientText()
            }
        }

        val textView = TextView(this).apply {
            text = ingredient
            textSize = 16f
            setPadding(16, 0, 0, 0)
            setTextColor(ContextCompat.getColor(this@ShoppingListActivity, R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        checkboxLayout.addView(checkbox)
        checkboxLayout.addView(textView)
        binding.ingredientsContainer.addView(checkboxLayout)

        // Agregar línea divisoria
        val divider = android.view.View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
            )
            setBackgroundColor(ContextCompat.getColor(this@ShoppingListActivity, R.color.gray_light))
        }
        binding.ingredientsContainer.addView(divider)
    }

    private fun updateIngredientText() {
        // Esta función se puede usar para actualizar la UI si es necesario
    }

    private fun shareShoppingList() {
        recipeViewModel.getRecipeById(recipeId).observe(this) { recipe ->
            if (recipe != null) {
                val ingredients = recipe.ingredients.split(",").map { it.trim() }

                val shareText = buildString {
                    append("🛒 Lista de Compras\n")
                    append("Receta: ${recipe.name}\n\n")
                    append("Ingredientes:\n")
                    ingredients.forEach { ingredient ->
                        val check = if (checkedItems.contains(ingredient)) "✅" else "⬜"
                        append("$check $ingredient\n")
                    }
                }

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Compartir lista de compras"))
            }
        }
    }

    private fun showClearConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Limpiar Lista")
            .setMessage("¿Deseas desmarcar todos los ingredientes?")
            .setPositiveButton("Limpiar") { _, _ ->
                clearList()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun clearList() {
        checkedItems.clear()
        loadIngredients()
        Toast.makeText(this, "Lista limpiada ✨", Toast.LENGTH_SHORT).show()
    }
}