package com.recetas.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.recetas.app.ui.add.AddRecipeActivity
import com.recetas.app.ui.home.CategoriesActivity
import com.recetas.app.ui.detail.DetailActivity
import com.recetas.app.ui.favorites.FavoritesActivity
import com.recetas.app.ui.profile.ProfileActivity
import com.recetas.app.R
import com.recetas.app.ui.search.SearchActivity
import com.recetas.app.adapters.RecipeAdapter
import com.recetas.app.data.model.Receta
import com.recetas.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter

    private val categories = listOf("Todas", "Mexicana", "Italiana", "Japonesa", "Americana", "Ensaladas", "Postres")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar ViewModel
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Setup Categorías
        setupCategories()

        // Observar recetas
        recipeViewModel.allRecipes.observe(this) { recipes ->
            adapter.setRecipes(recipes)
        }

        // Click en búsqueda
        binding.searchEditText.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Click en perfil
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Bottom Navigation
        setupBottomNavigation()

        // Insertar recetas de ejemplo
        insertSampleRecipes()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter { recipe ->
            // Navegar a DetailActivity
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            startActivity(intent)
        }

        binding.recipesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recipesRecyclerView.adapter = adapter
    }
    private fun setupCategories() {
        categories.forEach { category ->
            val chip = LayoutInflater.from(this)
                .inflate(R.layout.item_category_chip, binding.categoriesChipGroup, false) as Chip

            chip.text = category
            chip.isChecked = category == "Todas"

            chip.setOnClickListener {
                if (category == "Todas") {
                    recipeViewModel.allRecipes.observe(this) { recipes ->
                        adapter.setRecipes(recipes)
                    }
                } else {
                    // Abrir CategoriesActivity
                    val intent = Intent(this, CategoriesActivity::class.java)
                    intent.putExtra("CATEGORY", category)
                    startActivity(intent)
                }
            }

            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
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
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun insertSampleRecipes() {
        recipeViewModel.allRecipes.observe(this) { recipes ->
            if (recipes.isEmpty()) {
                val sampleRecipes = listOf(
                    Receta(
                        name = "Tacos al Pastor",
                        category = "Mexicana",
                        time = "30 min",
                        servings = 4,
                        difficulty = "Fácil",
                        ingredients = "Carne de cerdo,Piña,Tortillas,Cilantro,Cebolla,Limón",
                        instructions = "1. Marinar la carne con especias\n2. Asar la carne hasta dorar\n3. Calentar las tortillas\n4. Servir con piña, cilantro y cebolla",
                        imageUrl = "🌮"
                    ),
                    Receta(
                        name = "Pasta Carbonara",
                        category = "Italiana",
                        time = "20 min",
                        servings = 2,
                        difficulty = "Media",
                        ingredients = "Pasta,Huevos,Tocino,Queso parmesano,Pimienta negra",
                        instructions = "1. Cocinar la pasta al dente\n2. Freír el tocino hasta crujiente\n3. Mezclar huevos con queso\n4. Combinar todo caliente",
                        imageUrl = "🍝"
                    ),
                    Receta(
                        name = "Sushi Rolls",
                        category = "Japonesa",
                        time = "45 min",
                        servings = 3,
                        difficulty = "Difícil",
                        ingredients = "Arroz,Nori,Salmón,Aguacate,Pepino,Salsa soya",
                        instructions = "1. Cocinar y enfriar el arroz\n2. Extender nori sobre esterilla\n3. Agregar arroz e ingredientes\n4. Enrollar firmemente y cortar",
                        imageUrl = "🍣"
                    ),
                    Receta(
                        name = "Hamburguesa Casera",
                        category = "Americana",
                        time = "25 min",
                        servings = 4,
                        difficulty = "Fácil",
                        ingredients = "Carne molida,Pan,Queso,Lechuga,Tomate,Cebolla",
                        instructions = "1. Formar hamburguesas de 150g\n2. Asar a la parrilla 4 min por lado\n3. Tostar el pan\n4. Armar con vegetales y salsas",
                        imageUrl = "🍔"
                    ),
                    Receta(
                        name = "Ensalada César",
                        category = "Ensaladas",
                        time = "15 min",
                        servings = 2,
                        difficulty = "Fácil",
                        ingredients = "Lechuga romana,Pollo,Crutones,Queso parmesano,Aderezo césar",
                        instructions = "1. Lavar y cortar lechuga\n2. Cocinar y rebanar pollo\n3. Mezclar todos los ingredientes\n4. Agregar aderezo al gusto",
                        imageUrl = "🥗"
                    ),
                    Receta(
                        name = "Pizza Margarita",
                        category = "Italiana",
                        time = "40 min",
                        servings = 4,
                        difficulty = "Media",
                        ingredients = "Masa,Salsa de tomate,Mozzarella,Albahaca,Aceite de oliva",
                        instructions = "1. Extender la masa en círculo\n2. Agregar salsa de tomate\n3. Distribuir mozzarella\n4. Hornear a 220°C por 15 minutos",
                        imageUrl = "🍕"
                    )
                )

                sampleRecipes.forEach { recipe ->
                    recipeViewModel.insert(recipe)
                }
            }
        }
    }
}