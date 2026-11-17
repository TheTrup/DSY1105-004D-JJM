package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.DetallePedido
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.model.PrioridadPedido
import com.example.insertcode_grupojm.model.Venta
import com.example.insertcode_grupojm.repository.VentasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class VentasUiState(
    val pedidos: List<Pedido> = emptyList(),
    val ventas: List<Venta> = emptyList(),
    val filtroBusqueda: String = "",
    val isLoading: Boolean = false
)

class VentasViewModel(
    private val repository: VentasRepository = VentasRepository()
) : ViewModel() {

    private val _filtroBusqueda = MutableStateFlow("")

    val uiState: StateFlow<VentasUiState> = combine(
        repository.pedidos,
        repository.ventas,
        _filtroBusqueda
    ) { pedidos, ventas, filtro ->
        val pedidosFiltrados = if (filtro.isBlank()) {
            pedidos
        } else {
            pedidos.filter {
                it.nombreCliente.contains(filtro, ignoreCase = true) ||
                        it.id.contains(filtro, ignoreCase = true)
            }
        }
        VentasUiState(
            pedidos = pedidosFiltrados,
            ventas = ventas,
            filtroBusqueda = filtro,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VentasUiState(isLoading = true)
    )

    fun onBuscarQueryChange(nuevaBusqueda: String) {
        _filtroBusqueda.value = nuevaBusqueda
    }

    fun cambiarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(pedidoId, nuevoEstado)
        }
    }

    fun crearNuevoPedido(
        nombreCliente: String,
        nombreProducto: String,
        cantidad: Int,
        precioUnitario: Double,
        prioridad: PrioridadPedido
    ) {
        viewModelScope.launch {
            val correlativo = 101 + uiState.value.pedidos.size
            val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val detalle = DetallePedido(
                productoId = "PROD-$correlativo",
                nombreProducto = nombreProducto.trim(),
                cantidad = cantidad,
                precioUnitario = precioUnitario
            )
            val nuevoPedido = Pedido(
                id = "PED-$correlativo",
                clienteId = "CLI-$correlativo",
                nombreCliente = nombreCliente.trim(),
                vendedorId = "VEN-01",
                fechaCreacion = fechaActual,
                prioridad = prioridad,
                estado = EstadoPedido.PENDIENTE,
                items = listOf(detalle),
                total = detalle.subtotal
            )
            repository.agregarPedido(nuevoPedido)
        }
    }
}