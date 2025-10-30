package com.example.level_up_gamer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class ItemNavegacion(val icono: ImageVector, val etiqueta: String)
val elementosMenuLateral = listOf(
    ItemNavegacion(Icons.Default.Dashboard, "Inicio"),
    ItemNavegacion(Icons.Default.SportsEsports, "Juegos"),
    ItemNavegacion(Icons.Default.Group, "Equipos"),
    ItemNavegacion(Icons.Default.Person, "Perfil"),
    ItemNavegacion(Icons.Default.Star, "Logros")
)
