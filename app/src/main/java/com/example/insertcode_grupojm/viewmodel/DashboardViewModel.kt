package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.IndicadoresDashboard
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.repository.VentasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.roundToInt

data class DashboardUiState(
    val indicadores: IndicadoresDashboard? = null,
    val tareasPrioritarias: List<Pedido> = emptyList(),
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val ventasRepository: VentasRepository = VentasRepository()
) : ViewModel() {

    private val metaVentasDiaria = 5_000_000.0

    val uiState: StateFlow<DashboardUiState> = ventasRepository.pedidos
        .map { pedidos ->
            val entregados = pedidos.filter { it.estado == EstadoPedido.ENTREGADO }
            val activos = pedidos.filter {
                it.estado != EstadoPedido.ENTREGADO && it.estado != EstadoPedido.CANCELADO
            }

            val totalVentas = entregados.sumOf { it.total }
            val porcentajeMeta = ((totalVentas / metaVentasDiaria) * 1000).roundToInt() / 10f

            val indicadoresCalculados = IndicadoresDashboard(
                ventasTotalesHoy = totalVentas,
                pedidosPendientes = activos.size,
                cumplimientoMetasPorcentaje = porcentajeMeta,
                entregasRealizadas = entregados.size,
                entregasPendientes = activos.size
            )

            val prioritarios = activos.take(4)

            DashboardUiState(
                indicadores = indicadoresCalculados,
                tareasPrioritarias = prioritarios,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState(isLoading = true)
        )
}