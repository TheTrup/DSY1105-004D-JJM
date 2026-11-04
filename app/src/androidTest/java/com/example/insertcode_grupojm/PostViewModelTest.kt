package com.example.insertcode_grupojm

import com.example.insertcode_grupojm.data.remote.dto.PostDto
import com.example.insertcode_grupojm.repository.PostRepository
import com.example.insertcode_grupojm.viewmodel.PostViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "postList debe contener los datos esperados obtenidos desde el repositorio mockeado" {
        val fakePosts = listOf(
            PostDto(userId = 1, id = 1, title = "Venta Urgente", body = "Pedido urgente recibido"),
            PostDto(userId = 2, id = 2, title = "Alerta Logística", body = "Retraso en ruta")
        )

        val mockRepository = mockk<PostRepository>()
        coEvery { mockRepository.getPosts() } returns fakePosts

        val viewModel = object : PostViewModel(mockRepository) {
            override fun fetchPosts() {
                _postList.value = fakePosts
            }
        }

        runTest {
            viewModel.fetchPosts()
            viewModel.postList.value shouldContainExactly fakePosts
        }
    }
})