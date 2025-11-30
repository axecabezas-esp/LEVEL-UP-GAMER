package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.UsuarioDao
import com.example.level_up_gamer.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


open class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    protected val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    open val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado.asStateFlow()

    open fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena)
            usuarioDao.insertUsuario(nuevoUsuario)
        }
    }
    open fun actualizarUsuario(nuevoNombre: String, nuevoCorreo: String) {
        val usuarioActual = _usuarioLogueado.value ?: return

        viewModelScope.launch {
            val usuarioActualizado = usuarioActual.copy(
                nombre = nuevoNombre,
                correo = nuevoCorreo
            )
            usuarioDao.updateUsuario(usuarioActualizado)
            _usuarioLogueado.value = usuarioActualizado
        }
    }
    open suspend fun login(correo: String, contrasena: String): Boolean {
        val usuario = usuarioDao.getUsuarioByEmail(correo)
        return if (usuario != null && usuario.contrasena == contrasena) {
            _usuarioLogueado.value = usuario
            true
        } else {
            false
        }
    }

    fun logout() {
        _usuarioLogueado.value = null
    }
}
