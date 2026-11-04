package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VentasRepository {

    private val _pedidos = MutableStateFlow<List<Pedido>>(obtenerPedidosIniciales())
    val pedidos: Flow<List<Pedido>> = _pedidos.asStateFlow()

    private val _ventas = MutableStateFlow<List<Venta>>(obtenerVentasIniciales())
    val ventas: Flow<List<Venta>> = _ventas.asStateFlow()

    fun agregarPedido(pedido: Pedido) {
        _pedidos.update { listaActual -> listaActual + pedido }
    }

    fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido) {
        _pedidos.update { lista ->
            lista.map { pedido ->
                if (pedido.id == pedidoId) pedido.copy(estado = nuevoEstado) else pedido
            }
        }
    }

    private fun obtenerPedidosIniciales(): List<Pedido> {
        return listOf(
            Pedido(
                id = "PED-101",
                clienteId = "CLI-01",
                nombreCliente = "Distribuidora El Sol",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-04 09:30",
                prioridad = PrioridadPedido.URGENTE,
                estado = EstadoPedido.EN_PROCESO,
                items = listOf(
                    DetallePedido("PROD-01", "Caja Bebida 1.5L", 10, 12000.0),
                    DetallePedido("PROD-02", "Pack Snack Papas", 5, 8500.0)
                ),
                total = 162500.0
            ),
            Pedido(
                id = "PED-102",
                clienteId = "CLI-02",
                nombreCliente = "Comercial San José",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-04 10:15",
                prioridad = PrioridadPedido.ALTA,
                estado = EstadoPedido.PENDIENTE,
                items = listOf(
                    DetallePedido("PROD-03", "Caja Agua Mineral 500ml", 20, 6000.0)
                ),
                total = 120000.0
            ),
            Pedido(
                id = "PED-103",
                clienteId = "CLI-03",
                nombreCliente = "Minimarket Los Andes",
                vendedorId = "VEN-02",
                fechaCreacion = "2026-11-03 16:45",
                prioridad = PrioridadPedido.MEDIA,
                estado = EstadoPedido.ENTREGADO,
                items = listOf(
                    DetallePedido("PROD-01", "Caja Bebida 1.5L", 15, 12000.0)
                ),
                total = 180000.0
            )
        )
    }

    private fun obtenerVentasIniciales(): List<Venta> {
        return listOf(
            Venta(
                id = "VEN-501",
                pedidoId = "PED-103",
                clienteId = "CLI-03",
                vendedorId = "VEN-02",
                fechaVenta = "2026-11-03",
                montoTotal = 180000.0
            )
        )
    }
}