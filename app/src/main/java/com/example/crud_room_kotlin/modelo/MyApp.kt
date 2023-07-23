package com.example.crud_room_kotlin.modelo
import androidx.room.Room

import android.app.Application
import com.example.crud_room_kotlin.DBPrueba

class MyApp : Application(){

    companion object{
        lateinit var database: DBPrueba
        private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext,
            DBPrueba::class.java,
            "mi-base-de-datos").build()
    }
}