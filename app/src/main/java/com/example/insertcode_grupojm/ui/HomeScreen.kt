package com.example.insertcode_grupojm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("InsertCode - Ventas y Distribución") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Bienvenida e identificación del sistema móvil
            Text(
                text = "Bienvenido al MVP Móvil",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Seleccione su perfil operativo para gestionar clientes, pedidos, ventas y rutas en terreno.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Botón de acceso simulado al flujo comercial / logístico
            Button(
                onClick = { /* Acción para conectar con el módulo correspondiente */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar al Sistema")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}