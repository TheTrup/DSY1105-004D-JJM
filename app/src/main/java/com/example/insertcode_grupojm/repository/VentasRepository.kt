package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.data.remote.RetrofitInstance
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.model.PrioridadPedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VentasRepository {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: Flow<List<Pedido>> = _pedidos.asStateFlow()

    suspend fun cargarPedidos() {
        try {
            val pedidosRemote = RetrofitInstance.api.getPedidos()
            val pedidosMapeados = pedidosRemote.map { dto ->
                Pedido(
                    id = dto.id,
                    clienteId = dto.clienteId,
                    nombreCliente = dto.nombreCliente,
                    vendedorId = "VEN-01",
                    fechaCreacion = "2026-11-04",
                    prioridad = PrioridadPedido.valueOf(dto.prioridad.uppercase()),
                    estado = EstadoPedido.valueOf(dto.estado.uppercase()),
                    items = emptyList(),
                    total = dto.total
                )
            }
            _pedidos.value = pedidosMapeados
        } catch (e: Exception) {
            // Carga de respaldo local (Offline) ante falla de red
            _pedidos.value = obtenerPedidosDePrueba()
        }
    }

    fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido) {
        _pedidos.update { lista ->
            lista.map { pedido ->
                if (pedido.id == pedidoId) pedido.copy(estado = nuevoEstado) else pedido
            }
        }
    }

    private fun obtenerPedidosDePrueba(): List<Pedido> {
        return listOf(
            Pedido("PED-101", "CLI-01", "Distribuidora El Sol", "VEN-01", "2026-11-04 09:30", PrioridadPedido.URGENTE, EstadoPedido.EN_PROCESO, emptyList(), 162500.0),
            Pedido("PED-102", "CLI-02", "Comercial San José", "VEN-01", "2026-11-04 10:15", PrioridadPedido.ALTA, EstadoPedido.PENDIENTE, emptyList(), 120000.0),
            Pedido("PED-103", "CLI-03", "Minimarket Los Andes", "VEN-02", "2026-11-03 16:45", PrioridadPedido.MEDIA, EstadoPedido.ENTREGADO, emptyList(), 180000.0)
        )
    }
}