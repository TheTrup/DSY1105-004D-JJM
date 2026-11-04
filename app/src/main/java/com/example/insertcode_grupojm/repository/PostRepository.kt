package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.data.remote.RetrofitInstance
import com.example.insertcode_grupojm.data.remote.dto.PostDto

open class PostRepository {

    open suspend fun getPosts(): List<PostDto> {
        return RetrofitInstance.api.getPosts()
    }
}