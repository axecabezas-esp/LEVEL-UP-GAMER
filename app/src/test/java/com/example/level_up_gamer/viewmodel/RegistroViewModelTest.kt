package com.example.level_up_gamer.viewmodel

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

/**
 * Test para la lógica de validación de RegistroViewModel.
 * Se prueba directamente el estado del ViewModel sin necesidad de mocks, usando JUnit 4.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    private lateinit var viewModel: RegistroViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegistroViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `inicialmente, el formulario no es válido`() = runTest {
        viewModel.uiState.value.esFormularioValido shouldBe false
    }

    @Test
    fun `debería mostrar error si el nombre está vacío después de validar`() = runTest {
        viewModel.validarFormularioCompleto()
        viewModel.uiState.value.nombreError shouldBe "El nombre es obligatorio"
    }

    @Test
    fun `debería mostrar error para correos inválidos`() = runTest {
        // Caso 1: Formato de correo inválido
        viewModel.onCorreoChange("correo-invalido")
        viewModel.uiState.value.correoError shouldBe "Formato de correo no válido"

        // Caso 2: Dominio no permitido
        viewModel.onCorreoChange("test@otrodominio.com")
        viewModel.uiState.value.correoError shouldBe "Solo correos @duocuc.cl o @gmail.com"
    }

    @Test
    fun `debería mostrar error si la contraseña no cumple las reglas`() = runTest {
        viewModel.onContrasenaChange("corta")
        viewModel.uiState.value.contrasenaError shouldBe "Debe tener entre 8 y 12 caracteres"

        viewModel.onContrasenaChange("sinnumeros")
        viewModel.uiState.value.contrasenaError shouldBe "Debe incluir un número"

        viewModel.onContrasenaChange("password123")
        viewModel.uiState.value.contrasenaError shouldBe "Debe incluir una mayúscula"

        viewModel.onContrasenaChange("PASSWORD123")
        viewModel.uiState.value.contrasenaError shouldBe "Debe incluir una minúscula"

        viewModel.onContrasenaChange("Password123")
        viewModel.uiState.value.contrasenaError shouldBe "Debe incluir un caracter especial"
    }

    @Test
    fun `debería mostrar error si las contraseñas no coinciden`() = runTest {
        viewModel.onContrasenaChange("P@ssword1")
        viewModel.onConfirmarContrasenaChange("P@ssword2")
        viewModel.uiState.value.confirmarContrasenaError shouldBe "Las contraseñas no coinciden"
    }

    @Test
    fun `el formulario debería ser válido si todos los campos son correctos`() = runTest {
        // WHEN: Llenamos el formulario con datos válidos
        viewModel.onNombreChange("Usuario Test")
        viewModel.onCorreoChange("test@gmail.com")
        viewModel.onContrasenaChange("P@ssword1")
        viewModel.onConfirmarContrasenaChange("P@ssword1")

        // THEN: Verificamos que no hay errores y el formulario es válido
        val finalState = viewModel.uiState.value

        finalState.nombreError shouldBe null
        finalState.correoError shouldBe null
        finalState.contrasenaError shouldBe null
        finalState.confirmarContrasenaError shouldBe null
        finalState.esFormularioValido shouldBe true
    }
}
