package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.data.remote.dto.PostDto
import com.example.insertcode_grupojm.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    protected val _postList = MutableStateFlow<List<PostDto>>(emptyList())
    open val postList: StateFlow<List<PostDto>> = _postList.asStateFlow()

    init {
        fetchPosts()
    }

    open fun fetchPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repository.getPosts()
            } catch (e: Exception) {
                _postList.value = emptyList()
            }
        }
    }
}