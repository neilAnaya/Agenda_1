package com.example.crud_room_kotlin

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crud_room_kotlin.Daos.DaoAgua
import com.example.crud_room_kotlin.modelo.Salud
import com.example.crud_room_kotlin.modelo.Usuario

@Database(
    entities = [Usuario::class,Salud::class],
    version = 2, exportSchema = false
)

abstract class DBPrueba: RoomDatabase() {
    abstract fun daoUsuario(): DaoUsuario
    abstract fun daoAgua(): DaoAgua
}