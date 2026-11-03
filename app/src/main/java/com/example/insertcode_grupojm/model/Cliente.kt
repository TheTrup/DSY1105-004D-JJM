package com.example.insertcode_grupojm.model

data class Sucursal(
    val id: String,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
)

data class Cliente(
    val id: String,
    val nombre: String,
    val rut: String,
    val direccion: String,
    val telefono: String,
    val email: String,
    val sucursalId: String
)