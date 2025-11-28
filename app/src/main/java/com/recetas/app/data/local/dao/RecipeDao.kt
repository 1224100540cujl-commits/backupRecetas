package com.recetas.app.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.recetas.app.data.model.Receta

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Receta)

    @Update
    suspend fun update(recipe: Receta)

    @Delete
    suspend fun delete(recipe: Receta)

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Receta>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): LiveData<Receta>

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    fun getFavorites(): LiveData<List<Receta>>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :searchQuery || '%' OR ingredients LIKE '%' || :searchQuery || '%'")
    fun searchRecipes(searchQuery: String): LiveData<List<Receta>>

    @Query("SELECT * FROM recipes WHERE category = :category")
    fun getRecipesByCategory(category: String): LiveData<List<Receta>>
}