package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.insertcode_grupojm.model.RolUsuario
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Launchers para Galería y Cámara
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImagenSeleccionada(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitosa ->
        viewModel.onFotoCapturada(exitosa)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar interactivo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (uiState.imagenUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.imagenUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar por defecto",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Galería")
                }
                Button(onClick = {
                    val uri = viewModel.crearUriParaCamara(context)
                    cameraLauncher.launch(uri)
                }) {
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = uiState.usuario.nombre, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Rol: ${uiState.usuario.rol.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Email: ${uiState.usuario.email}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Cambiar Rol de Prueba (Simulación MVP):", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = uiState.usuario.rol == RolUsuario.SUPERVISOR,
                    onClick = { viewModel.cambiarRolSimulado(RolUsuario.SUPERVISOR) },
                    label = { Text("Supervisor") }
                )
                FilterChip(
                    selected = uiState.usuario.rol == RolUsuario.VENDEDOR,
                    onClick = { viewModel.cambiarRolSimulado(RolUsuario.VENDEDOR) },
                    label = { Text("Vendedor") }
                )
                FilterChip(
                    selected = uiState.usuario.rol == RolUsuario.REPARTIDOR,
                    onClick = { viewModel.cambiarRolSimulado(RolUsuario.REPARTIDOR) },
                    label = { Text("Repartidor") }
                )
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