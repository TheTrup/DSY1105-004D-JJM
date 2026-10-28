package com.example.insertcode_grupojm.data.remote

import com.example.insertcode_grupojm.model.Post
import com.example.insertcode_grupojm.model.RegisterRequest
import com.example.insertcode_grupojm.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Obtener todos los posts
    @GET("posts")
    suspend fun getPosts(): List<Post>

    // Obtener un post específico por ID mediante @Path (CodePath)
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    // Filtrar posts por usuario mediante @Query (CodePath)
    @GET("posts")
    suspend fun getPostsByUser(@Query("userId") userId: Int): List<Post>

    // Crear un nuevo post mediante @Body (CodePath)
    @POST("posts")
    suspend fun createPost(@Body newPost: Post): Post

    @POST("users") // O "register" según la API que utilices
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse
}