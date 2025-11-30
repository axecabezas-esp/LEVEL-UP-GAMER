package com.example.level_up_gamer.viewmodel

import com.example.level_up_gamer.data.model.Producto
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
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CartViewModel

    // Datos de prueba (Dummies)
    // Nota: Ajusta los parámetros según tu data class Producto real si difieren
    private val producto1 = Producto(1, "Juego Prueba 1", "Desc", "img", "$10.000")
    private val producto2 = Producto(2, "Juego Prueba 2", "Desc", "img", "$20.000")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `CartViewModel debe iniciar con carrito vacio y total en cero`() = runTest {
        val currentState = viewModel.uiState.value
        currentState.productosEnCarrito.isEmpty() shouldBe true
        // El formato puede variar ligeramente según la JVM ($0 o $ 0), pero verificamos el inicio
        currentState.total.contains("0") shouldBe true
    }

    @Test
    fun `agregarProducto debe añadir elemento a la lista y actualizar total`() = runTest {
        viewModel.agregarProducto(producto1)

        val currentState = viewModel.uiState.value
        currentState.productosEnCarrito.size shouldBe 1
        currentState.productosEnCarrito.first() shouldBe producto1
        // Verificamos que el total contenga el valor esperado (ej: $10.000)
        currentState.total.contains("10.000") shouldBe true
    }

    @Test
    fun `agregar multiples productos debe sumar el total correctamente`() = runTest {
        viewModel.agregarProducto(producto1) // 10.000
        viewModel.agregarProducto(producto2) // 20.000

        val currentState = viewModel.uiState.value
        currentState.productosEnCarrito.size shouldBe 2
        // El total debería ser 30.000
        currentState.total.contains("30.000") shouldBe true
    }

    @Test
    fun `removerProducto debe eliminar el elemento y restar el total`() = runTest {
        // Arrange (Preparar)
        viewModel.agregarProducto(producto1)
        viewModel.agregarProducto(producto2)

        // Act (Actuar)
        viewModel.removerProducto(producto1)

        // Assert (Verificar)
        val currentState = viewModel.uiState.value
        currentState.productosEnCarrito.size shouldBe 1
        currentState.productosEnCarrito.first() shouldBe producto2
        currentState.total.contains("20.000") shouldBe true
    }

    @Test
    fun `vaciarCarrito debe limpiar la lista y resetear el total`() = runTest {
        // Arrange
        viewModel.agregarProducto(producto1)
        viewModel.agregarProducto(producto2)

        // Act
        viewModel.vaciarCarrito()

        // Assert
        val currentState = viewModel.uiState.value
        currentState.productosEnCarrito.isEmpty() shouldBe true
        currentState.total.contains("0") shouldBe true
    }
}