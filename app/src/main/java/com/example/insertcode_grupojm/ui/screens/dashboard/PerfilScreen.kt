package com.example.insertcode_grupojm.ui.screens.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.insertcode_grupojm.ui.components.ImagenInteligente
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var nombreInput by remember { mutableStateOf("") }
    var correoInput by remember { mutableStateOf("") }
    var direccionInput by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onImagenSeleccionada(uri) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitosa -> viewModel.onFotoCapturada(exitosa) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ImagenInteligente(fotoUri = uiState.fotoUri)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Galería")
                }
                Button(onClick = {
                    val uri = viewModel.crearUriParaCamara(context)
                    cameraLauncher.launch(uri)
                }) {
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modo Especial (DataStore):")
                Switch(
                    checked = uiState.modoEspecial,
                    onCheckedChange = { viewModel.toggleModoEspecial(it) }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Registro Local de Usuario (Room)", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = nombreInput,
                onValueChange = { nombreInput = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = correoInput,
                onValueChange = { correoInput = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = direccionInput,
                onValueChange = { direccionInput = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.guardarUsuarioEnRoom(nombreInput, correoInput, direccionInput)
                    nombreInput = ""
                    correoInput = ""
                    direccionInput = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar en DB Room")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Usuarios Guardados en DB Local:", style = MaterialTheme.typography.titleSmall)
        }

        items(uiState.usuariosGuardados) { usuario ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(usuario.nombre, style = MaterialTheme.typography.bodyLarge)
                    Text(usuario.correo, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}