package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.IndicadoresDashboard
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.repository.DashboardRepository
import com.example.insertcode_grupojm.repository.VentasRepository
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val indicadores: IndicadoresDashboard? = null,
    val tareasPrioritarias: List<Pedido> = emptyList(),
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository = DashboardRepository(),
    private val ventasRepository: VentasRepository = VentasRepository()
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        dashboardRepository.indicadores,
        ventasRepository.pedidos
    ) { indicadores, pedidos ->
        DashboardUiState(
            indicadores = indicadores,
            tareasPrioritarias = pedidos.take(3), // Muestra las primeras 3 tareas prioritarias
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )
}