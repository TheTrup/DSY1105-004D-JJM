package com.example.insertcode_grupojm.model

data class Venta(
    val id: String,
    val pedidoId: String,
    val clienteId: String,
    val vendedorId: String,
    val fechaVenta: String,
    val montoTotal: Double
)