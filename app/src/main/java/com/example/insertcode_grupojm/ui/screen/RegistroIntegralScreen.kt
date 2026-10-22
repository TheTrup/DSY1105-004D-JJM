package com.example.insertcode_grupojm.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.insertcode_grupojm.ui.components.ImagenInteligente
import com.example.insertcode_grupojm.ui.components.PremiumCard
import com.example.insertcode_grupojm.viewmodel.UsuarioIntegralViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroIntegralScreen(
    viewModel: UsuarioIntegralViewModel,
    onNavigateResumen: () -> Unit,
    onNavigateListado: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val mensajeExito by viewModel.mensajeExito.collectAsState()

    // Activity Result Launchers
    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.actualizarFoto(uri)
    }

    val camaraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        if (exito) viewModel.actualizarFoto(viewModel.tempCameraUri)
    }

    val permisoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        if (ok) camaraLauncher.launch(viewModel.crearUriTemporalCamara())
    }

    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Animación de color según modo especial
    val colorBoton by animateColorAsState(
        targetValue = if (uiState.modoEspecial) Color(0xFF2E7D32) else Color(0xFF757575),
        animationSpec = tween(500),
        label = "ColorPremium"
    )

    // Texto derivado
    val textoModo by remember(uiState.modoEspecial) {
        derivedStateOf { if (uiState.modoEspecial) "Desactivar Modo Premium" else "Activar Modo Premium" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro Módulo Integral") },
                actions = {
                    TextButton(onClick = onNavigateListado) {
                        Text("Ver Offline")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ImagenInteligente(imagenUri = uiState.fotoUri)

            uiState.errores.foto?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val tienePermiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    if (tienePermiso) {
                        camaraLauncher.launch(viewModel.crearUriTemporalCamara())
                    } else {
                        permisoLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Cámara")
                }
                OutlinedButton(onClick = { galeriaLauncher.launch("image/*") }) {
                    Text("Galería")
                }
            }

            PremiumCard(visible = uiState.modoEspecial)

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = uiState.errores.nombre != null,
                supportingText = { uiState.errores.nombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                isError = uiState.errores.correo != null,
                supportingText = { uiState.errores.correo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errores.clave != null,
                supportingText = { uiState.errores.clave?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección") },
                isError = uiState.errores.direccion != null,
                supportingText = { uiState.errores.direccion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = uiState.aceptaTerminos, onCheckedChange = viewModel::onTerminosChange)
                Text("Acepto términos y condiciones")
            }
            uiState.errores.terminos?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = { viewModel.toggleModoEspecial() },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(textoModo)
            }

            AnimatedVisibility(visible = mensajeExito) {
                Text("¡Preferencia guardada en DataStore!", color = Color(0xFF2E7D32))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.guardarEnRoom { onNavigateResumen() } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar y ver Resumen")
            }
        }
    }
}