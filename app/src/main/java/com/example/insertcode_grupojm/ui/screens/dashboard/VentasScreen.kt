package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.model.EstadoPedido
import com.example.insertcode_grupojm.model.Pedido
import com.example.insertcode_grupojm.model.PrioridadPedido
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.viewmodel.VentasViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun VentasScreen(
    viewModel: VentasViewModel = viewModel(),
    rolActual: Rol = Rol.SUPERVISOR
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogoNuevoPedido by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Reglas de autorización según el perfil
    val puedeCrearPedidos = rolActual == Rol.VENDEDOR || rolActual == Rol.ADMIN
    val puedeGestionarComercial = rolActual == Rol.VENDEDOR || rolActual == Rol.ADMIN
    val puedeActualizarDistribucion = rolActual == Rol.REPARTIDOR || rolActual == Rol.ADMIN

    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Botón siempre visible y activo: valida permisos al hacer clic
            FloatingActionButton(
                onClick = {
                    if (puedeCrearPedidos) {
                        mostrarDialogoNuevoPedido = true
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Acceso denegado: el rol ${rolActual.etiqueta} no tiene permisos para registrar pedidos."
                            )
                        }
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Nuevo Pedido")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Banner informativo de permisos activos para el perfil actual
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Perfil activo: ${rolActual.etiqueta} • ${rolActual.descripcionPermisos}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(10.dp)
                )
            }

            OutlinedTextField(
                value = uiState.filtroBusqueda,
                onValueChange = viewModel::onBuscarQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                placeholder = { Text("Buscar cliente, código o ruta...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.filtroBusqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onBuscarQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                }
            )

            Text(
                text = if (rolActual == Rol.REPARTIDOR) {
                    "Rutas y Entregas Asignadas (${uiState.pedidos.size})"
                } else {
                    "Pedidos y Seguimiento (${uiState.pedidos.size})"
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 72.dp)
            ) {
                items(uiState.pedidos, key = { it.id }) { pedido ->
                    val siguienteEstadoPermitido = when {
                        pedido.estado == EstadoPedido.PENDIENTE && puedeGestionarComercial ->
                            EstadoPedido.EN_PROCESO
                        pedido.estado == EstadoPedido.EN_PROCESO && puedeActualizarDistribucion ->
                            EstadoPedido.DESPACHADO
                        pedido.estado == EstadoPedido.DESPACHADO && puedeActualizarDistribucion ->
                            EstadoPedido.ENTREGADO
                        else -> null
                    }

                    PedidoItemCard(
                        pedido = pedido,
                        formatoMoneda = formatoMoneda,
                        siguienteEstado = siguienteEstadoPermitido,
                        mostrarRutaLogistica = rolActual == Rol.REPARTIDOR || rolActual == Rol.SUPERVISOR,
                        onCambiarEstado = { nuevoEstado ->
                            viewModel.cambiarEstadoPedido(pedido.id, nuevoEstado)
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoNuevoPedido && puedeCrearPedidos) {
        NuevoPedidoDialog(
            onDismiss = { mostrarDialogoNuevoPedido = false },
            onConfirmar = { cliente, producto, cantidad, precio, prioridad ->
                viewModel.crearNuevoPedido(cliente, producto, cantidad, precio, prioridad)
                mostrarDialogoNuevoPedido = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NuevoPedidoDialog(
    onDismiss: () -> Unit,
    onConfirmar: (
        cliente: String,
        producto: String,
        cantidad: Int,
        precioUnitario: Double,
        prioridad: PrioridadPedido
    ) -> Unit
) {
    var cliente by remember { mutableStateOf("") }
    var producto by remember { mutableStateOf("Licencia Dashboard Web Sales") }
    var cantidadTexto by remember { mutableStateOf("1") }
    var precioTexto by remember { mutableStateOf("450000") }
    var prioridad by remember { mutableStateOf(PrioridadPedido.ALTA) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    val catalogoRapido = listOf(
        Triple("Licencia Dashboard Web Sales", "450000", "Dashboard Web"),
        Triple("Módulo IoT Rastreador GPS", "120000", "Rastreador GPS"),
        Triple("Terminal POS Móvil Android", "180000", "Terminal POS")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Nuevo Pedido") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = cliente,
                    onValueChange = {
                        cliente = it
                        errorMensaje = null
                    },
                    label = { Text("Nombre del Cliente") },
                    placeholder = { Text("Ej. Duoc UC Sede Alameda") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Selección rápida de producto:", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    catalogoRapido.forEach { (nombreCompleto, precioDefecto, etiqueta) ->
                        FilterChip(
                            selected = producto == nombreCompleto,
                            onClick = {
                                producto = nombreCompleto
                                precioTexto = precioDefecto
                            },
                            label = { Text(etiqueta) }
                        )
                    }
                }

                OutlinedTextField(
                    value = producto,
                    onValueChange = {
                        producto = it
                        errorMensaje = null
                    },
                    label = { Text("Producto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cantidadTexto,
                        onValueChange = { cantidadTexto = it.filter { c -> c.isDigit() } },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = precioTexto,
                        onValueChange = { precioTexto = it.filter { c -> c.isDigit() } },
                        label = { Text("Precio Unit.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Prioridad:", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PrioridadPedido.entries.forEach { nivel ->
                        FilterChip(
                            selected = prioridad == nivel,
                            onClick = { prioridad = nivel },
                            label = { Text(nivel.name) }
                        )
                    }
                }

                errorMensaje?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cant = cantidadTexto.toIntOrNull() ?: 0
                    val prec = precioTexto.toDoubleOrNull() ?: 0.0
                    when {
                        cliente.isBlank() -> errorMensaje = "Ingresa el nombre del cliente"
                        producto.isBlank() -> errorMensaje = "Ingresa el nombre del producto"
                        cant <= 0 -> errorMensaje = "La cantidad debe ser mayor a 0"
                        prec <= 0.0 -> errorMensaje = "El precio debe ser mayor a 0"
                        else -> onConfirmar(cliente, producto, cant, prec, prioridad)
                    }
                }
            ) {
                Text("Guardar Pedido")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun PedidoItemCard(
    pedido: Pedido,
    formatoMoneda: NumberFormat,
    siguienteEstado: EstadoPedido?,
    mostrarRutaLogistica: Boolean,
    onCambiarEstado: (EstadoPedido) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${pedido.id} • ${pedido.nombreCliente}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatoMoneda.format(pedido.total),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Fecha: ${pedido.fechaCreacion} • Ítems: ${pedido.items.sumOf { it.cantidad }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (mostrarRutaLogistica) {
                Text(
                    text = "Ruta asignada: Zona Metropolitana • Destino: ${pedido.nombreCliente}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("${pedido.estado.name} (${pedido.prioridad.name})") }
                )

                if (siguienteEstado != null) {
                    FilledTonalButton(
                        onClick = { onCambiarEstado(siguienteEstado) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Avanzar a ${siguienteEstado.name}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}