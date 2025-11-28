package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredientes",
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["receta_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "receta_id", index = true)
    val recetaId: Int = 0,

    @ColumnInfo(name = "nombre_ingrediente")
    val nombreIngrediente: String = "",

    @ColumnInfo(name = "cantidad")
    val cantidad: String = "",

    @ColumnInfo(name = "unidad")
    val unidad: String = ""
)