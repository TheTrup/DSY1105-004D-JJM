package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.model.Venta
import com.example.insertcode_grupojm.repository.VentasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
}