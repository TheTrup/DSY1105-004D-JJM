package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.Post
import com.example.insertcode_grupojm.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PostUiState {
    data object Cargando : PostUiState
    data class Exito(val posts: List<Post>) : PostUiState
    data class Error(val mensaje: String) : PostUiState
}

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _estado = MutableStateFlow<PostUiState>(PostUiState.Cargando)
    val estado: StateFlow<PostUiState> = _estado.asStateFlow()

    init {
        obtenerPosts()
    }

    fun obtenerPosts() {
        viewModelScope.launch {
            _estado.value = PostUiState.Cargando
            try {
                val resultado = repository.getPosts()
                _estado.value = PostUiState.Exito(resultado)
            } catch (e: Exception) {
                _estado.value = PostUiState.Error(
                    e.localizedMessage ?: "Error de conexión al obtener datos"
                )
            }
        }
    }
}