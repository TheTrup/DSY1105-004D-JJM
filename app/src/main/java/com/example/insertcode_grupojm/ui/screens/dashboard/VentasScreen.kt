package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VentasScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Registrar nueva venta o pedido */ }) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Venta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                placeholder = { Text("Buscar cliente o producto...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Text(
                text = "Historial de Pedidos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    listOf(
                        "Pedido #101 - $120.000 (Entregado)",
                        "Pedido #102 - $85.000 (En Proceso)",
                        "Pedido #103 - $210.000 (Pendiente)"
                    )
                ) { pedido ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = pedido, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Prioridad: Alta", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}