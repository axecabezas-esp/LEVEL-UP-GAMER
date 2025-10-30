package com.example.level_up_gamer.ui.screen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.RegistroViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    registroViewModel: RegistroViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel
) {
    val uiState by registroViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val colorFondo by animateColorAsState(
        targetValue = if (uiState.esFormularioValido) Color(0xFF2E4B2E) else Color(0xFF1a1a1a),
        animationSpec = tween(durationMillis = 500),
        label = "FondoColor"
    )

    Scaffold(
        topBar = { LevelUpAppBar(title = "Formulario de Registro") },
        containerColor = Color(0xFF1a1a1a) 
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crea tu Cuenta", fontSize = 28.sp, color = Color(0xFF0DCAF0), modifier = Modifier.padding(bottom = 24.dp))

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { registroViewModel.onNombreChange(it) },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.nombreError != null,
                supportingText = { if (uiState.nombreError != null) Text(uiState.nombreError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { registroViewModel.onCorreoChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.correoError != null,
                supportingText = { if (uiState.correoError != null) Text(uiState.correoError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = { registroViewModel.onContrasenaChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.contrasenaError != null,
                supportingText = { if (uiState.contrasenaError != null) Text(uiState.contrasenaError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.confirmarContrasena,
                onValueChange = { registroViewModel.onConfirmarContrasenaChange(it) },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.confirmarContrasenaError != null,
                supportingText = { if (uiState.confirmarContrasenaError != null) Text(uiState.confirmarContrasenaError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (registroViewModel.validarFormularioCompleto()) {
                        usuarioViewModel.registrarUsuario(uiState.nombre, uiState.correo, uiState.contrasena)
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF198754))
            ) {
                Text("Registrarse", fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun textFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    unfocusedTextColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedBorderColor = Color(0xFF6CFF5A),
    focusedBorderColor = Color(0xFF0DCAF0),
    unfocusedLabelColor = Color.Gray,
    focusedLabelColor = Color(0xFF0DCAF0),
    cursorColor = Color(0xFF6CFF5A),
    errorBorderColor = Color.Red,
    errorLabelColor = Color.Red
)
