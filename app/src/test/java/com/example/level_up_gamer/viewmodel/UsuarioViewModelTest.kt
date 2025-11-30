package com.example.level_up_gamer.viewmodel

import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    private lateinit var mockDao: UsuarioDao
    private lateinit var viewModel: UsuarioViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDao = mockk()
        viewModel = UsuarioViewModel(mockDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login debería ser exitoso si las credenciales son correctas`() = runTest {
        val fakeUser = Usuario(id = 1, nombre = "Test User", correo = "test@example.com", contrasena = "password123")
        coEvery { mockDao.getUsuarioByEmail("test@example.com") } returns fakeUser
        val result = viewModel.login("test@example.com", "password123")
        result shouldBe true
        viewModel.usuarioLogueado.value shouldBe fakeUser
    }

    @Test
    fun `login debería fallar si la contraseña es incorrecta`() = runTest {
        val fakeUser = Usuario(id = 1, nombre = "Test User", correo = "test@example.com", contrasena = "password123")
        coEvery { mockDao.getUsuarioByEmail("test@example.com") } returns fakeUser
        val result = viewModel.login("test@example.com", "wrongpassword")
        result shouldBe false
        viewModel.usuarioLogueado.value shouldBe null
    }

    @Test
    fun `login debería fallar si el usuario no existe`() = runTest {
        coEvery { mockDao.getUsuarioByEmail("nonexistent@example.com") } returns null
        val result = viewModel.login("nonexistent@example.com", "password123")
        result shouldBe false
        viewModel.usuarioLogueado.value shouldBe null
    }


    @Test
    fun `registrarUsuario debería llamar al método insertUsuario del DAO`() = runTest {
        coEvery { mockDao.insertUsuario(any()) } returns Unit
        viewModel.registrarUsuario("Nuevo Usuario", "nuevo@example.com", "P@ssword1")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { mockDao.insertUsuario(any()) }
    }

    @Test
    fun `logout debería limpiar el usuario logueado`() = runTest {
        val fakeUser = Usuario(id = 1, nombre = "Test User", correo = "test@example.com", contrasena = "password123")
        coEvery { mockDao.getUsuarioByEmail("test@example.com") } returns fakeUser
        viewModel.login("test@example.com", "password123")
        Assert.assertEquals(fakeUser, viewModel.usuarioLogueado.value) // Verificación previa
        viewModel.logout()
        Assert.assertNull(viewModel.usuarioLogueado.value)
    }
}
