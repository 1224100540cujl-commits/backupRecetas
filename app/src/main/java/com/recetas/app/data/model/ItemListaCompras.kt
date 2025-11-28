package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items_lista_compras",
    foreignKeys = [
        ForeignKey(
            entity = ListaCompras::class,
            parentColumns = ["id"],
            childColumns = ["lista_compras_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemListaCompras(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "lista_compras_id", index = true)
    val listaComprasId: String = "",

    @ColumnInfo(name = "nombre_ingrediente")
    val nombreIngrediente: String = "",

    @ColumnInfo(name = "cantidad")
    val cantidad: String = "",

    @ColumnInfo(name = "unidad")
    val unidad: String = "",

    @ColumnInfo(name = "marcado")
    val marcado: Boolean = false
)