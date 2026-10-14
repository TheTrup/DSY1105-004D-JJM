package com.example.insertcode_grupojm.repository

import android.content.Context

// Persistencia local limitada: solo recuerda el último correo usado.
// Por seguridad, la contraseña NUNCA se guarda.
class PreferenciasRepository(context: Context) {

    private val preferencias = context.applicationContext
        .getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)

    fun obtenerUltimoEmail(): String =
        preferencias.getString(CLAVE_ULTIMO_EMAIL, "") ?: ""

    fun guardarUltimoEmail(email: String) {
        preferencias.edit().putString(CLAVE_ULTIMO_EMAIL, email).apply()
    }

    private companion object {
        const val NOMBRE_ARCHIVO = "guardian_preferencias"
        const val CLAVE_ULTIMO_EMAIL = "ultimo_email"
    }
}
