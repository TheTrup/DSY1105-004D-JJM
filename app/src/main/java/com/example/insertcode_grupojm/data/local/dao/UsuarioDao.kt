package com.example.insertcode_grupojm.data.local.dao

import androidx.room.*
import com.example.insertcode_grupojm.model.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun getUsuariosFlow(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    fun getPorId(id: Int): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios WHERE correo LIKE '%' || :query || '%'")
    fun buscarPorCorreo(query: String): Flow<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)
}