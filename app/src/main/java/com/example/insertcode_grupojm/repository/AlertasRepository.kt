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
            Alerta(
                id = "ALT-01",
                titulo = "Retraso en Despacho",
                mensaje = "El pedido PED-101 presenta retraso en la ruta asignada a la zona Centro.",
                tipo = TipoAlerta.LOGISTICA_RETRASO,
                severidad = SeveridadAlerta.ALTA,
                timestamp = "Hace 15 min",
                entidadReferenciaId = "PED-101"
            ),
            Alerta(
                id = "ALT-02",
                titulo = "Stock Crítico",
                mensaje = "El producto 'Pack Snack Papas' cuenta con solo 3 unidades en inventario.",
                tipo = TipoAlerta.STOCK_CRITICO,
                severidad = SeveridadAlerta.MEDIA,
                timestamp = "Hace 1 hora",
                entidadReferenciaId = "PROD-02"
            ),
            Alerta(
                id = "ALT-03",
                titulo = "Pedido Prioritario Urgente",
                mensaje = "Se requiere aprobación inmediata para la orden PED-102.",
                tipo = TipoAlerta.PEDIDO_URGENTE,
                severidad = SeveridadAlerta.ALTA,
                timestamp = "Hace 2 horas",
                entidadReferenciaId = "PED-102"
            )
        )
    }
}