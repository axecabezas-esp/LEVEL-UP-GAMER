package com.example.level_up_gamer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "PerfilUsuario")
    data class PerfilUsuario (
        @PrimaryKey(autoGenerate = true)
        val id : Int = 0,
        val nombre : String = "",
        val correo : String = "",
        val contrasenia : String = "",
        val fechaNac : String = ""
    )

