package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.model.Alerta
import com.example.insertcode_grupojm.model.SeveridadAlerta
import com.example.insertcode_grupojm.ui.components.MapaRutaGpsCard
import com.example.insertcode_grupojm.ui.components.PuntoRutaSimulado
import com.example.insertcode_grupojm.viewmodel.AlertasViewModel

@Composable
fun AlertasScreen(
    viewModel: AlertasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sinLeer = uiState.alertas.count { !it.leida }

    val puntosRuta = remember {
        listOf(
            PuntoRutaSimulado(
                numero = 0,
                nombre = "Base InsertCode",
                direccion = "Centro Operaciones Santiago",
                estado = "ORIGEN",
                coordenadas = "Lat: -33.4569, Lon: -70.6483",
                xRatio = 0.16f,
                yRatio = 0.76f,
                colorPunto = Color(0xFF10B981)
            ),
            PuntoRutaSimulado(
                numero = 1,
                nombre = "Duoc UC Alameda",
                direccion = "Av. España 8, Santiago Centro",
                estado = "PENDIENTE",
                coordenadas = "Lat: -33.4489, Lon: -70.6785",
                xRatio = 0.38f,
                yRatio = 0.48f,
                colorPunto = Color(0xFFF59E0B)
            ),
            PuntoRutaSimulado(
                numero = 2,
                nombre = "Tech Chile",
                direccion = "Av. Providencia 1234, Of. 502",
                estado = "EN_RUTA",
                coordenadas = "Lat: -33.4262, Lon: -70.6148",
                xRatio = 0.64f,
                yRatio = 0.25f,
                colorPunto = Color(0xFF3B82F6)
            ),
            PuntoRutaSimulado(
                numero = 3,
                nombre = "Comercial Las Condes",
                direccion = "Av. Andrés Bello 2680, Las Condes",
                estado = "ENTREGADO",
                coordenadas = "Lat: -33.4145, Lon: -70.6012",
                xRatio = 0.83f,
                yRatio = 0.76f,
                colorPunto = Color(0xFF10B981)
            )
        )
    }

    var puntoSeleccionado by remember { mutableStateOf(puntosRuta[1]) }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título de Ruta GPS
            item {
                Text(
                    text = "Ruta GPS de Despacho",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Seguimiento de entregas asignadas en terreno (Coordenadas simuladas)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Mapa estático referencial separado en ui/components
            item {
                MapaRutaGpsCard(
                    puntos = puntosRuta,
                    puntoSeleccionado = puntoSeleccionado
                )
            }

            // Secuencia de Entregas interactiva con el mapa
            item {
                Text(
                    text = "Secuencia de Entregas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(puntosRuta.drop(1), key = { it.numero }) { parada ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { puntoSeleccionado = parada },
                    colors = CardDefaults.cardColors(
                        containerColor = if (puntoSeleccionado.numero == parada.numero)
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        else
                            MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${parada.numero}",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = parada.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = parada.direccion,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        AssistChip(
                            onClick = { puntoSeleccionado = parada },
                            label = {
                                Text(
                                    text = parada.estado,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = parada.colorPunto,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    }
                }
            }

            // Cabecera de Alertas Operativas con tu Badge original
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Alertas Operativas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
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
            }

            // Lista de alertas operativas conectada a marcarAlertaComoLeida
            items(uiState.alertas, key = { it.id }) { alerta ->
                AlertaItemCard(
                    alerta = alerta,
                    onMarcarLeida = { viewModel.marcarAlertaComoLeida(alerta.id) }
                )
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