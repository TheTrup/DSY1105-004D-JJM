package com.example.insertcode_grupojm.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthHelper(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    private fun findActivity(context: Context): Activity? {
        var current = context
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
    }

    suspend fun signInWithGoogle(
        serverClientId: String,
        onSuccess: (idToken: String, email: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val activity = findActivity(context)
        if (activity == null) {
            onError("No se pudo obtener la referencia de la Activity")
            return
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context = activity, request = request)
            val credential = result.credential
            if (credential is androidx.credentials.CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                onSuccess(googleIdToken.idToken, googleIdToken.id)
            } else {
                onError("Tipo de credencial no soportado")
            }
        } catch (e: GetCredentialException) {
            onError("Autenticación cancelada o fallida: ${e.localizedMessage}")
        } catch (e: Exception) {
            onError("Error desconocido: ${e.localizedMessage}")
        }
    }
}