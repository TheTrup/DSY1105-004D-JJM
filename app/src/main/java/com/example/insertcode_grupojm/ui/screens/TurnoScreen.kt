package com.example.insertcode_grupojm.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.viewmodel.TurnoViewModel

@Composable
fun TurnoScreen(
    modifier: Modifier = Modifier,
    viewModel: TurnoViewModel = viewModel()
) {
    val turnoActivo by viewModel.turnoActivo.collectAsState()
    val mostrarFeedback by viewModel.mostrarFeedback.collectAsState()

    // 1. Loader mientras se recupera el estado de DataStore
    if (turnoActivo == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val activo = turnoActivo!!

        // 2. Animación de color según el estado del turno
        val colorBoton by animateColorAsState(
            targetValue = if (activo) Color(0xFF2E7D32) else Color(0xFFC62828),
            animationSpec = tween(durationMillis = 500),
            label = "ColorAnimadoTurno"
        )

        // 3. Estado derivado para el texto del botón
        val textoBoton by remember(activo) {
            derivedStateOf {
                if (activo) "Finalizar Turno / Detener Ruta" else "Iniciar Turno Operativo"
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Control de Operaciones en Terreno",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (activo) "Estado actual: EN RUTA" else "Estado actual: INACTIVO",
                style = MaterialTheme.typography.bodyMedium,
                color = if (activo) Color(0xFF2E7D32) else Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón reactivo con color animado
            Button(
                onClick = { viewModel.alternarTurno() },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = textoBoton, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Mensaje con animación de visibilidad al guardar
            AnimatedVisibility(visible = mostrarFeedback) {
                Text(
                    text = "¡Estado sincronizado y guardado en local!",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}