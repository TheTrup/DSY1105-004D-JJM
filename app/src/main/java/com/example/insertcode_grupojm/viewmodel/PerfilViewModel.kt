package com.example.insertcode_grupojm.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

data class PerfilUiState(
    val usuario: Usuario = Usuario(
        email = "juan.perez@empresa.cl",
        rol = Rol.SUPERVISOR
    ),
    val imagenUri: Uri? = null
)

class PerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    var fotoTemporalUri: Uri? = null
        private set

    fun cambiarRolSimulado(nuevoRol: Rol) {
        _uiState.update { currentState ->
            currentState.copy(
                usuario = currentState.usuario.copy(rol = nuevoRol)
            )
        }
    }

    fun onImagenSeleccionada(uri: Uri?) {
        uri?.let { nuevaUri ->
            _uiState.update { currentState ->
                currentState.copy(imagenUri = nuevaUri)
            }
        }
    }

    fun onFotoCapturada(exitosa: Boolean) {
        if (exitosa && fotoTemporalUri != null) {
            _uiState.update { currentState ->
                currentState.copy(imagenUri = fotoTemporalUri)
            }
        }
    }

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