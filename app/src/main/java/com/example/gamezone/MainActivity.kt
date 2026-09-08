package com.example.gamezone

import android.graphics.Typeface
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /* Contenedor con scroll para visualizar el reporte.
         Empecé a hacer esto antes de que se mencionara que salga la info por terminal,
         por eso en pantalla de movil no sale toda la informacion */
        val scrollView = ScrollView(this)
        val Pantalla = TextView(this).apply {
            textSize = 13f
            typeface = Typeface.MONOSPACE
            setPadding(40, 60, 40, 60)
        }
        scrollView.addView(Pantalla)
        setContentView(scrollView)

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            ejecutarSimulacionGameZone(Pantalla, scrollView)
        }
    }

    private suspend fun ejecutarSimulacionGameZone(vistaTexto: TextView, scrollView: ScrollView) {
        val salidaConsola = StringBuilder()

        fun agregarLog(mensaje: String) {
            println(mensaje)
            salidaConsola.append(mensaje).append("\n")
            vistaTexto.text = salidaConsola.toString()
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        }

        agregarLog(">>> INICIANDO SISTEMA GAMEZONE <<<")

        val sistema = SistemaGameZone(capacidadMaxima = 10)

        // Datos de prueba sugeridos
        val c1 = ConsolaClasica("CC12CD", "Sony", "PlayStation 5", TipoUsuario.SOCIO)
        val c2 = ConsolaClasica("CC99ZA", "Microsoft", "Xbox Series X", TipoUsuario.INFANTIL)
        val c3 = ConsolaModerna("CM22TO", "Nintendo", "Switch", TipoUsuario.INFANTIL)
        val c4 = ConsolaVR("VR44RG", "Meta", "Quest 3", TipoUsuario.EDUCACIONAL, tieneAccesoriosPremium = true)
        val c5 = ConsolaVR("VR77RG", "HTC", "Vive Pro", TipoUsuario.INFANTIL, tieneAccesoriosPremium = false)
        val cInvalida = ConsolaClasica("123ABC", "Sega", "Genesis", TipoUsuario.INFANTIL)

        // 1. Registro de entradas
        agregarLog("\n--- 1. Registrando Entradas ---")
        sistema.registrarEntrada(c1)
        sistema.registrarEntrada(c2)
        sistema.registrarEntrada(c3)
        sistema.registrarEntrada(c4)
        sistema.registrarEntrada(c5)
        sistema.registrarEntrada(cInvalida)

        agregarLog("\nPuestos disponibles: ${sistema.puestosDisponibles()}")

        // Registro de salidas
        agregarLog("\n--- 2. Registrando Salidas y Cobros ---")
        sistema.registrarSalida("CC12CD", 75)
        sistema.registrarSalida("CC99ZA", 180)
        sistema.registrarSalida("CM22TO", 18)
        sistema.registrarSalida("VR44RG", 120)
        sistema.registrarSalida("VR77RG", 45)
        sistema.registrarSalida("XX00XX", 60)

        // Consultas de negocio
        agregarLog("\n--- 3. Consultas de Negocio ---")
        agregarLog("Socios atendidos: ${sistema.consolasDeSocios().map { it.codigo }}")
        agregarLog("Consolas finalizadas: ${sistema.codigosConsolasFinalizadas()}")
        val maxUso = sistema.consolaMayorTiempo()
        agregarLog("Mayor tiempo de uso: ${maxUso?.codigoConsola} (${maxUso?.tiempoMinutos} min)")

        // Reporte final
        sistema.imprimirReporteCierre()
        agregarLog("\n=== Cierre de turno finalizado con exito ===")
    }
}