package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Juan Pérez", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Rol: Supervisor Comercial", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Sucursal: Alameda", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { /* Cambiar rol de prueba */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar Rol de Prueba")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { /* Forzar sincronización Room / Retrofit */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sincronizar Datos Local/Remoto")
            }
        }

        Button(
            onClick = { /* Cierre de sesión */ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Cerrar Sesión")
        }
    }
}