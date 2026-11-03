package com.example.insertcode_grupojm.model

enum class EstadoEntrega {
    PENDIENTE,
    EN_RUTA,
    ENTREGADO,
    NO_ENTREGADO
}

data class Entrega(
    val id: String,
    val pedidoId: String,
    val clienteNombre: String,
    val direccionEntrega: String,
    val latitudSimulada: Double,
    val longitudSimulada: Double,
    val horaEstimada: String,
    val horaEntregaReal: String? = null,
    val estado: EstadoEntrega,
    val evidenciaUrl: String? = null
)

data class Ruta(
    val id: String,
    val repartidorId: String,
    val vehiculoId: String,
    val fecha: String,
    val entregas: List<Entrega>
)