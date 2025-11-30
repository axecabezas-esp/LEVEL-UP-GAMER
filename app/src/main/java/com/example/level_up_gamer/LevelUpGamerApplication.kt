package com.example.level_up_gamer

import android.app.Application
import com.example.level_up_gamer.di.AppContainer
import com.example.level_up_gamer.di.AppDataContainer

class LevelUpGamerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
