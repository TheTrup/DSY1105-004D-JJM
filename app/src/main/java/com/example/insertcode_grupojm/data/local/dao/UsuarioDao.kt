package com.example.insertcode_grupojm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.insertcode_grupojm.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun getUsuariosFlow(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun getPorId(id: Long): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity): Int

    @Query("SELECT * FROM usuarios WHERE correo LIKE '%' || :query || '%'")
    fun buscarPorCorreo(query: String): Flow<List<UsuarioEntity>>
}