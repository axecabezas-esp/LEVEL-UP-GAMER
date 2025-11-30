package com.example.level_up_gamer.viewmodel

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `MainViewModel debe cargar la lista de noticias al iniciar`() = runTest {
        val currentState = viewModel.uiState.value
        currentState.noticias.size shouldBe 6
        currentState.noticias.first().id shouldBe 1
        currentState.noticias.first().titulo shouldBe "LevelUpGamer inaugura su primera competencia online de eSports"
    }

    @Test
    fun `MainViewModel debe contener datos correctos en la última noticia`() = runTest {
        val currentState = viewModel.uiState.value
        val lastNew = currentState.noticias.last()
        lastNew.id shouldBe 6
        lastNew.titulo shouldBe "Anunciado nuevo torneo de Valorant"
    }

    @Test
    fun `la recarga de datos debe mantener un estado consistente`() = runTest {
        val initialState = viewModel.uiState.value
        val newState = viewModel.uiState.value
        newState.noticias.size shouldBe 6
        newState shouldBe initialState
    }
}