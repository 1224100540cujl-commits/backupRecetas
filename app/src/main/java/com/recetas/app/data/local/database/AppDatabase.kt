package com.recetas.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.recetas.app.data.local.dao.RecipeDao
import com.recetas.app.data.model.*

@Database(
    entities = [
        Usuario::class,
        Receta::class,
        Ingrediente::class,
        Instruccion::class,
        ListaCompras::class,
        ItemListaCompras::class,
        Etiqueta::class,
        RecetaEtiqueta::class
    ],
    version = 2, // Aumentar versión
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recetas_database"
                )
                    .fallbackToDestructiveMigration() // Para desarrollo
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}