package com.example.insertcode_grupojm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.data.TurnoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TurnoViewModel(application: Application) : AndroidViewModel(application) {

    private val turnoDataStore = TurnoDataStore(application)

    // Estado reactivo: null mientras carga desde DataStore, Boolean cuando está listo
    private val _turnoActivo = MutableStateFlow<Boolean?>(null)
    val turnoActivo: StateFlow<Boolean?> = _turnoActivo

    // Estado para controlar la visibilidad del mensaje de feedback animado
    private val _mostrarFeedback = MutableStateFlow(false)
    val mostrarFeedback: StateFlow<Boolean> = _mostrarFeedback

    init {
        cargarEstadoInicial()
    }

    private fun cargarEstadoInicial() {
        viewModelScope.launch {
            // Demora simulada para ilustrar la carga asíncrona mediante CircularProgressIndicator
            delay(1200)
            _turnoActivo.value = turnoDataStore.obtenerEstadoTurno().first() ?: false
        }
    }

    fun alternarTurno() {
        viewModelScope.launch {
            val nuevoValor = !(_turnoActivo.value ?: false)

            // Persistencia en DataStore
            turnoDataStore.guardarEstadoTurno(nuevoValor)
            _turnoActivo.value = nuevoValor

            // Feedback visual temporal
            _mostrarFeedback.value = true
            delay(2000)
            _mostrarFeedback.value = false
        }
    }
}