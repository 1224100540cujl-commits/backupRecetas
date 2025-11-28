package com.recetas.app.data.repository

import androidx.lifecycle.LiveData
import com.recetas.app.data.local.dao.RecipeDao
import com.recetas.app.data.model.Receta

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: LiveData<List<Receta>> = recipeDao.getAllRecipes()
    val favorites: LiveData<List<Receta>> = recipeDao.getFavorites()

    suspend fun insert(recipe: Receta) {
        recipeDao.insert(recipe)
    }

    suspend fun update(recipe: Receta) {
        recipeDao.update(recipe)
    }

    suspend fun delete(recipe: Receta) {
        recipeDao.delete(recipe)
    }

    fun searchRecipes(query: String): LiveData<List<Receta>> {
        return recipeDao.searchRecipes(query)
    }

    fun getRecipesByCategory(category: String): LiveData<List<Receta>> {
        return recipeDao.getRecipesByCategory(category)
    }

    fun getRecipeById(id: Int): LiveData<Receta> {
        return recipeDao.getRecipeById(id)
    }
}