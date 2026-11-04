package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.insertcode_grupojm.viewmodel.VentasViewModel

@Composable
fun VentasScreen(
    viewModel: VentasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.filtroBusqueda,
            onValueChange = { viewModel.onBuscarQueryChange(it) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            placeholder = { Text("Buscar cliente o ID pedido...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Text(
            text = "Pedidos Comerciales",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.pedidos) { pedido ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "${pedido.id} - ${pedido.nombreCliente}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Monto: $${pedido.total}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Prioridad: ${pedido.prioridad} | Estado: ${pedido.estado}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}