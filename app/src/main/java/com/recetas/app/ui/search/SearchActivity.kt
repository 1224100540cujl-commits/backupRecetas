package com.recetas.app.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.recetas.app.ui.add.AddRecipeActivity
import com.recetas.app.R
import com.recetas.app.adapters.RecipeAdapter
import com.recetas.app.databinding.ActivitySearchBinding
import com.recetas.app.ui.detail.DetailActivity
import com.recetas.app.ui.favorites.FavoritesActivity
import com.recetas.app.ui.home.MainActivity
import com.recetas.app.ui.home.RecipeViewModel
import com.recetas.app.ui.profile.ProfileActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Búsqueda en tiempo real
        binding.searchEditTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchRecipes(query)
                } else {
                    hideResults()
                }
            }
        })

        // Chips de ingredientes
        setupIngredientChips()

        // Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter { recipe ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            startActivity(intent)
        }

        binding.searchRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun searchRecipes(query: String) {
        recipeViewModel.searchRecipes(query).observe(this) { recipes ->
            if (recipes.isNotEmpty()) {
                showResults()
                adapter.setRecipes(recipes)
            } else {
                hideResults()
            }
        }
    }

    private fun setupIngredientChips() {
        binding.chipPollo.setOnClickListener { searchByIngredient("Pollo") }
        binding.chipPasta.setOnClickListener { searchByIngredient("Pasta") }
        binding.chipTomate.setOnClickListener { searchByIngredient("Tomate") }
        binding.chipQueso.setOnClickListener { searchByIngredient("Queso") }
        binding.chipAguacate.setOnClickListener { searchByIngredient("Aguacate") }
        binding.chipArroz.setOnClickListener { searchByIngredient("Arroz") }
    }

    private fun searchByIngredient(ingredient: String) {
        binding.searchEditTextInput.setText(ingredient)
        searchRecipes(ingredient)
    }

    private fun showResults() {
        binding.resultsTitle.visibility = View.VISIBLE
        binding.searchRecyclerView.visibility = View.VISIBLE
        binding.recentSearchesSection.visibility = View.GONE
    }

    private fun hideResults() {
        binding.resultsTitle.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.recentSearchesSection.visibility = View.VISIBLE
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_search

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_search -> true
                R.id.nav_add -> {
                    startActivity(Intent(this, AddRecipeActivity::class.java))
                    finish()
                    true
                }
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