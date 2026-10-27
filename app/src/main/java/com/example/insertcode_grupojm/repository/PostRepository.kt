package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.data.remote.RetrofitClient
import com.example.insertcode_grupojm.model.Post

class PostRepository {

    private val api = RetrofitClient.apiService

    suspend fun getPosts(): List<Post> {
        return api.getPosts()
    }

    suspend fun getPostById(id: Int): Post {
        return api.getPostById(id)
    }

    suspend fun createPost(post: Post): Post {
        return api.createPost(post)
    }
}