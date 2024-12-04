package com.example.crud_room_kotlin.modelo

import android.content.Context
import androidx.room.Room
import com.example.crud_room_kotlin.DBPrueba

object DatabaseProvider {
    private var database: DBPrueba? = null

    fun getDatabase(context: Context): DBPrueba {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DBPrueba::class.java,
                "app_database"
            ).build()
        }
        return database!!
    }
}