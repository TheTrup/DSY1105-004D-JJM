package com.example.insertcode_grupojm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.insertcode_grupojm.ui.components.ImagenInteligente
import com.example.insertcode_grupojm.viewmodel.UsuarioIntegralViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenIntegralScreen(
    viewModel: UsuarioIntegralViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen del Registro") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImagenInteligente(imagenUri = uiState.fotoUri)

            Text("Nombre: ${uiState.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("Correo: ${uiState.correo}", style = MaterialTheme.typography.bodyLarge)
            Text("Dirección: ${uiState.direccion}", style = MaterialTheme.typography.bodyLarge)
            Text(
                "Modo: ${if (uiState.modoEspecial) "Premium" else "Básico"}",
                color = if (uiState.modoEspecial) Color(0xFF2E7D32) else Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Registro")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoOfflineScreen(
    viewModel: UsuarioIntegralViewModel,
    onVolver: () -> Unit
) {
    val usuarios by viewModel.usuariosGuardados.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Usuarios Offline (ROOM)") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (usuarios.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay usuarios registrados en Room")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(usuarios) { u ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ImagenInteligente(imagenUri = u.fotoPath?.toUri(), tamano = 60.dp)
                                Column {
                                    Text(u.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(u.correo, style = MaterialTheme.typography.bodyMedium)
                                    Text("Modo: ${if (u.modoEspecial) "Premium" else "Básico"}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}