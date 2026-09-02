package com.example.variablesyfunciones

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.variablesyfunciones.databinding.ActivityMainBinding

class MainActivityGuia7 : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val nombreUsuario: String = "Ana"
    var edadUsuario: Int = 20
    var promedioNotas: Double = 6.5
    val esMayorDeEdad: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val saludo = crearSaludo(nombreUsuario, edadUsuario)
        val mayorDeEdadCalculado = calcularMayoriaEdad(edadUsuario)

        val mensajeFinal = "$saludo. Promedio: $promedioNotas. Es mayor de edad: $mayorDeEdadCalculado"
        mostrarResultado(mensajeFinal)

    }

    fun crearSaludo(nombre: String, edad: Int): String {
        return("Hola $nombre, tienes $edad años!" )
    }

    fun calcularMayoriaEdad(edad: Int): Boolean{
        return edad >= 18
    }

    fun mostrarResultado(mensaje: String): Unit{
        binding.textView.text = mensaje
    }

}