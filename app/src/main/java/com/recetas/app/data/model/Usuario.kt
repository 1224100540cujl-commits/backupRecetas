package com.recetas.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "nombre")
    val nombre: String = "",

    @ColumnInfo(name = "correo")
    val correo: String = "",

    @ColumnInfo(name = "contrasena")
    val contrasena: String = "", // Solo Room, NO Firebase

    @ColumnInfo(name = "avatar")
    val avatar: String = "👨‍🍳",

    @ColumnInfo(name = "creado_en")
    val creadoEn: Long = System.currentTimeMillis()
)