package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.model.Alerta
import com.example.insertcode_grupojm.model.SeveridadAlerta
import com.example.insertcode_grupojm.viewmodel.AlertasViewModel

@Composable
fun AlertasScreen(
    viewModel: AlertasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sinLeer = uiState.alertas.count { !it.leida }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Alertas Operativas",
                style = MaterialTheme.typography.titleLarge
            )
            Badge(
                containerColor = if (sinLeer > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
            ) {
                Text(
                    text = if (sinLeer > 0) "$sinLeer sin leer" else "Al día",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(uiState.alertas, key = { it.id }) { alerta ->
                    AlertaItemCard(
                        alerta = alerta,
                        onMarcarLeida = { viewModel.marcarAlertaComoLeida(alerta.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertaItemCard(
    alerta: Alerta,
    onMarcarLeida: () -> Unit
) {
    val containerColor = when {
        alerta.leida -> MaterialTheme.colorScheme.surfaceVariant
        alerta.severidad == SeveridadAlerta.ALTA -> MaterialTheme.colorScheme.errorContainer
        alerta.severidad == SeveridadAlerta.MEDIA -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when {
        alerta.leida -> MaterialTheme.colorScheme.onSurfaceVariant
        alerta.severidad == SeveridadAlerta.ALTA -> MaterialTheme.colorScheme.onErrorContainer
        alerta.severidad == SeveridadAlerta.MEDIA -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = when {
                        alerta.leida -> Icons.Default.CheckCircle
                        alerta.severidad == SeveridadAlerta.ALTA -> Icons.Default.Warning
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = alerta.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = "${alerta.timestamp}${alerta.entidadReferenciaId?.let { " • Ref: $it" } ?: ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.8f)
                    )
                }
            }

            Text(
                text = alerta.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )

            if (!alerta.leida) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onMarcarLeida) {
                        Text(
                            text = "Marcar como leída",
                            color = contentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}