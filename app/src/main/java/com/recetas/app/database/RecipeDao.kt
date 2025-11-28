package com.recetas.app.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): LiveData<Recipe>

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    fun getFavorites(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :searchQuery || '%' OR ingredients LIKE '%' || :searchQuery || '%'")
    fun searchRecipes(searchQuery: String): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :category")
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>>
}