package com.example.insertcode_grupojm.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.UsuarioEntity
import com.example.insertcode_grupojm.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val direccion: String = "",
    val fotoUri: Uri? = null,
    val modoEspecial: Boolean = false,
    val usuariosGuardados: List<UsuarioEntity> = emptyList(),
    val cargando: Boolean = false,
    val mensajeExito: String? = null
)

class PerfilViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    var fotoTemporalUri: Uri? = null
        private set

    init {
        observarDatos()
    }

    private fun observarDatos() {
        viewModelScope.launch {
            combine(repository.usuarios, repository.modoEspecial) { listaUsuarios, modoEspecial ->
                _uiState.update { current ->
                    current.copy(
                        usuariosGuardados = listaUsuarios,
                        modoEspecial = modoEspecial
                    )
                }
            }.collect()
        }
    }

    fun onImagenSeleccionada(uri: Uri?) {
        uri?.let { nueva -> _uiState.update { it.copy(fotoUri = nueva) } }
    }

    fun onFotoCapturada(exitosa: Boolean) {
        if (exitosa && fotoTemporalUri != null) {
            _uiState.update { it.copy(fotoUri = fotoTemporalUri) }
        }
    }

    fun crearUriParaCamara(context: Context): Uri {
        val archivo = File(context.cacheDir, "foto_perfil_${System.currentTimeMillis()}.jpg")
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, archivo)
        fotoTemporalUri = uri
        return uri
    }

    fun toggleModoEspecial(activo: Boolean) {
        viewModelScope.launch {
            repository.alternarModoEspecial(activo)
        }
    }

    fun guardarUsuarioEnRoom(nombre: String, correo: String, direccion: String) {
        viewModelScope.launch {
            val entity = UsuarioEntity(
                nombre = nombre,
                correo = correo,
                direccion = direccion,
                modoEspecial = _uiState.value.modoEspecial,
                fotoPath = _uiState.value.fotoUri?.toString()
            )
            repository.registrarUsuario(entity)
        }
    }
}