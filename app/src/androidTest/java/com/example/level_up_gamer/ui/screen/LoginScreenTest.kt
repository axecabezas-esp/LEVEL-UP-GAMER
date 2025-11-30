package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import com.example.level_up_gamer.viewmodel.UsuarioViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Usamos el Fake para tener control total sin librerías externas
    private val fakeViewModel = FakeUsuarioViewModel()

    @Test
    fun validacion_boton_se_habilita_solo_con_campos_llenos() {
        // 1. Cargar pantalla
        composeTestRule.setContent {
            LoginScreen(usuarioViewModel = fakeViewModel, onLoginSuccess = {})
        }

        // 2. Verificar estado inicial: Botón deshabilitado
        val botonLogin = composeTestRule.onNode(hasText("Iniciar Sesión") and hasClickAction())
        botonLogin.assertIsNotEnabled()

        // 3. Llenar solo correo -> Botón sigue deshabilitado
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@correo.com")
        botonLogin.assertIsNotEnabled()

        // 4. Llenar contraseña -> Botón DEBE habilitarse ahora
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        botonLogin.assertIsEnabled()
    }

    @Test
    fun login_exitoso_llama_al_callback_de_navegacion() {
        // Variable para verificar si "navegamos"
        var navegoAHome = false

        composeTestRule.setContent {
            LoginScreen(
                usuarioViewModel = fakeViewModel,
                onLoginSuccess = { navegoAHome = true } // Si tiene éxito, esto cambia a true
            )
        }

        // 1. Ingresar credenciales CORRECTAS (Según nuestro Fake definido abajo)
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("correcto@test.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")

        // 2. Hacer clic en el botón (que ya debe estar habilitado)
        composeTestRule.onNode(hasText("Iniciar Sesión") and hasClickAction()).performClick()

        // 3. Esperar que la UI reaccione (waitForIdle ayuda a que terminen las corrutinas)
        composeTestRule.waitForIdle()

        // 4. Verificar: ¿Se llamó a la función de éxito?
        assertTrue("Debería haber navegado al home tras login exitoso", navegoAHome)
    }

    @Test
    fun login_fallido_NO_llama_al_callback_de_navegacion() {
        var navegoAHome = false

        composeTestRule.setContent {
            LoginScreen(
                usuarioViewModel = fakeViewModel,
                onLoginSuccess = { navegoAHome = true }
            )
        }

        // 1. Ingresar credenciales INCORRECTAS
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("hacker@malvado.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("incorrecta")

        // 2. Intentar loguearse
        composeTestRule.onNode(hasText("Iniciar Sesión") and hasClickAction()).performClick()
        composeTestRule.waitForIdle()

        // 3. Verificar: NO debería haber navegado
        assertFalse("No debería navegar si las credenciales son incorrectas", navegoAHome)
    }
}

// --- FAKES ---
// (Puedes moverlos a un archivo 'TestFakes.kt' si quieres reutilizarlos)

class FakeUsuarioDao : UsuarioDao {
    // No necesitamos implementar lógica real de DB aquí para pruebas de UI
    override suspend fun insertUsuario(usuario: Usuario) {}
    override suspend fun getUsuarioByEmail(correo: String): Usuario? = null
    override suspend fun updateUsuario(usuario: Usuario) {}
}

class FakeUsuarioViewModel : UsuarioViewModel(FakeUsuarioDao()) {

    // Simulamos la lógica de negocio: Solo deja pasar a un usuario específico
    override suspend fun login(correo: String, contrasena: String): Boolean {
        return if (correo == "correcto@test.com" && contrasena == "password123") {
            true // Login Exitoso
        } else {
            false // Login Fallido (usuario no encontrado o pass mal)
        }
    }
}