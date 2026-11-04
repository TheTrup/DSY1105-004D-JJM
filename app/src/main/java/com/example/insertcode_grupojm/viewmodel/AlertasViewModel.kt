package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.Alerta
import com.example.insertcode_grupojm.repository.AlertasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AlertasUiState(
    val alertas: List<Alerta> = emptyList(),
    val isLoading: Boolean = false
)

class AlertasViewModel(
    private val repository: AlertasRepository = AlertasRepository()
) : ViewModel() {

    val uiState: StateFlow<AlertasUiState> = repository.alertas
        .map { listaAlertas ->
            AlertasUiState(
                alertas = listaAlertas,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AlertasUiState(isLoading = true)
        )

    fun marcarAlertaComoLeida(alertaId: String) {
        viewModelScope.launch {
            repository.marcarComoLeida(alertaId)
        }
    }
}