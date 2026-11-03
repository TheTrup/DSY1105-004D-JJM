package com.example.insertcode_grupojm.model

enum class TipoAlerta {
    LOGISTICA_RETRASO,
    PEDIDO_URGENTE,
    STOCK_CRITICO,
    DESVIACION_RUTA
}

enum class SeveridadAlerta {
    INFORMATIVA,
    MEDIA,
    ALTA
}

data class Alerta(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val tipo: TipoAlerta,
    val severidad: SeveridadAlerta,
    val timestamp: String,
    val leida: Boolean = false,
    val entidadReferenciaId: String? = null
)

data class IndicadoresDashboard(
    val ventasTotalesHoy: Double,
    val cumplimientoMetasPorcentaje: Float,
    val pedidosPendientes: Int,
    val entregasRealizadas: Int,
    val entregasPendientes: Int
)