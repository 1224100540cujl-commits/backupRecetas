package com.recetas.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.recetas.app.database.AppDatabase
import com.recetas.app.database.Recipe
import com.recetas.app.database.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Recipe>>
    val favorites: LiveData<List<Recipe>>

    init {
        val recipeDao = AppDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
        favorites = repository.favorites
    }

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun update(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }

    fun searchRecipes(query: String): LiveData<List<Recipe>> {
        return repository.searchRecipes(query)
    }

    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> {
        return repository.getRecipesByCategory(category)
    }

    fun getRecipeById(id: Int): LiveData<Recipe> {
        return repository.getRecipeById(id)
    }
}