package com.example.insertcode_grupojm.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class PerfilViewModel : ViewModel() {

    // URI de la imagen seleccionada o capturada (observable por Compose)
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // URI temporal para la foto que tomará la cámara
    var fotoTemporalUri: Uri? = null
        private set

    // Actualiza la imagen cuando el usuario selecciona desde la galería
    fun onImagenSeleccionada(uri: Uri?) {
        uri?.let { _imagenUri.value = it }
    }

    // Actualiza la imagen cuando la cámara confirma la captura exitosa
    fun onFotoCapturada(exitosa: Boolean) {
        if (exitosa) {
            _imagenUri.value = fotoTemporalUri
        }
    }

    // Genera un archivo temporal y su URI seguro mediante FileProvider para la cámara
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