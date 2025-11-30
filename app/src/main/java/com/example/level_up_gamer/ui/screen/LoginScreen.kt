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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.ui.components.LevelUpInput
import com.example.level_up_gamer.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var esFormularioValido by remember { mutableStateOf(false) }

    LaunchedEffect(correo, contrasena) {
        esFormularioValido = correo.isNotBlank() && contrasena.isNotBlank()
    }

    val colorFondo by animateColorAsState(
        targetValue = if (esFormularioValido) Color(0xFF1C2C3C) else Color(0xFF1a1a1a),
        animationSpec = tween(durationMillis = 500),
        label = "FondoColorLogin"
    )

    Scaffold(
        topBar = { LevelUpAppBar(title = "Iniciar Sesión") },
        containerColor = Color(0xFF1a1a1a)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido de Vuelta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0DCAF0), modifier = Modifier.padding(bottom = 24.dp))

            LevelUpInput(
                value = correo,
                onValueChange = { correo = it },
                label = "Correo electrónico"
            )
            Spacer(modifier = Modifier.height(16.dp))

            LevelUpInput(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = "Contraseña",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        val loginExitoso = usuarioViewModel.login(correo, contrasena)
                        if (loginExitoso) {
                            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            correo = ""
                            contrasena = ""
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = esFormularioValido, 
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF198754),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Iniciar Sesión", fontSize = 18.sp)
            }
        }
    }
}
