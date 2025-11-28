package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.recetas.app.adapters.RecipeAdapter
import com.recetas.app.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter
    private var category: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener categoría del intent
        category = intent.getStringExtra("CATEGORY") ?: "Todas"

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Actualizar título
        binding.categoryTitle.text = category

        // Botón volver
        binding.backButton.setOnClickListener {
            finish()
        }

        // Setup RecyclerView
        setupRecyclerView()

        // Cargar recetas de la categoría
        loadCategoryRecipes()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter { recipe ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            startActivity(intent)
        }

        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.categoriesRecyclerView.adapter = adapter
    }

    private fun loadCategoryRecipes() {
        recipeViewModel.getRecipesByCategory(category).observe(this) { recipes ->
            if (recipes.isEmpty()) {
                showEmptyState()
            } else {
                showRecipes()
                adapter.setRecipes(recipes)
                binding.recipeCount.text = "${recipes.size} recetas"
            }
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.categoriesRecyclerView.visibility = View.GONE
        binding.recipeCount.text = "0 recetas"
    }

    private fun showRecipes() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.categoriesRecyclerView.visibility = View.VISIBLE
    }
}