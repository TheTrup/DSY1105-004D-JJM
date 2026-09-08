package com.example.gamezone

import kotlinx.coroutines.delay

class SistemaGameZone(val capacidadMaxima: Int = 10) {

    private val puestos: List<Puesto> = List(capacidadMaxima) { Puesto(it + 1) }
    private val historialTickets: MutableList<Ticket> = mutableListOf()
    private val historialConsolas: MutableList<Consola> = mutableListOf()
    private var contadorTickets: Int = 1

    fun validarFormatoCodigo(codigo: String): Boolean {
        val regex = Regex("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{2}$")
        return regex.matches(codigo)
    }

    fun calcularTarifaFinal(consola: Consola, minutosUso: Int): Double {
        val costoBase = consola.calcularCobroBase(minutosUso)

        if (costoBase == 0.0) {
            return 0.0
        }

        // Aplicar IVA del 19%
        var montoFinal = costoBase * 1.19

        // Aplicar 50% descuento sobre monto con IVA si es usuario educacional
        if (consola.tipoUsuario == TipoUsuario.EDUCACIONAL) {
            montoFinal *= 0.50
        }

        return montoFinal
    }


    suspend fun registrarEntrada(consola: Consola): Boolean {
        //Validaciones de codigo
        if (!validarFormatoCodigo(consola.codigo)) {
            println("[ERROR REGISTRO] El codigo '${consola.codigo}' es invalido. Formato requerido: 2 letras, 2 numeros, 2 letras.")
            return false
        }

        val puestoLibre = puestos.find { it.estaDisponible() }
        if (puestoLibre == null) {
            println("[ERROR CAPACIDAD] Sistema sin capacidad disponible. Todos los puestos estan ocupados o en servicio.")
            return false
        }

        puestoLibre.asignarEstado(EstadoPuesto.EnProceso("Registrando entrada y esperando confirmacion de sensor"))
        println("[SENSOR ENTRADA] Puesto ${puestoLibre.numero}: Conectando con sensor de entrada (3s)...")
        delay(3000L)

        puestoLibre.asignarEstado(EstadoPuesto.EnJuego(consola))
        println("[ENTRADA EXITOSA] Consola ${consola.codigo} asignada al Puesto ${puestoLibre.numero}.")
        println("  -> Detalle: ${consola.mostrarDetalle()}")
        return true
    }

    suspend fun registrarSalida(codigoConsola: String, minutosUso: Int): Ticket? {
        // Validacion de puesto
        val puestoOcupado = puestos.find {
            val actual = it.estado
            actual is EstadoPuesto.EnJuego && actual.consola.codigo.equals(codigoConsola, ignoreCase = true)
        }

        if (puestoOcupado == null) {
            println("[ERROR SALIDA] Consola con codigo '$codigoConsola' no encontrada en puestos activos.")
            return null
        }

        val consola = (puestoOcupado.estado as EstadoPuesto.EnJuego).consola

        puestoOcupado.asignarEstado(EstadoPuesto.EnProceso("Calculando tarifa y procesando cobro en sensor"))
        println("[SENSOR SALIDA] Puesto ${puestoOcupado.numero}: Procesando salida de consola $codigoConsola (6.5s)...")
        delay(6500L)

        val tarifaFinal = calcularTarifaFinal(consola, minutosUso)

        // Validacion de tarifa si no es el caso permitido de 0 por menos de 20 min en consola moderna
        if (tarifaFinal <= 0.0 && !(consola is ConsolaModerna && minutosUso < 20)) {
            println("[ADVERTENCIA DATOS] Calculo invalido de tarifa ($$tarifaFinal) para consola ${consola.codigo}.")
        }

        // Emision de Ticket y registro
        val ticket = Ticket(
            numeroTicket = contadorTickets++,
            codigoConsola = consola.codigo,
            tipoConsola = consola.categoria,
            tiempoMinutos = minutosUso,
            montoPagado = tarifaFinal
        )

        historialTickets.add(ticket)
        historialConsolas.add(consola)

        // Liberar puesto
        puestoOcupado.asignarEstado(EstadoPuesto.Libre)
        println("[SALIDA COMPLETADA] Ticket #${ticket.numeroTicket} emitido. Monto pagado: $$tarifaFinal. Puesto ${puestoOcupado.numero} ahora esta Libre.")

        return ticket
    }

    // Consultas
    fun puestosDisponibles(): Int = puestos.count { it.estaDisponible() }

    fun consolasDeSocios(): List<Consola> = historialConsolas.filter { it.tipoUsuario == TipoUsuario.SOCIO }

    fun ingresoPromedio(): Double {
        if (historialTickets.isEmpty()) return 0.0
        return historialTickets.map { it.montoPagado }.average()
    }

    fun codigosConsolasFinalizadas(): List<String> = historialTickets.map { it.codigoConsola }

    fun consolaMayorTiempo(): Ticket? = historialTickets.maxByOrNull { it.tiempoMinutos }

    // Reporte de cierre de turno
    fun imprimirReporteCierre() {
        println("\n=======================================================")
        println("         GAMEZONE - REPORTE DE CIERRE DE TURNO         ")
        println("=======================================================")
        println("--- Tickets Emitidos ---")
        if (historialTickets.isEmpty()) {
            println("No se registraron sesiones completadas en el turno.")
        } else {
            historialTickets.forEach {
                println("Ticket #${it.numeroTicket} | Tipo: ${it.tipoConsola} | Codigo: ${it.codigoConsola} | Tiempo: ${it.tiempoMinutos} min | Monto: $${it.montoPagado}")
            }
        }

        val totalRecaudado = historialTickets.sumOf { it.montoPagado }
        val totalAtendidas = historialTickets.size
        val recaudacionPorTipo = historialTickets.groupBy { it.tipoConsola }
            .mapValues { entry -> entry.value.sumOf { it.montoPagado } }
        val tipoMasIngresos = recaudacionPorTipo.maxByOrNull { it.value }?.key

        println("\n--- Estadisticas Generales ---")
        println("Total recaudado en el turno: $$totalRecaudado")
        println("Cantidad de consolas atendidas: $totalAtendidas")
        println("Ingreso promedio por consola: $${ingresoPromedio()}")
        println("Tipo de consola con mayor ingreso: ${tipoMasIngresos ?: "N/A"}")
        println("Puestos disponibles al cierre: ${puestosDisponibles()} de $capacidadMaxima")

        println("\n--- Recaudacion por Tipo de Consola ---")
        CategoriaConsola.values().forEach { cat ->
            val total = recaudacionPorTipo[cat] ?: 0.0
            println(" - $cat: $$total")
        }
        println("=======================================================\n")
    }
}