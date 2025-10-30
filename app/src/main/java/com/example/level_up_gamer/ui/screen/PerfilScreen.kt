package com.example.level_up_gamer.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.SelectorImagenViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuarioViewModel: UsuarioViewModel,
    selectorImagenViewModel: SelectorImagenViewModel = viewModel()
) {
    val usuario by usuarioViewModel.usuarioLogueado.collectAsState()
    val uriImagen = selectorImagenViewModel.uriImagen

    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectorImagenViewModel.asignarUriImagen(uri?.toString())
        }
    )

    Scaffold(
        topBar = {
            LevelUpAppBar(title = "Perfil de Usuario")
        },
        containerColor = Color(0xFF1a1a1a)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (usuario != null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .border(2.dp, Color(0xFF6CFF5A), CircleShape)
                        .clickable { lanzadorGaleria.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (uriImagen != null) {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(uriImagen)),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Toca para\ncambiar imagen", color = Color.White, textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = usuario!!.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = usuario!!.correo, fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { usuarioViewModel.logout() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
                ) {
                    Text("Cerrar Sesión", fontSize = 18.sp)
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Icono de perfil", modifier = Modifier.size(80.dp), tint = Color.Gray)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Invitado", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Inicia sesión para ver tu perfil", fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}
