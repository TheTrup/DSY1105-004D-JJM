package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.IndicadoresDashboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardRepository {

    private val _indicadores = MutableStateFlow(
        IndicadoresDashboard(
            ventasTotalesHoy = 462500.0,
            cumplimientoMetasPorcentaje = 78.5f,
            pedidosPendientes = 5,
            entregasRealizadas = 12,
            entregasPendientes = 3
        )
    )
    val indicadores: Flow<IndicadoresDashboard> = _indicadores.asStateFlow()
}