package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import com.example.level_up_gamer.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.update
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify


class EditProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pantalla_muestra_datos_del_fake_usuario_inicialmente() {
        // Given: Usamos nuestro Fake que ya trae datos
        val fakeViewModel = FakeEditUsuarioViewModel()

        // When: Cargamos la pantalla
        composeTestRule.setContent {
            EditProfileScreen(
                usuarioViewModel = fakeViewModel,
                onNavigateBack = {}
            )
        }

        // Then: Verificamos que se muestren los datos que definimos en el init del Fake
        composeTestRule.onNodeWithText("Gamer Fake").assertIsDisplayed()
        composeTestRule.onNodeWithText("fake@duocuc.cl").assertIsDisplayed()
    }

    @Test
    fun editar_y_guardar_actualiza_el_estado_del_fake() {
        val fakeViewModel = FakeEditUsuarioViewModel()

        composeTestRule.setContent {
            EditProfileScreen(
                usuarioViewModel = fakeViewModel,
                onNavigateBack = {}
            )
        }

        // 1. Modificamos los campos de texto
        // Usamos performTextReplacement para borrar lo anterior y escribir lo nuevo
        composeTestRule.onNodeWithText("Gamer Fake").performTextReplacement("Nuevo Nombre")
        composeTestRule.onNodeWithText("fake@duocuc.cl").performTextReplacement("nuevo@email.com")

        // 2. Guardamos
        composeTestRule.onNodeWithText("Guardar Cambios").performClick()

        // 3. Verificamos visualmente que el texto en los campos persistió (el Fake actualizó su estado)
        composeTestRule.onNodeWithText("Nuevo Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("nuevo@email.com").assertIsDisplayed()
    }

    @Test
    fun boton_volver_funciona() {
        val fakeViewModel = FakeEditUsuarioViewModel()

        // 1. EL ESPÍA: Una variable simple que empieza en falso
        var navegacionRealizada = false

        composeTestRule.setContent {
            EditProfileScreen(
                usuarioViewModel = fakeViewModel,
                // 2. LA TRAMPA: Le pasamos una función que cambia la variable a true
                onNavigateBack = { navegacionRealizada = true }
            )
        }

        // 3. ACCIÓN: Click en la flecha de volver
        composeTestRule.onNodeWithContentDescription("Volver").performClick()

        // 4. VERIFICACIÓN: Si la variable cambió a true, es que la función se llamó.
        // Usamos 'assert' nativo de Kotlin/Java
        assert(navegacionRealizada) { "La función de navegación no fue llamada" }
    }
}

// --- TU FAKE VIEWMODEL ---
// Hereda de UsuarioViewModel para usarlo en la pantalla
// Pasamos un DAO mockeado al padre porque el constructor lo exige, pero no lo usaremos.
class FakeEditUsuarioViewModel : UsuarioViewModel(
    // ✅ CAMBIO AQUÍ: En vez de mock(...), usamos un objeto "dummy" manual.
    // Esto evita que Mockito tenga que inicializarse, eliminando el error de raíz.
    object : UsuarioDao {
        override suspend fun insertUsuario(usuario: Usuario) {} // No hace nada
        override suspend fun getUsuarioByEmail(correo: String): Usuario? = null // Retorna null
        override suspend fun updateUsuario(usuario: Usuario) {} // No hace nada
    }
) {

    init {
        val usuarioInicial = Usuario(
            id = 1,
            nombre = "Gamer Fake",
            correo = "fake@duocuc.cl",
            contrasena = "123456"
        )
        // Asignamos el valor inicial
        _usuarioLogueado.value = usuarioInicial
    }

    override fun actualizarUsuario(nuevoNombre: String, nuevoCorreo: String) {
        // Actualizamos el estado localmente sin tocar la BD
        _usuarioLogueado.update { usuarioActual ->
            usuarioActual?.copy(
                nombre = nuevoNombre,
                correo = nuevoCorreo
            )
        }
    }
}