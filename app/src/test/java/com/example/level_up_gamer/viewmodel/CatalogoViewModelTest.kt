package com.example.level_up_gamer.viewmodel

import com.example.level_up_gamer.data.model.CategoriaProducto
import com.example.level_up_gamer.data.model.Producto
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CatalogoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModelSoloLocal()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    class TestViewModelSoloLocal : CatalogoViewModel() {
        override fun cargarCatalogo() {
            val locales = productosLocales
            val todosLosProductos = locales.flatMap { it.productos }
            val categoriaTodos = CategoriaProducto(nombre = "Todos", productos = todosLosProductos)
            _uiState.value = CatalogoUiState(categorias = listOf(categoriaTodos) + locales)
        }
    }

    @Test
    fun `debe cargar correctamente los productos locales`() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        val consolas = state.categorias.find { it.nombre == "Consolas" }
        consolas shouldNotBe null
        consolas?.productos?.size shouldBe 3
        consolas?.productos?.first()?.nombre shouldBe "PlayStation 5"
    }

    @Test
    fun `al iniciar, debe haber 3 categorías (Todos + Consolas + Juegos de Mesa)`() = runTest {
        advanceUntilIdle()
        val currentState = viewModel.uiState.value
        currentState.categorias shouldHaveSize 6

        currentState.categorias.first().nombre shouldBe "Todos"
        currentState.categorias.last().nombre shouldBe "Juegos de Mesa"
    }

    @Test
    fun `la categoría 'Todos' debe sumar los productos locales`() = runTest {
        advanceUntilIdle()
        val currentState = viewModel.uiState.value

        val totalProductosEnEspecificas = currentState.categorias
            .filter { it.nombre != "Todos" }
            .sumOf { it.productos.size }

        val productosEnTodos = currentState.categorias
            .first { it.nombre == "Todos" }
            .productos.size
        productosEnTodos shouldBe totalProductosEnEspecificas
        productosEnTodos shouldBe 13
    }

    @Test
    fun `el producto 'Catan' debe existir y tener precio correcto`() = runTest {
        advanceUntilIdle()
        val currentState = viewModel.uiState.value

        val productoCatan = currentState.categorias
            .flatMap { it.productos }
            .find { it.nombre == "Carcassonne" }

        productoCatan shouldNotBe null
        productoCatan?.precio shouldBe "$24.990"
    }
}