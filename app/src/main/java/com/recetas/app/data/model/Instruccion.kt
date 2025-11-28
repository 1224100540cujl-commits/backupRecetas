package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "instrucciones",
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["receta_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Instruccion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "receta_id", index = true)
    val recetaId: Int = 0,

    @ColumnInfo(name = "paso_numero")
    val pasoNumero: Int = 0,

    @ColumnInfo(name = "descripcion")
    val descripcion: String = ""
)