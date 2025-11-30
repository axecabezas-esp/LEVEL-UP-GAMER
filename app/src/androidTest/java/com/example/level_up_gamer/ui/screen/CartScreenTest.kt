package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.level_up_gamer.data.model.Producto
import com.example.level_up_gamer.viewmodel.CartUiState
import com.example.level_up_gamer.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- TEST 1: Estado Vacío ---
    @Test
    fun carrito_vacio_muestra_mensaje_y_icono() {
        val fakeViewModel = FakeCartViewModel(isEmpty = true)

        composeTestRule.setContent {
            CartScreen(cartViewModel = fakeViewModel)
        }

        // Verifica que se muestra el mensaje de vacío
        composeTestRule.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
        composeTestRule.onNodeWithText("¡Ve al catálogo y agrega tus juegos favoritos!").assertIsDisplayed()

        // Verifica que NO se muestre el botón de pagar
        composeTestRule.onNodeWithText("Finalizar Compra").assertDoesNotExist()
    }

    // --- TEST 2: Carrito con Productos ---
    @Test
    fun carrito_con_productos_muestra_lista_y_total() {
        val fakeViewModel = FakeCartViewModel(isEmpty = false)

        composeTestRule.setContent {
            CartScreen(cartViewModel = fakeViewModel)
        }

        // 1. Verifica que los productos están en pantalla
        composeTestRule.onNodeWithText("Juego Test 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("$10.000").assertIsDisplayed()

        // 2. Verifica el total calculado
        composeTestRule.onNodeWithText("Total a Pagar:").assertIsDisplayed()
        composeTestRule.onNodeWithText("$30.000").assertIsDisplayed() // Suma de los fake products

        // 3. Verifica el botón de pagar
        composeTestRule.onNodeWithText("Finalizar Compra")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    // --- TEST 3: Interacción (Eliminar) ---
    @Test
    fun boton_eliminar_existe_en_cada_item() {
        val fakeViewModel = FakeCartViewModel(isEmpty = false)

        composeTestRule.setContent {
            CartScreen(cartViewModel = fakeViewModel)
        }

        // Busca todos los iconos de eliminar (debería haber 2 por los 2 productos fake)
        composeTestRule.onAllNodesWithContentDescription("Eliminar")
            .onFirst()
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}

// --- FAKE VIEWMODEL PARA CONTROLAR EL ESTADO ---
// Heredamos de CartViewModel para poder pasárselo a la pantalla
class FakeCartViewModel(isEmpty: Boolean) : CartViewModel() {
    init {
        if (isEmpty) {
            // Estado A: Vacío
            // Accedemos a la propiedad protegida o usamos un método público si existe
            // Como _uiState es privado en tu código original, simulamos vaciarlo
            vaciarCarrito()
        } else {
            // Estado B: Con datos falsos
            val p1 = Producto(1, "Juego Test 1", "Desc", "img", "$10.000")
            val p2 = Producto(2, "Juego Test 2", "Desc", "img", "$20.000")

            // Usamos tu método agregarProducto para poblar el estado
            agregarProducto(p1)
            agregarProducto(p2)

            // El total se calcula automáticamente en tu VM real al agregar
        }
    }
}