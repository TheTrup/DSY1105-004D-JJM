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
                id = "PRJ-104",
                clienteId = "CLI-01",
                nombreCliente = "Logística Austral SpA",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-17 09:30",
                prioridad = PrioridadPedido.ALTA,
                estado = EstadoPedido.PENDIENTE,
                items = listOf(
                    DetallePedido("SRV-01", "Desarrollo a la Medida (ERP)", 1, 2_400_000.0)
                ),
                total = 2_400_000.0
            ),
            Pedido(
                id = "PRJ-103",
                clienteId = "CLI-02",
                nombreCliente = "Clínica Santa María Norte",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-17 08:45",
                prioridad = PrioridadPedido.ALTA,
                estado = EstadoPedido.EN_PROCESO,
                items = listOf(
                    DetallePedido("SRV-02", "Dashboards Power BI Ejecutivo", 2, 650_000.0)
                ),
                total = 1_300_000.0
            ),
            Pedido(
                id = "PRJ-102",
                clienteId = "CLI-03",
                nombreCliente = "Financiera Andina S.A.",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-16 17:15",
                prioridad = PrioridadPedido.MEDIA,
                estado = EstadoPedido.DESPACHADO,
                items = listOf(
                    DetallePedido("SRV-04", "Outsourcing TI (Célula Dedicada)", 1, 1_850_000.0)
                ),
                total = 1_850_000.0
            ),
            Pedido(
                id = "PRJ-101",
                clienteId = "CLI-04",
                nombreCliente = "Retail Pacífico",
                vendedorId = "VEN-01",
                fechaCreacion = "2026-11-16 11:00",
                prioridad = PrioridadPedido.BAJA,
                estado = EstadoPedido.ENTREGADO,
                items = listOf(
                    DetallePedido("SRV-03", "App Móvil Corporativa Android", 1, 1_900_000.0)
                ),
                total = 1_900_000.0
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