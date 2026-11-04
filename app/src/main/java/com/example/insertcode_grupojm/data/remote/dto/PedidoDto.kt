package com.example.insertcode_grupojm.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO para la API REST de prueba de Posts (Guía 14)
data class PostDto(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)

// DTO para sincronización comercial del MVP (InsertCode)
data class PedidoDto(
    @SerializedName("id") val id: String,
    @SerializedName("cliente_id") val clienteId: String,
    @SerializedName("nombre_cliente") val nombreCliente: String,
    @SerializedName("total") val total: Double,
    @SerializedName("estado") val estado: String,
    @SerializedName("prioridad") val prioridad: String
)