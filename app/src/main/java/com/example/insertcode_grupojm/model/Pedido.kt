package com.example.insertcode_grupojm.model

enum class EstadoPedido {
    PENDIENTE,
    EN_PROCESO,
    DESPACHADO,
    ENTREGADO,
    CANCELADO
}

enum class PrioridadPedido {
    BAJA,
    MEDIA,
    ALTA,
    URGENTE
}

data class Producto(
    val id: String,
    val nombre: String,
    val codigoSKU: String,
    val precioUnitario: Double,
    val stockDisponible: Int
)

data class DetallePedido(
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}

data class Pedido(
    val id: String,
    val clienteId: String,
    val nombreCliente: String,
    val vendedorId: String,
    val fechaCreacion: String,
    val prioridad: PrioridadPedido,
    val estado: EstadoPedido,
    val items: List<DetallePedido>,
    val total: Double
)