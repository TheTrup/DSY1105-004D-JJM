package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VentasRepository {

    private val _pedidos = MutableStateFlow(
        listOf(
            Pedido(
                id = "PED-103",
                clienteId = "CLI-01",
                nombreCliente = "Duoc UC Sede Alameda (Av. España 8)",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-18 09:30",
                prioridad = PrioridadPedido.ALTA,
                estado = EstadoPedido.PENDIENTE,
                items = listOf(
                    DetallePedido("PROD-01", "Licencia Dashboard Web Sales", 2, 450_000.0)
                ),
                total = 900_000.0
            ),
            Pedido(
                id = "PED-102",
                clienteId = "CLI-02",
                nombreCliente = "Empresa Tech Chile Ltd (Av. Providencia 1234)",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-18 08:45",
                prioridad = PrioridadPedido.MEDIA,
                estado = EstadoPedido.EN_PROCESO,
                items = listOf(
                    DetallePedido("PROD-02", "Módulo IoT Rastreador GPS", 4, 120_000.0)
                ),
                total = 480_000.0
            ),
            Pedido(
                id = "PED-101",
                clienteId = "CLI-03",
                nombreCliente = "Comercial Central InsertCode (Av. Andrés Bello 2680)",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-17 16:20",
                prioridad = PrioridadPedido.BAJA,
                estado = EstadoPedido.ENTREGADO,
                items = listOf(
                    DetallePedido("PROD-03", "Terminal POS Móvil Android", 10, 178_000.0)
                ),
                total = 1_780_000.0
            )
        )
    )
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    fun agregarPedido(pedido: Pedido) {
        _pedidos.update { listaActual -> listOf(pedido) + listaActual }
    }

    fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido) {
        _pedidos.update { listaActual ->
            listaActual.map { pedido ->
                if (pedido.id == pedidoId) pedido.copy(estado = nuevoEstado) else pedido
            }
        }
    }
}