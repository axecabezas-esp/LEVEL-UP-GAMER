package com.example.level_up_gamer.di

import android.content.Context
import com.example.level_up_gamer.data.AppDatabase
import com.example.level_up_gamer.data.UsuarioDao

interface AppContainer {
    val usuarioDao: UsuarioDao
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val usuarioDao: UsuarioDao by lazy {
        AppDatabase.getDatabase(context).usuarioDao()
    }
}
