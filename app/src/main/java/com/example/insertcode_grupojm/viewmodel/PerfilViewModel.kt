package com.example.insertcode_grupojm.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.example.insertcode_grupojm.model.RolUsuario
import com.example.insertcode_grupojm.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

data class PerfilUiState(
    val usuario: Usuario = Usuario(
        id = "USR-001",
        nombre = "Juan Pérez",
        email = "juan.perez@empresa.cl",
        rol = RolUsuario.SUPERVISOR,
        sucursalId = "SUC-ALAMEDA"
    ),
    val imagenUri: Uri? = null
)

class PerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // URI temporal para la foto que tomará la cámara
    var fotoTemporalUri: Uri? = null
        private set

    // Cambiar rol simulado para demostraciones según perfil usuario
    fun cambiarRolSimulado(nuevoRol: RolUsuario) {
        _uiState.update { currentState ->
            currentState.copy(
                usuario = currentState.usuario.copy(rol = nuevoRol)
            )
        }
    }

    // Actualiza la imagen cuando el usuario selecciona desde la galería
    fun onImagenSeleccionada(uri: Uri?) {
        uri?.let { nuevaUri ->
            _uiState.update { currentState ->
                currentState.copy(imagenUri = nuevaUri)
            }
        }
    }

    // Actualiza la imagen cuando la cámara confirma la captura exitosa
    fun onFotoCapturada(exitosa: Boolean) {
        if (exitosa && fotoTemporalUri != null) {
            _uiState.update { currentState ->
                currentState.copy(imagenUri = fotoTemporalUri)
            }
        }
    }

    // Genera un archivo temporal y su URI seguro mediante FileProvider
    fun crearUriParaCamara(context: Context): Uri {
        val archivo = File(context.cacheDir, "foto_perfil_${System.currentTimeMillis()}.jpg")
        val authority = "${context.applicationContext.packageName}.fileprovider"

        val uri = FileProvider.getUriForFile(
            context,
            authority,
            archivo
        )
        fotoTemporalUri = uri
        return uri
    }
}