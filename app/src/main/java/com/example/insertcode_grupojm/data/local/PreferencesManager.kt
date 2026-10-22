package com.example.insertcode_grupojm.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStorePreferences by preferencesDataStore(name = "preferencias_usuario")

class PreferencesManager(private val context: Context) {

    private val MODO_ESPECIAL_KEY = booleanPreferencesKey("modo_especial")

    val modoEspecialFlow: Flow<Boolean> = context.dataStorePreferences.data.map { preferences ->
        preferences[MODO_ESPECIAL_KEY] ?: false
    }

    suspend fun guardarModoEspecial(activo: Boolean) {
        context.dataStorePreferences.edit { preferences ->
            preferences[MODO_ESPECIAL_KEY] = activo
        }
    }
}