package com.recetas.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.recetas.app.data.local.database.AppDatabase
import com.recetas.app.data.model.Receta
import com.recetas.app.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Receta>>
    val favorites: LiveData<List<Receta>>

    init {
        val recipeDao = AppDatabase.Companion.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
        favorites = repository.favorites
    }

    fun insert(recipe: Receta) = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun update(recipe: Receta) = viewModelScope.launch {
        repository.update(recipe)
    }

    fun delete(recipe: Receta) = viewModelScope.launch {
        repository.delete(recipe)
    }

    fun searchRecipes(query: String): LiveData<List<Receta>> {
        return repository.searchRecipes(query)
    }

    fun getRecipesByCategory(category: String): LiveData<List<Receta>> {
        return repository.getRecipesByCategory(category)
    }

    fun getRecipeById(id: Int): LiveData<Receta> {
        return repository.getRecipeById(id)
    }
}