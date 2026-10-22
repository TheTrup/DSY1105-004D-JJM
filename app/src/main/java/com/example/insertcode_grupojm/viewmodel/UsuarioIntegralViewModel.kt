package com.example.insertcode_grupojm.viewmodel

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.data.local.AppDatabase
import com.example.insertcode_grupojm.data.local.PreferencesManager
import com.example.insertcode_grupojm.data.local.entity.UsuarioEntity
import com.example.insertcode_grupojm.model.UsuarioIntegralErrores
import com.example.insertcode_grupojm.model.UsuarioIntegralUiState
import com.example.insertcode_grupojm.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class UsuarioIntegralViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository
    var tempCameraUri: Uri? = null
        private set

    private val _uiState = MutableStateFlow(UsuarioIntegralUiState())
    val uiState: StateFlow<UsuarioIntegralUiState> = _uiState.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeExito = MutableStateFlow(false)
    val mensajeExito: StateFlow<Boolean> = _mensajeExito.asStateFlow()

    val usuariosGuardados: StateFlow<List<UsuarioEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        val pref = PreferencesManager(application)
        repository = UsuarioRepository(db.usuarioDao(), pref)

        usuariosGuardados = repository.usuarios.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            delay(1500)
            val modoGuardado = repository.modoEspecial.first()
            _uiState.update { it.copy(modoEspecial = modoGuardado) }
            _cargando.value = false
        }
    }

    fun onNombreChange(v: String) = _uiState.update { it.copy(nombre = v, errores = it.errores.copy(nombre = null)) }
    fun onCorreoChange(v: String) = _uiState.update { it.copy(correo = v, errores = it.errores.copy(correo = null)) }
    fun onClaveChange(v: String) = _uiState.update { it.copy(clave = v, errores = it.errores.copy(clave = null)) }
    fun onDireccionChange(v: String) = _uiState.update { it.copy(direccion = v, errores = it.errores.copy(direccion = null)) }
    fun onTerminosChange(v: Boolean) = _uiState.update { it.copy(aceptaTerminos = v, errores = it.errores.copy(terminos = null)) }

    fun actualizarFoto(uri: Uri?) {
        _uiState.update { it.copy(fotoUri = uri, errores = it.errores.copy(foto = null)) }
    }

    fun toggleModoEspecial() {
        viewModelScope.launch {
            val nuevoValor = !_uiState.value.modoEspecial
            repository.setModoEspecial(nuevoValor)
            _uiState.update { it.copy(modoEspecial = nuevoValor) }
            _mensajeExito.value = true
            delay(2000)
            _mensajeExito.value = false
        }
    }

    fun validarFormulario(): Boolean {
        val s = _uiState.value
        val errNombre = if (s.nombre.trim().length < 3) "Mínimo 3 caracteres" else null
        val errCorreo = if (!s.correo.contains("@")) "Correo inválido (@ obligatorio)" else null
        val errClave = if (s.clave.length < 6) "Mínimo 6 caracteres" else null
        val errDireccion = if (s.direccion.isBlank()) "Campo obligatorio" else null
        val errTerminos = if (!s.aceptaTerminos) "Debe aceptar los términos" else null
        val errFoto = if (s.fotoUri == null) "Foto requerida" else null

        val errores = UsuarioIntegralErrores(errNombre, errCorreo, errClave, errDireccion, errTerminos, errFoto)
        _uiState.update { it.copy(errores = errores) }

        return listOfNotNull(errNombre, errCorreo, errClave, errDireccion, errTerminos, errFoto).isEmpty()
    }

    fun guardarEnRoom(onSuccess: () -> Unit) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val s = _uiState.value
            val entity = UsuarioEntity(
                nombre = s.nombre,
                correo = s.correo,
                direccion = s.direccion,
                modoEspecial = s.modoEspecial,
                fotoPath = s.fotoUri?.toString(),
                fechaRegistro = Date()
            )
            repository.guardarUsuario(entity)
            onSuccess()
        }
    }

    fun crearUriTemporalCamara(): Uri {
        val context = getApplication<Application>()
        val dir = context.getExternalFilesDir(null) ?: context.cacheDir
        val archivo = File(dir, "temp_camera_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
        tempCameraUri = uri
        return uri
    }
}