package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer.data.model.Noticia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class MainUiState(
    val noticias: List<Noticia> = emptyList()
)

open class MainViewModel : ViewModel() {

    protected val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        cargarDatos()
    }

    open fun cargarDatos() {
        _uiState.value = MainUiState(
            noticias = listOf(
                Noticia(1, "LevelUpGamer inaugura su primera competencia online de eSports", "LevelUpGamer anunció con orgullo el lanzamiento...", "esports"),
                Noticia(2, "Nueva alianza: LevelUpGamer se une a desarrolladores indie", "En un esfuerzo por apoyar a los creadores...", "juegos_indie"),
                Noticia(3, "Review del último lanzamiento AAA", "Analizamos el juego más esperado del año, ¿cumple con las expectativas?", "esports"),
                Noticia(4, "Guía de optimización para PC Gamer", "Saca el máximo provecho a tu hardware con estos sencillos trucos.", "juegos_indie"),
                Noticia(5, "Los 5 mejores juegos de mesa para empezar", "Si quieres iniciarte en los juegos de mesa, esta es nuestra selección.", "esports"),
                Noticia(6, "Anunciado nuevo torneo de Valorant", "Prepárate para la competencia, las inscripciones ya están abiertas.", "juegos_indie")
            )
        )
    }
}
