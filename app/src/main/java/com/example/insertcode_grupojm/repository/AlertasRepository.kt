package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.Alerta
import com.example.insertcode_grupojm.model.SeveridadAlerta
import com.example.insertcode_grupojm.model.TipoAlerta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlertasRepository {

    private val _alertas = MutableStateFlow(
        listOf(
            Alerta(
                id = "ALT-01",
                titulo = "Hito Crítico: Entrega ERP",
                mensaje = "Logística Austral SpA requiere pase a producción del módulo de inventario hoy antes de las 18:00.",
                tipo = TipoAlerta.entries.first(),
                severidad = SeveridadAlerta.ALTA,
                timestamp = "Hace 15 min",
                entidadReferenciaId = "PRJ-104"
            ),
            Alerta(
                id = "ALT-02",
                titulo = "Revisión ETL Power BI",
                mensaje = "Actualización de conectores SQL programada para los tableros ejecutivos de Clínica Santa María Norte.",
                tipo = TipoAlerta.entries.getOrElse(1) { TipoAlerta.entries.first() },
                severidad = SeveridadAlerta.MEDIA,
                timestamp = "Hace 1 hora",
                entidadReferenciaId = "PRJ-103"
            ),
            Alerta(
                id = "ALT-03",
                titulo = "Célula Outsourcing TI Asignada",
                mensaje = "El equipo de soporte y desarrollo ya se encuentra operativo con Financiera Andina S.A.",
                tipo = TipoAlerta.entries.last(),
                severidad = SeveridadAlerta.INFORMATIVA,
                timestamp = "Hoy, 09:00",
                entidadReferenciaId = "PRJ-102"
            )
        )
    )
    val alertas: StateFlow<List<Alerta>> = _alertas.asStateFlow()

    fun marcarComoLeida(alertaId: String) {
        _alertas.update { lista ->
            lista.map { if (it.id == alertaId) it.copy(leida = true) else it }
        }
    }
}