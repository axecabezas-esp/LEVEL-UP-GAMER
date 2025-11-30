package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val contrasenaError: String? = null,
    val confirmarContrasenaError: String? = null,
    val haComenzadoAValidar: Boolean = false,
    val esFormularioValido: Boolean = false
)

open class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    private fun startValidation() {
        if (!_uiState.value.haComenzadoAValidar) {
            _uiState.update { it.copy(haComenzadoAValidar = true) }
        }
    }

    fun onNombreChange(nombre: String) {
        startValidation()
        _uiState.update { it.copy(nombre = nombre) }
        validarCampos()
    }

    fun onCorreoChange(correo: String) {
        startValidation()
        _uiState.update { it.copy(correo = correo) }
        validarCampos()
    }

    fun onContrasenaChange(contrasena: String) {
        startValidation()
        _uiState.update { it.copy(contrasena = contrasena) }
        validarCampos()
    }

    fun onConfirmarContrasenaChange(confirmar: String) {
        startValidation()
        _uiState.update { it.copy(confirmarContrasena = confirmar) }
        validarCampos()
    }

    fun validarFormularioCompleto(): Boolean {
        startValidation()
        validarCampos()
        return _uiState.value.esFormularioValido
    }

    private fun validarCampos() {
        _uiState.update { currentState ->
            val nombreError = if (currentState.haComenzadoAValidar && currentState.nombre.isBlank()) "El nombre es obligatorio" else null

            val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
            val correoError = if(currentState.haComenzadoAValidar) {
                 when {
                    currentState.correo.isBlank() -> "El correo es obligatorio"
                    !currentState.correo.matches(emailRegex) -> "Formato de correo no válido"
                    !currentState.correo.endsWith("@duocuc.cl", true) && !currentState.correo.endsWith("@gmail.com", true) -> "Solo correos @duocuc.cl o @gmail.com"
                    else -> null
                }
            } else null

            val contrasenaError = if(currentState.haComenzadoAValidar) {
                 when {
                    currentState.contrasena.isBlank() -> "La contraseña es obligatoria"
                    currentState.contrasena.length !in 8..12 -> "Debe tener entre 8 y 12 caracteres"
                    !currentState.contrasena.any { it.isDigit() } -> "Debe incluir un número"
                    !currentState.contrasena.any { it.isUpperCase() } -> "Debe incluir una mayúscula"
                    !currentState.contrasena.any { it.isLowerCase() } -> "Debe incluir una minúscula"
                    currentState.contrasena.all { it.isLetterOrDigit() } -> "Debe incluir un caracter especial"
                    else -> null
                }
            } else null

            val confirmarError = if (currentState.haComenzadoAValidar && currentState.contrasena != currentState.confirmarContrasena) "Las contraseñas no coinciden" else null

            val esValido = nombreError == null && correoError == null && contrasenaError == null && confirmarError == null && currentState.nombre.isNotBlank()

            currentState.copy(
                nombreError = nombreError,
                correoError = correoError,
                contrasenaError = contrasenaError,
                confirmarContrasenaError = confirmarError,
                esFormularioValido = esValido
            )
        }
    }
}
