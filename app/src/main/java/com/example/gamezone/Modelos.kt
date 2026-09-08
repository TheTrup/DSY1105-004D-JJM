package com.example.gamezone

import java.util.Date

enum class TipoUsuario {
    INFANTIL, SOCIO, EDUCACIONAL
}

enum class CategoriaConsola {
    CLASICA, MODERNA, VR
}

sealed class EstadoPuesto {
    object Libre : EstadoPuesto()
    data class EnJuego(val consola: Consola) : EstadoPuesto()
    data class EnProceso(val motivo: String) : EstadoPuesto()
    data class EnReparacion(val motivo: String) : EstadoPuesto()
}

data class Ticket(
    val numeroTicket: Int,
    val codigoConsola: String,
    val tipoConsola: CategoriaConsola,
    val tiempoMinutos: Int,
    val montoPagado: Double
)

abstract class Consola(
    val codigo: String,
    val marca: String,
    val modelo: String,
    val fechaIngreso: Date = Date(),
    val tipoUsuario: TipoUsuario
) {
    abstract val categoria: CategoriaConsola
    abstract val tarifaBasePorHora: Double

    abstract fun calcularCobroBase(minutosUso: Int): Double

    open fun mostrarDetalle(): String {
        return "[$categoria] Codigo: $codigo | $marca $modelo | Usuario: $tipoUsuario"
    }
}

class ConsolaClasica(
    codigo: String,
    marca: String,
    modelo: String,
    tipoUsuario: TipoUsuario
) : Consola(codigo, marca, modelo, Date(), tipoUsuario) {
    override val categoria = CategoriaConsola.CLASICA
    override val tarifaBasePorHora = 800.0

    override fun calcularCobroBase(minutosUso: Int): Double {
        val horas = minutosUso.toDouble() / 60.0
        var total = horas * tarifaBasePorHora
        if (tipoUsuario == TipoUsuario.SOCIO) {
            total *= 0.80
        }
        return total
    }
}

class ConsolaModerna(
    codigo: String,
    marca: String,
    modelo: String,
    tipoUsuario: TipoUsuario
) : Consola(codigo, marca, modelo, Date(), tipoUsuario) {
    override val categoria = CategoriaConsola.MODERNA
    override val tarifaBasePorHora = 1500.0

    override fun calcularCobroBase(minutosUso: Int): Double {
        if (minutosUso < 20) {
            return 0.0
        }
        val horas = minutosUso.toDouble() / 60.0
        return horas * tarifaBasePorHora
    }
}

class ConsolaVR(
    codigo: String,
    marca: String,
    modelo: String,
    tipoUsuario: TipoUsuario,
    val tieneAccesoriosPremium: Boolean
) : Consola(codigo, marca, modelo, Date(), tipoUsuario) {
    override val categoria = CategoriaConsola.VR
    override val tarifaBasePorHora = 3000.0

    override fun calcularCobroBase(minutosUso: Int): Double {
        val horas = minutosUso.toDouble() / 60.0
        var total = horas * tarifaBasePorHora
        if (tieneAccesoriosPremium) {
            total *= 1.30
        }
        return total
    }

    override fun mostrarDetalle(): String {
        val premiumStr = if (tieneAccesoriosPremium) "Si" else "No"
        return "${super.mostrarDetalle()} | Accesorios Premium: $premiumStr"
    }
}