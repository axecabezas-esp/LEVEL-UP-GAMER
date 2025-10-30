package com.example.level_up_gamer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.level_up_gamer.model.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class UsuarioDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: UsuarioDatabase? = null

        fun getDatabase(context: Context): UsuarioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsuarioDatabase::class.java,
                    "usuario_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
