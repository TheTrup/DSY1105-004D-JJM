package com.example.insertcode_grupojm.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para declarar DataStore a nivel de contexto
val Context.dataStore by preferencesDataStore(name = "preferencias_operador")

class TurnoDataStore(private val context: Context) {

    // Clave para guardar el estado del turno/ruta en terreno
    private val ESTADO_TURNO_ACTIVO = booleanPreferencesKey("turno_activo")

    // Guardar el estado en DataStore de manera asíncrona
    suspend fun guardarEstadoTurno(activo: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_TURNO_ACTIVO] = activo
        }
    }

    // Obtener el estado como flujo reactivo Flow
    fun obtenerEstadoTurno(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_TURNO_ACTIVO]
        }
    }
}