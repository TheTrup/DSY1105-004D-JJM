package com.example.insertcode_grupojm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val correo: String,
    val direccion: String,
    val modoEspecial: Boolean,
    val fotoPath: String?,
    val fechaRegistro: Date
)