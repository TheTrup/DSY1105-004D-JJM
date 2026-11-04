package com.example.insertcode_grupojm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val direccion: String,
    val modoEspecial: Boolean = false,
    val fotoPath: String? = null,
    val fechaRegistro: Date = Date()
)