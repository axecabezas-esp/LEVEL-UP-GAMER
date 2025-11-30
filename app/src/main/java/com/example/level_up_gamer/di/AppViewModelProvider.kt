package com.example.level_up_gamer.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.level_up_gamer.LevelUpGamerApplication
import com.example.level_up_gamer.viewmodel.CatalogoViewModel
import com.example.level_up_gamer.viewmodel.MainViewModel
import com.example.level_up_gamer.viewmodel.RegistroViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MainViewModel()
        }
        initializer {
            CatalogoViewModel()
        }
        initializer {
            RegistroViewModel()
        }
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LevelUpGamerApplication
            UsuarioViewModel(application.container.usuarioDao)
        }
    }
}
