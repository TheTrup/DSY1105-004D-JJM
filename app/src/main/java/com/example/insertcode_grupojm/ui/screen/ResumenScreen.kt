package com.example.insertcode_grupojm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insertcode_grupojm.viewmodel.UsuarioViewModel

// Resumen del registro (Guía 11): lee el mismo UsuarioViewModel que el formulario.
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onVolverAlLogin: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Resumen del Registro",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "Nombre: ${estado.nombre}")
            Text(text = "Correo: ${estado.correo}")
            Text(text = "Dirección: ${estado.direccion}")
            Text(text = "Contraseña: ${"*".repeat(estado.clave.length)}")
            Text(text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")

            Button(
                onClick = onVolverAlLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ir al login")
            }
        }
    }
}
