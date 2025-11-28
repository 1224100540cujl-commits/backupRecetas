package com.recetas.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.recetas.app.databinding.ActivityProfileBinding
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Cargar datos del usuario
        loadUserData()

        // Cargar estadísticas
        loadStatistics()

        // Clicks en las opciones
        binding.editProfileCard.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.myRecipesCard.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.favoritesCard.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
            finish()
        }

        binding.shoppingListCard.setOnClickListener {
            Toast.makeText(this, "Abre una receta para ver su lista de compras", Toast.LENGTH_SHORT).show()
        }

        // Botón cerrar sesión
        binding.logoutButton.setOnClickListener {
            val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
            prefs.edit().putBoolean("isLoggedIn", false).apply()

            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Bottom Navigation
        setupBottomNavigation()
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences("RecetAppPrefs", MODE_PRIVATE)
        val name = prefs.getString("name", "Chef Usuario") ?: "Chef Usuario"
        val email = prefs.getString("email", "chef@recetapp.com") ?: "chef@recetapp.com"
        val avatar = prefs.getString("avatar", "👨‍🍳") ?: "👨‍🍳"

        binding.profileName.text = name
        binding.profileEmail.text = email

        // Usar findViewById en lugar de binding para el avatar
        findViewById<TextView>(R.id.profileAvatar)?.text = avatar
    }

    private fun loadStatistics() {
        // Total de recetas
        recipeViewModel.allRecipes.observe(this) { recipes ->
            binding.totalRecipesText.text = recipes.size.toString()

            // Contar categorías únicas
            val uniqueCategories = recipes.map { it.category }.distinct().size
            binding.totalCategoriesText.text = uniqueCategories.toString()
        }

        // Total de favoritos
        recipeViewModel.favorites.observe(this) { favorites ->
            binding.totalFavoritesText.text = favorites.size.toString()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_profile

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
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loadUserData()
        loadStatistics()
    }
}