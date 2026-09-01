package com.example.jelomundo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class Entrada(val id: Int, val precio: Double) {
    open fun mostrarDetalle(): String = "Entrada #$id - Precio: $$precio"
}

class EntradaGeneral(id: Int, precio: Double, val sector: String = "General") : Entrada(id, precio) {
    override fun mostrarDetalle(): String = "[GENERAL] Entrada #$id | Sector: $sector | Precio: $$precio"
}

class EntradaVIP(id: Int, precio: Double, val beneficiosExtra: List<String>) : Entrada(id, precio) {
    override fun mostrarDetalle(): String = "[VIP] Entrada #$id | Beneficios: [${beneficiosExtra.joinToString()}] | Precio: $$precio"
}

sealed class EstadoValidacion {
    object Validando : EstadoValidacion()
    data class Valida(val entrada: Entrada) : EstadoValidacion()
    data class NoValida(val mensajeError: String) : EstadoValidacion()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            ejecutarEvaluacion()
        }
    }

    private suspend fun validarEntrada(idBuscado: Int, lista: List<Entrada>): EstadoValidacion {
        delay(2000L)
        val encontrada = lista.find { it.id == idBuscado }
        return if (encontrada != null) EstadoValidacion.Valida(encontrada)
        else EstadoValidacion.NoValida("ID #$idBuscado no existe.")
    }

    private suspend fun ejecutarEvaluacion() {
        val TAG = "EVALUACIONF_1"
        val entradas: List<Entrada> = listOf(
            EntradaGeneral(101, 15000.0, "Platea"),
            EntradaVIP(102, 45000.0, listOf("Acceso VIP")),
            EntradaGeneral(103, 15000.0, "Galería")
        )

        Log.d(TAG, "Total recaudado: $${entradas.sumOf { it.precio }}")
        Log.d(TAG, "Cantidad VIP: ${entradas.count { it is EntradaVIP }}")

        val res1 = validarEntrada(102, entradas)
        when (res1) {
            is EstadoValidacion.Valida -> Log.d(TAG, "Validada: ${res1.entrada.mostrarDetalle()}")
            is EstadoValidacion.NoValida -> Log.d(TAG, "Error: ${res1.mensajeError}")
            is EstadoValidacion.Validando -> Log.d(TAG, "Validando...")
        }
    }
}