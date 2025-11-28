package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "listas_compras",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListaCompras(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "usuario_id", index = true)
    val usuarioId: String = "",

    @ColumnInfo(name = "nombre")
    val nombre: String = "",

    @ColumnInfo(name = "creado_en")
    val creadoEn: Long = System.currentTimeMillis()
)