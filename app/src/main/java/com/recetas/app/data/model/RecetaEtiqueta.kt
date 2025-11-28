package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recetas_etiquetas",
    primaryKeys = ["receta_id", "etiqueta_id"],
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["receta_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["etiqueta_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecetaEtiqueta(
    @ColumnInfo(name = "receta_id", index = true)
    val recetaId: Int = 0,

    @ColumnInfo(name = "etiqueta_id", index = true)
    val etiquetaId: Int = 0
)