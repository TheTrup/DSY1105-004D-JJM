package com.example.insertcode_grupojm.ui.screens.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    loginViewModel: LoginViewModel,
    perfilViewModel: PerfilViewModel = viewModel(),
    onNavigateToPosts: () -> Unit = {},
    onNavigateToIntegral: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val perfilState by perfilViewModel.uiState.collectAsState()
    val loginState by loginViewModel.estado.collectAsState()
    val context = LocalContext.current

    // Sincroniza el rol inicial con el de la sesión activa si existe
    LaunchedEffect(loginState.usuario?.rol) {
        loginState.usuario?.rol?.let { rolActivo ->
            perfilViewModel.cambiarRolSimulado(rolActivo)
        }
    }

    val emailMostrado = loginState.usuario?.email ?: perfilState.usuario.email
    val rolMostrado = perfilState.usuario.rol

    // Launchers para Galería y Cámara
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        perfilViewModel.onImagenSeleccionada(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitosa ->
        perfilViewModel.onFotoCapturada(exitosa)
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            try {
                val uri = perfilViewModel.crearUriParaCamara(context)
                cameraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al iniciar cámara: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Avatar circular
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            if (perfilState.imagenUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(perfilState.imagenUri),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar por defecto",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Botones de Galería y Cámara con validación de permiso
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Galería")
            }
            Button(onClick = {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) {
                    try {
                        val uri = perfilViewModel.crearUriParaCamara(context)
                        cameraLauncher.launch(uri)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text("Cámara")
            }
        }

        // Tarjeta con datos de la sesión activa
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Sesión Activa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Correo: $emailMostrado", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rol operativo: ${rolMostrado.etiqueta}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Selector de rol de prueba
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cambiar Rol de Prueba (Simulación MVP):",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = rolMostrado == Rol.ADMIN,
                    onClick = { perfilViewModel.cambiarRolSimulado(Rol.ADMIN) },
                    label = { Text("Admin") }
                )
                FilterChip(
                    selected = rolMostrado == Rol.SUPERVISOR,
                    onClick = { perfilViewModel.cambiarRolSimulado(Rol.SUPERVISOR) },
                    label = { Text("Supervisor") }
                )
                FilterChip(
                    selected = rolMostrado == Rol.OPERADOR,
                    onClick = { perfilViewModel.cambiarRolSimulado(Rol.OPERADOR) },
                    label = { Text("Operador") }
                )
            }
        }

        // Accesos directos a módulos técnicos (Retrofit & Room/DataStore)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Módulos Técnicos (Evaluación)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onNavigateToPosts,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Consumo API REST (Retrofit)")
                }
                OutlinedButton(
                    onClick = onNavigateToIntegral,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Storage, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Persistencia Local (Room + DataStore)")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Cerrar Sesión
        Button(
            onClick = {
                loginViewModel.cerrarSesion()
                onCerrarSesion()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
        }
    }
}