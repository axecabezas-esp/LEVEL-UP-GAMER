package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import com.example.level_up_gamer.viewmodel.RegistroViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Usamos el RegistroViewModel REAL porque solo tiene lógica de negocio (sin DB),
    // así probamos que las validaciones de tu UI reaccionan a tu lógica real.
    private val registroViewModel = RegistroViewModel()

    // Usamos el Fake para UsuarioViewModel (que sí toca base de datos)
    private val fakeUsuarioViewModel = FakeUsuarioViewModelRegistro()

    @Test
    fun elementos_iniciales_se_muestran_correctamente() {
        composeTestRule.setContent {
            RegistroScreen(
                registroViewModel = registroViewModel,
                usuarioViewModel = fakeUsuarioViewModel
            )
        }

        // Verificamos títulos y campos vacíos
        composeTestRule.onNodeWithText("Crea tu Cuenta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre completo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrarse").assertIsDisplayed()
    }

    @Test
    fun validacion_correo_muestra_error_si_dominio_es_incorrecto() {
        composeTestRule.setContent {
            RegistroScreen(registroViewModel, fakeUsuarioViewModel)
        }

        // 1. Ingresar correo inválido (no es gmail ni duocuc)
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@yahoo.com")

        // 2. Forzar validación (tu lógica valida al escribir o al dar click)
        // Escribimos algo en otro campo para asegurar que el focus cambie o la UI se actualice
        composeTestRule.onNodeWithText("Nombre completo").performTextInput("Juan")

        // 3. Verificar que aparece el mensaje de error específico definido en tu ViewModel
        composeTestRule.onNodeWithText("Solo correos @duocuc.cl o @gmail.com").assertIsDisplayed()
    }

    @Test
    fun validacion_contrasena_muestra_error_si_es_muy_simple() {
        composeTestRule.setContent {
            RegistroScreen(registroViewModel, fakeUsuarioViewModel)
        }

        // 1. Contraseña sin mayúsculas, ni números, ni especiales
        composeTestRule.onNodeWithText("Contraseña").performTextInput("simple")

        // 2. Verificar error de longitud (tu VM pide entre 8 y 12)
        composeTestRule.onNodeWithText("Debe tener entre 8 y 12 caracteres").assertIsDisplayed()
    }

    @Test
    fun validacion_contrasenas_deben_coincidir() {
        composeTestRule.setContent {
            RegistroScreen(registroViewModel, fakeUsuarioViewModel)
        }

        // 1. Ingresar contraseña válida
        composeTestRule.onNodeWithText("Contraseña").performTextInput("Pass1234$")

        // 2. Ingresar confirmación DIFERENTE
        composeTestRule.onNodeWithText("Confirmar contraseña").performTextInput("Pass1234#")

        // 3. Verificar error de coincidencia
        composeTestRule.onNodeWithText("Las contraseñas no coinciden").assertIsDisplayed()
    }

    @Test
    fun registro_exitoso_llama_a_crear_usuario() {
        composeTestRule.setContent {
            RegistroScreen(registroViewModel, fakeUsuarioViewModel)
        }

        // 1. Llenar formulario CORRECTAMENTE para pasar todas las reglas del ViewModel
        composeTestRule.onNodeWithText("Nombre completo").performTextInput("Gamer Pro")
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("gamer@gmail.com")
        // Reglas: 8-12 chars, mayúscula, minúscula, número, especial
        composeTestRule.onNodeWithText("Contraseña").performTextInput("Gamer123$")
        composeTestRule.onNodeWithText("Confirmar contraseña").performTextInput("Gamer123$")

        // 2. Click en Registrarse
        composeTestRule.onNodeWithText("Registrarse").performClick()
        composeTestRule.waitForIdle()

        // 3. Verificar que se llamó al método del ViewModel
        assertTrue("Se debería haber llamado a registrarUsuario", fakeUsuarioViewModel.usuarioRegistrado)
    }
}

// --- FAKES ESPECÍFICOS PARA ESTE TEST ---

// Fake del DAO (necesario para instanciar el ViewModel padre)
class FakeUsuarioDaoRegistro : UsuarioDao {
    override suspend fun insertUsuario(usuario: Usuario) {}
    override suspend fun getUsuarioByEmail(correo: String): Usuario? = null
    override suspend fun updateUsuario(usuario: Usuario) {}
}

// Fake del ViewModel para espiar si se registra el usuario
class FakeUsuarioViewModelRegistro : UsuarioViewModel(FakeUsuarioDaoRegistro()) {
    var usuarioRegistrado = false

    // Sobrescribimos el método real para no usar corrutinas complejas ni DB real
    override fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        usuarioRegistrado = true
    }
}