package com.example.insertcode_grupojm.data.remote

import com.example.insertcode_grupojm.data.remote.dto.PedidoDto
import com.example.insertcode_grupojm.data.remote.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Endpoints de prueba Guía 14
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): PostDto

    // Endpoints simulados para el Dashboard InsertCode
    @GET("pedidos")
    suspend fun getPedidos(): List<PedidoDto>

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: PedidoDto): PedidoDto
}