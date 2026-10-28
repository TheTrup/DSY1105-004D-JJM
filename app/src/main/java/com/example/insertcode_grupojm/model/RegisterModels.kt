package com.example.insertcode_grupojm.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("message") val message: String? = null
)