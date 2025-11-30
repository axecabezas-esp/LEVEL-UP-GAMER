package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import com.example.level_up_gamer.viewmodel.SelectorImagenViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PerfilScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Usamos el ViewModel real pero con un DAO falso para controlar los datos
    private val fakeDao = FakeUsuarioDaoPerfil()
    private val usuarioViewModel = UsuarioViewModel(fakeDao)

    // El selector de imagen no tiene dependencias complejas, usamos el real
    private val selectorViewModel = SelectorImagenViewModel()

    @Test
    fun perfil_modo_invitado_muestra_elementos_por_defecto() {
        // 1. Nos aseguramos de estar deslogueados
        usuarioViewModel.logout()

        // 2. Cargar pantalla
        composeTestRule.setContent {
            PerfilScreen(
                usuarioViewModel = usuarioViewModel,
                selectorImagenViewModel = selectorViewModel
            )
        }

        // 3. Validar textos de invitado
        composeTestRule.onNodeWithText("Invitado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inicia sesión para ver tu perfil").assertIsDisplayed()

        // 4. Validar que NO aparezca el botón de cerrar sesión
        composeTestRule.onNodeWithText("Cerrar Sesión").assertDoesNotExist()
    }

    @Test
    fun perfil_modo_usuario_muestra_datos_y_boton_logout() {
        // 1. PREPARACIÓN: Simulamos un Login exitoso antes de cargar la UI
        // Usamos runBlocking porque login es suspend
        runBlocking {
            usuarioViewModel.login("test@correo.com", "123")
        }

        // 2. Cargar pantalla
        composeTestRule.setContent {
            PerfilScreen(
                usuarioViewModel = usuarioViewModel,
                selectorImagenViewModel = selectorViewModel
            )
        }

        // 3. Validar que se ven los datos del usuario definido en el FakeDao
        composeTestRule.onNodeWithText("Jugador 1").assertIsDisplayed() // Nombre
        composeTestRule.onNodeWithText("test@correo.com").assertIsDisplayed() // Correo

        // 4. Validar que aparece el botón de cerrar sesión
        composeTestRule.onNodeWithText("Cerrar Sesión").assertIsDisplayed()

        // 5. Validar que NO aparece el texto de invitado
        composeTestRule.onNodeWithText("Invitado").assertDoesNotExist()
    }

    @Test
    fun boton_cerrar_sesion_regresa_a_modo_invitado() {
        // 1. Login inicial
        runBlocking { usuarioViewModel.login("test@correo.com", "123") }

        composeTestRule.setContent {
            PerfilScreen(usuarioViewModel, selectorViewModel)
        }

        // 2. Verificar que estamos logueados
        composeTestRule.onNodeWithText("Jugador 1").assertIsDisplayed()

        // 3. Hacer clic en "Cerrar Sesión"
        composeTestRule.onNodeWithText("Cerrar Sesión").performClick()

        // Necesitamos esperar que el estado se actualice (Composer idle)
        composeTestRule.waitForIdle()

        // 4. Verificar que volvio a "Invitado"
        composeTestRule.onNodeWithText("Invitado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jugador 1").assertDoesNotExist()
    }
}

// --- FAKE DAO ---
// Este Fake siempre devuelve un usuario válido si el correo coincide,
// permitiendo que el ViewModel real haga su trabajo de actualizar el StateFlow.

class FakeUsuarioDaoPerfil : UsuarioDao {
    override suspend fun insertUsuario(usuario: Usuario) {
        // No hace nada
    }
    override suspend fun updateUsuario(usuario: Usuario) {}

    override suspend fun getUsuarioByEmail(correo: String): Usuario? {
        if (correo == "test@correo.com") {
            return Usuario(
                nombre = "Jugador 1",
                correo = "test@correo.com",
                contrasena = "123"
            )
        }
        return null
    }
}