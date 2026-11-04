package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.Alerta
import com.example.insertcode_grupojm.model.SeveridadAlerta
import com.example.insertcode_grupojm.model.TipoAlerta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlertasRepository {

    private val _alertas = MutableStateFlow<List<Alerta>>(obtenerAlertasIniciales())
    val alertas: Flow<List<Alerta>> = _alertas.asStateFlow()

    fun marcarComoLeida(alertaId: String) {
        _alertas.update { lista ->
            lista.map { alerta ->
                if (alerta.id == alertaId) alerta.copy(leida = true) else alerta
            }
        }
    }

    private fun obtenerAlertasIniciales(): List<Alerta> {
        return listOf(
            Alerta("ALT-01", "Retraso en Despacho", "El pedido PED-101 presenta retraso en la ruta asignada a la zona Centro.", TipoAlerta.LOGISTICA_RETRASO, SeveridadAlerta.ALTA, "Hace 15 min", false, "PED-101"),
            Alerta("ALT-02", "Stock Crítico", "El producto 'Pack Snack Papas' cuenta con solo 3 unidades en inventario.", TipoAlerta.STOCK_CRITICO, SeveridadAlerta.MEDIA, "Hace 1 hora", false, "PROD-02"),
            Alerta("ALT-03", "Pedido Prioritario Urgente", "Se requiere aprobación inmediata para la orden PED-102.", TipoAlerta.PEDIDO_URGENTE, SeveridadAlerta.ALTA, "Hace 2 horas", false, "PED-102")
        )
    }
}