package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.level_up_gamer.data.model.Noticia
import com.example.level_up_gamer.viewmodel.MainUiState
import com.example.level_up_gamer.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_muestra_titulos_principales() {
        // Usamos el Fake para evitar errores de imágenes
        val fakeViewModel = FakeMainViewModel()

        composeTestRule.setContent {
            MainScreen(mainViewModel = fakeViewModel)
        }

        composeTestRule.onNodeWithText("LEVEL-UP GAMER").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bienvenido al Mundo Gamer 🎮").assertIsDisplayed()
        composeTestRule.onNodeWithText("Noticias Importantes").assertIsDisplayed()
    }

    @Test
    fun mainScreen_muestra_tarjetas_de_noticia() {
        val fakeViewModel = FakeMainViewModel()

        composeTestRule.setContent {
            MainScreen(mainViewModel = fakeViewModel)
        }

        // 1. Verificar el título de la noticia fake
        composeTestRule.onNodeWithText("Noticia de Prueba").assertIsDisplayed()

        // 2. Verificar el contenido (descripción)
        composeTestRule.onNodeWithText("Esta es una descripción corta para el test.").assertIsDisplayed()
    }

    @Test
    fun mainScreen_muestra_multiples_noticias_con_scroll() {
        val fakeViewModel = FakeMainViewModel()

        composeTestRule.setContent {
            MainScreen(mainViewModel = fakeViewModel)
        }

        // Verificamos la primera noticia
        composeTestRule.onNodeWithText("Noticia de Prueba").assertIsDisplayed()

        // Scrolleamos para buscar la última noticia generada en el Fake
        composeTestRule.onNodeWithTag("listaNoticias")
            .performScrollToNode(hasText("Noticia Final"))

        composeTestRule.onNodeWithText("Noticia Final").assertIsDisplayed()
    }
}

// --- FAKE VIEWMODEL ---
// Hereda del original pero inyecta datos sin imágenes peligrosas
class FakeMainViewModel : MainViewModel() {

    // Sobrescribimos cargarDatos para poner nuestros propios datos de prueba
    override fun cargarDatos() {
        val noticiasFalsas = listOf(
            Noticia(1, "Noticia de Prueba", "Esta es una descripción corta para el test.", ""), // Imagen vacía para evitar crash
            Noticia(2, "Otra Noticia", "Contenido de relleno.", ""),
            Noticia(3, "Noticia Medio", "Más contenido.", ""),
            Noticia(4, "Noticia Final", "Esta está al final de la lista.", "")
        )

        // Asignamos al StateFlow (funciona porque lo cambiamos a protected)
        _uiState.value = MainUiState(noticias = noticiasFalsas)
    }
}