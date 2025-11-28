package com.recetas.app.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recetas.app.ui.add.AddRecipeActivity
import com.recetas.app.ui.profile.ProfileActivity
import com.recetas.app.R
import com.recetas.app.ui.search.SearchActivity
import com.recetas.app.adapters.FavoriteAdapter
import com.recetas.app.databinding.ActivityFavoritesBinding
import com.recetas.app.ui.detail.DetailActivity
import com.recetas.app.ui.home.MainActivity
import com.recetas.app.ui.home.RecipeViewModel

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Observar favoritos
        recipeViewModel.favorites.observe(this) { favorites ->
            if (favorites.isEmpty()) {
                showEmptyState()
            } else {
                showFavorites()
                adapter.setRecipes(favorites)
                binding.favoritesCount.text = "${favorites.size} recetas guardadas"
            }
        }

        // Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(
            onItemClick = { recipe ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("RECIPE_ID", recipe.id)
                startActivity(intent)
            },
            onRemoveFavorite = { recipe ->
                val updatedRecipe = recipe.copy(isFavorite = false)
                recipeViewModel.update(updatedRecipe)
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
        )

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoritesRecyclerView.adapter = adapter
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.favoritesCount.text = "0 recetas guardadas"
    }

    private fun showFavorites() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.VISIBLE
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_favorites

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
                R.id.nav_add -> {
                    startActivity(Intent(this, AddRecipeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_favorites -> true
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