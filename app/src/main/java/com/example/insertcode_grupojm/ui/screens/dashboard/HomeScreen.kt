package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.viewmodel.DashboardViewModel
import com.example.insertcode_grupojm.viewmodel.VentasViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: DashboardViewModel = viewModel(),
    ventasViewModel: VentasViewModel = viewModel(),
    rolActual: Rol = Rol.SUPERVISOR,
    onIrAPedidos: () -> Unit = {},
    onIrARutaGps: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val ventasState by ventasViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }

    val pedidos = ventasState.pedidos
    val pendientes = pedidos.count { it.estado == EstadoPedido.PENDIENTE }
    val enRuta = pedidos.count {
        it.estado == EstadoPedido.EN_PROCESO || it.estado == EstadoPedido.DESPACHADO
    }
    val entregados = pedidos.count { it.estado == EstadoPedido.ENTREGADO }
    val totalPedidos = (pendientes + enRuta + entregados).coerceAtLeast(1)

    val colorPendiente = Color(0xFFF59E0B)
    val colorEnRuta = Color(0xFF3B82F6)
    val colorEntregado = Color(0xFF10B981)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Panel Comercial & Operacional",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Indicadores clave de ventas y cumplimiento de entregas en terreno",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Botón siempre visible y activo: valida permisos al hacer clic
            Button(
                onClick = {
                    if (rolActual == Rol.VENDEDOR || rolActual == Rol.ADMIN) {
                        onIrAPedidos()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Acción restringida: el perfil ${rolActual.etiqueta} no tiene permisos para crear pedidos."
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Nuevo Pedido", fontWeight = FontWeight.SemiBold)
            }

            // Fila 1 de KPIs: Ventas Totales y Pendientes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("VENTAS TOTALES", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = formatoMoneda.format(uiState.indicadores?.ventasTotalesHoy ?: 0.0),
                            style = MaterialTheme.typography.titleLarge,
                            color = colorEntregado,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "↑ Meta: ${uiState.indicadores?.cumplimientoMetasPorcentaje ?: 0f}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("PENDIENTES", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$pendientes",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorPendiente,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Por despachar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Fila 2 de KPIs: En Ruta y Entregados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("EN RUTA", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$enRuta",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorEnRuta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Despachos activos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ENTREGADOS", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$entregados",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorEntregado,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Confirmados en terreno",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Barra de progreso segmentada
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Estado de Pedidos en Terreno",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(CircleShape)
                    ) {
                        if (pendientes > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(pendientes.toFloat() / totalPedidos)
                                    .fillMaxHeight()
                                    .background(colorPendiente)
                            )
                        }
                        if (enRuta > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(enRuta.toFloat() / totalPedidos)
                                    .fillMaxHeight()
                                    .background(colorEnRuta)
                            )
                        }
                        if (entregados > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(entregados.toFloat() / totalPedidos)
                                    .fillMaxHeight()
                                    .background(colorEntregado)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("● Pendientes ($pendientes)", style = MaterialTheme.typography.labelSmall, color = colorPendiente)
                        Text("● En Ruta ($enRuta)", style = MaterialTheme.typography.labelSmall, color = colorEnRuta)
                        Text("● Entregados ($entregados)", style = MaterialTheme.typography.labelSmall, color = colorEntregado)
                    }
                }
            }

            // Acciones Rápidas
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Acciones Rápidas", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    FilledTonalButton(
                        onClick = onIrAPedidos,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gestionar Pedidos y Catálogo", modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                    OutlinedButton(
                        onClick = onIrARutaGps,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ver Ruta GPS y Alertas de Despacho", modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}