package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.level_up_gamer.data.model.CategoriaProducto
import com.example.level_up_gamer.data.model.Producto
import com.example.level_up_gamer.viewmodel.CatalogoUiState
import com.example.level_up_gamer.viewmodel.CatalogoViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AdminProductosScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lista_muestra_productos_locales_para_editar() {
        // Given
        val fakeViewModel = FakeAdminViewModel()

        // When
        composeTestRule.setContent {
            AdminProductosScreen(
                catalogoViewModel = fakeViewModel,
                onNavigateBack = {}
            )
        }

        // Then: Verificamos que aparezcan los productos del Fake
        composeTestRule.onNodeWithText("Silla Gamer Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("$100.000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Teclado Mecánico").assertIsDisplayed()
        composeTestRule.onNodeWithText("$50.000").assertIsDisplayed()
    }

    @Test
    fun al_hacer_click_en_producto_se_abre_dialogo_edicion() {
        val fakeViewModel = FakeAdminViewModel()

        composeTestRule.setContent {
            AdminProductosScreen(
                catalogoViewModel = fakeViewModel,
                onNavigateBack = {}
            )
        }

        // Click en el primer producto (aquí solo hay uno visible, así que no falla)
        composeTestRule.onNodeWithText("Silla Gamer Test").performClick()

        // Verificamos que aparece el título del Diálogo
        composeTestRule.onNodeWithText("Editar Producto").assertIsDisplayed()

        // ✅ CORRECCIÓN AQUÍ:
        // Usamos un filtro compuesto: Texto + Propiedad de ser editable (Input)
        // Así ignoramos el texto de la tarjeta de fondo y seleccionamos solo el del diálogo.
        composeTestRule.onNode(
            hasText("Silla Gamer Test").and(hasSetTextAction())
        ).assertIsDisplayed()

        // Hacemos lo mismo para la descripción por seguridad
        composeTestRule.onNode(
            hasText("Descripción de prueba").and(hasSetTextAction())
        ).assertIsDisplayed()
    }

    @Test
    fun boton_volver_llama_al_callback_navegacion() {
        val fakeViewModel = FakeAdminViewModel()

        // 1. SOLUCIÓN: Usamos una variable simple en vez de mock()
        // Esto es inmune a fallos de configuración de librerías
        var seNavegoAtras = false

        composeTestRule.setContent {
            AdminProductosScreen(
                catalogoViewModel = fakeViewModel,
                // 2. Cuando la pantalla llame a esta función, cambiamos la variable a true
                onNavigateBack = { seNavegoAtras = true }
            )
        }

        // Buscamos el botón rojo de abajo y le damos click
        composeTestRule.onNodeWithText("Volver al Menú").performClick()

        // 3. Verificamos que la variable cambió a true
        assert(seNavegoAtras) { "La función de navegación no fue llamada" }
    }

    @Test
    fun editar_producto_actualiza_la_lista_visual() {
        val fakeViewModel = FakeAdminViewModel()

        composeTestRule.setContent {
            AdminProductosScreen(
                catalogoViewModel = fakeViewModel,
                onNavigateBack = {}
            )
        }

        // 1. Abrir diálogo
        composeTestRule.onNodeWithText("Teclado Mecánico").performClick()

        // 2. Cambiar el nombre en el TextField
        // Nota: En los dialogos a veces hay dos nodos con el mismo texto (el label y el input).
        // Usamos onAllNodes y elegimos el que es editable (el input).
        val inputNombre = composeTestRule.onAllNodesWithText("Teclado Mecánico").filter(hasSetTextAction())
        inputNombre[0].performTextReplacement("Teclado SUPER Gamer")

        // 3. Guardar
        composeTestRule.onNodeWithText("Guardar").performClick()

        // 4. Verificar que la lista cambió
        composeTestRule.onNodeWithText("Teclado SUPER Gamer").assertIsDisplayed()
        // El nombre viejo ya no debería existir
        composeTestRule.onNodeWithText("Teclado Mecánico").assertDoesNotExist()
    }
}

// --- FAKE VIEWMODEL ESPECIALIZADO ---
// --- FAKE VIEWMODEL ESPECIALIZADO ---
class FakeAdminViewModel : CatalogoViewModel() {

    init {
        // Datos iniciales (esto estaba bien)
        productosLocales = mutableListOf(
            CategoriaProducto(
                nombre = "Hardware",
                productos = listOf(
                    Producto(1, "Silla Gamer Test", "Descripción de prueba", "img1", "$100.000"),
                    Producto(2, "Teclado Mecánico", "Teclas Cherry MX", "img2", "$50.000")
                )
            )
        )
        _uiState.value = CatalogoUiState(categorias = productosLocales)
    }

    override fun cargarCatalogo() {
        // Dejamos esto vacío o simplemente recargamos lo local para evitar API
        _uiState.value = CatalogoUiState(categorias = productosLocales)
    }

    // ✅ NUEVO: Sobrescribimos la edición para que funcione en el Test
    override fun editarProductoLocal(productoEditado: Producto) {
        // 1. Actualizamos la lista local del Fake (Lógica simulada)
        val nuevasCategorias = productosLocales.map { categoria ->
            val productosActualizados = categoria.productos.map { prod ->
                if (prod.id == productoEditado.id) productoEditado else prod
            }
            categoria.copy(productos = productosActualizados)
        }.toMutableList()

        productosLocales = nuevasCategorias

        // 2. ⚠️ IMPORTANTE: Forzamos la actualización de la UI inmediatamente
        _uiState.value = CatalogoUiState(categorias = productosLocales)
    }
}