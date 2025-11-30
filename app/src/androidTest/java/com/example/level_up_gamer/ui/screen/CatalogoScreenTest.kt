package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.level_up_gamer.data.model.CategoriaProducto
import com.example.level_up_gamer.data.model.Producto
import com.example.level_up_gamer.viewmodel.CatalogoUiState
import com.example.level_up_gamer.viewmodel.CatalogoViewModel
import org.junit.Rule
import org.junit.Test

class CatalogoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun catalogo_muestra_productos_con_precio() {
        val fakeViewModel = FakeCatalogoViewModel()
        composeTestRule.setContent { CatalogoScreen(catalogoViewModel = fakeViewModel) }

        composeTestRule.onNodeWithText("PlayStation Fake").assertIsDisplayed()
        composeTestRule.onNodeWithText("$100.000", substring = true).assertIsDisplayed()

        composeTestRule.onNodeWithTag("listaCatalogo").performScrollToNode(hasText("Juego Fake"))
        composeTestRule.onNodeWithText("Juego Fake").assertIsDisplayed()
        composeTestRule.onNodeWithText("$20.000", substring = true).assertIsDisplayed()
    }

    @Test
    fun botones_agregar_al_carrito_son_visibles() {
        val fakeViewModel = FakeCatalogoViewModel()
        composeTestRule.setContent { CatalogoScreen(catalogoViewModel = fakeViewModel) }

        composeTestRule.onAllNodesWithText("Agregar al Carrito").onFirst().assertIsDisplayed()
    }

    @Test
    fun catalogo_muestra_descripcion_de_productos() {
        val fakeViewModel = FakeCatalogoViewModel()
        composeTestRule.setContent { CatalogoScreen(catalogoViewModel = fakeViewModel) }
        composeTestRule.onNodeWithText("Consola de prueba para tests").assertIsDisplayed()
        composeTestRule.onNodeWithTag("listaCatalogo")
            .performScrollToNode(hasText("Juego de estrategia"))

        composeTestRule.onNodeWithText("Juego de estrategia").assertIsDisplayed()
    }

    @Test
    fun categorias_se_muestran_como_titulos() {
        val fakeViewModel = FakeCatalogoViewModel()
        composeTestRule.setContent { CatalogoScreen(catalogoViewModel = fakeViewModel) }
        composeTestRule.onNodeWithText("Ofertas Especiales").assertIsDisplayed()
    }
    class FakeEmptyCatalogoViewModel : CatalogoViewModel() {
        override fun cargarCatalogo() {
            _uiState.value = CatalogoUiState(categorias = emptyList())
        }
    }
    @Test
    fun catalogo_vacio_muestra_solo_titulo_principal() {
        val emptyViewModel = FakeEmptyCatalogoViewModel()

        composeTestRule.setContent {
            CatalogoScreen(catalogoViewModel = emptyViewModel)
        }
        composeTestRule.onNodeWithText("🕹️ Nuestros Productos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Agregar al Carrito").assertDoesNotExist()
        composeTestRule.onNodeWithText("PlayStation 5").assertDoesNotExist()
    }
}

class FakeCatalogoViewModel : CatalogoViewModel() {
    init {
        val productosFalsos = listOf(
            Producto(1, "PlayStation Fake", "Consola de prueba para tests", "", "$100.000"),
            Producto(2, "Juego Fake", "Juego de estrategia", "", "$20.000")
        )
        val categoriaPrueba = CategoriaProducto("Ofertas Especiales", productosFalsos)

        _uiState.value = CatalogoUiState(categorias = listOf(categoriaPrueba))
    }

    override fun cargarCatalogo() {
    }
}
