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
import com.example.level_up_gamer.ui.components.LevelUpInput
import com.example.level_up_gamer.ui.components.textFieldColors
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

            LevelUpInput(
                value = uiState.nombre,
                onValueChange = { registroViewModel.onNombreChange(it) },
                label = "Nombre completo",
                errorMessage = uiState.nombreError
            )
            Spacer(modifier = Modifier.height(16.dp))

            LevelUpInput(
                value = uiState.correo,
                onValueChange = { registroViewModel.onCorreoChange(it) },
                label = "Correo electrónico",
                errorMessage = uiState.correoError
            )
            Spacer(modifier = Modifier.height(16.dp))

            LevelUpInput(
                value = uiState.contrasena,
                onValueChange = { registroViewModel.onContrasenaChange(it) },
                label = "Contraseña",
                errorMessage = uiState.contrasenaError,
                visualTransformation = PasswordVisualTransformation() // Solo agregas esto si es password
            )
            Spacer(modifier = Modifier.height(16.dp))

            LevelUpInput(
                value = uiState.confirmarContrasena,
                onValueChange = { registroViewModel.onConfirmarContrasenaChange(it) },
                label = "Confirmar contraseña",
                visualTransformation = PasswordVisualTransformation(),
                errorMessage = uiState.confirmarContrasenaError
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

