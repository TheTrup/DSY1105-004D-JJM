package com.example.insertcode_grupojm.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.ui.components.ImagenInteligente
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()

    // Lanzador para seleccionar imagen de Galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImagenSeleccionada(uri)
    }

    // Lanzador para capturar imagen con Cámara
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitosa ->
        viewModel.onFotoCapturada(exitosa)
    }

    // Lanzador para solicitar el permiso en tiempo de ejecución
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            try {
                val uri = viewModel.crearUriParaCamara(context)
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al preparar la cámara: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara para tomar fotos", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario - Modo Guardián") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Componente visual circular
            ImagenInteligente(imagenUri = imagenUri)

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Galería
            Button(
                onClick = { galeriaLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar de Galería")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Cámara con validación previa de permiso
            OutlinedButton(
                onClick = {
                    val tienePermiso = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (tienePermiso) {
                        try {
                            val uri = viewModel.crearUriParaCamara(context)
                            camaraLauncher.launch(uri)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Foto con Cámara")
            }
        }
    }
}