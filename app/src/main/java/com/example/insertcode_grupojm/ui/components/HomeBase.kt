package com.example.insertcode_grupojm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insertcode_grupojm.model.LoginUiState

// Estructura común de los tres Home. Cada rol aporta su título; el rol y el correo
// salen del LoginUiState que observa cada pantalla (sin pasar argumentos por la ruta).
@Composable
fun HomeBase(
    titulo: String,
    estado: LoginUiState,
    onCerrarSesion: () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Sesión iniciada",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Usuario: ${estado.usuario?.email ?: "sin sesión"}")
                    Text(text = "Rol: ${estado.rol?.etiqueta ?: "sin rol"}")
                }
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
