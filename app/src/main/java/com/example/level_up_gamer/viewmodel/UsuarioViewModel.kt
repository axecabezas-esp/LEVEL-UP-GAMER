package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.UsuarioDatabase
import com.example.level_up_gamer.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = UsuarioDatabase.getDatabase(application).usuarioDao()

    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena)
            usuarioDao.insertUsuario(nuevoUsuario)
        }
    }

    suspend fun login(correo: String, contrasena: String): Boolean {
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
