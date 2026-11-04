package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.data.local.PreferencesManager
import com.example.insertcode_grupojm.data.local.dao.UsuarioDao
import com.example.insertcode_grupojm.model.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val preferencesManager: PreferencesManager
) {
    val usuarios: Flow<List<UsuarioEntity>> = usuarioDao.getUsuariosFlow()
    val modoEspecial: Flow<Boolean> = preferencesManager.modoEspecialFlow

    suspend fun registrarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertar(usuario)
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminar(usuario)
    }

    suspend fun alternarModoEspecial(activo: Boolean) {
        preferencesManager.guardarModoEspecial(activo)
    }
}