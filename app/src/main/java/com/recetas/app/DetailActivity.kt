package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.recetas.app.adapters.RecipeDetailPagerAdapter
import com.recetas.app.database.Recipe
import com.recetas.app.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private var recipeId: Int = 0
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener ID de la receta
        recipeId = intent.getIntExtra("RECIPE_ID", 0)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Cargar datos de la receta
        loadRecipeData()

        // Botón de favorito
        binding.favoriteButton.setOnClickListener {
            toggleFavorite()
        }

        // Botón de editar
        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeId)
            startActivity(intent)
        }
        // Botón de lista de compras
        binding.shoppingListButton.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeId)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()
        loadRecipeData()
    }

    private fun loadRecipeData() {
        recipeViewModel.getRecipeById(recipeId).observe(this) { recipe ->
            if (recipe != null) {
                currentRecipe = recipe

                binding.recipeEmojiDetail.text = recipe.imageUrl ?: "🍽️"
                binding.recipeNameDetail.text = recipe.name
                binding.recipeCategoryDetail.text = recipe.category
                binding.recipeTimeDetail.text = recipe.time
                binding.recipeServingsDetail.text = recipe.servings.toString()
                binding.recipeDifficultyDetail.text = recipe.difficulty

                updateFavoriteIcon(recipe.isFavorite)

                // Configurar ViewPager con Tabs
                setupViewPager(recipe.ingredients, recipe.instructions)
            }
        }
    }

    private fun setupViewPager(ingredients: String, instructions: String) {
        // Mostrar ViewPager y TabLayout
        binding.viewPager.visibility = android.view.View.VISIBLE
        binding.tabLayout.visibility = android.view.View.VISIBLE

        // Convertir ingredientes a lista
        val ingredientsList = ingredients.split(",").map { it.trim() }

        // Configurar adapter
        val adapter = RecipeDetailPagerAdapter(ingredientsList, instructions)
        binding.viewPager.adapter = adapter

        // Conectar TabLayout con ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ingredientes"
                1 -> "Preparación"
                else -> ""
            }
        }.attach()
    }

    private fun toggleFavorite() {
        currentRecipe?.let { recipe ->
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
            recipeViewModel.update(updatedRecipe)
            currentRecipe = updatedRecipe

            updateFavoriteIcon(updatedRecipe.isFavorite)

            val message = if (updatedRecipe.isFavorite) {
                "Agregado a favoritos ❤️"
            } else {
                "Eliminado de favoritos"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        // El icono se mantiene, solo cambia funcionalidad
    }
}