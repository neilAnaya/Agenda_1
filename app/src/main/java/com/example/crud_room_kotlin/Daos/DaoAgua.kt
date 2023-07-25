package com.example.crud_room_kotlin.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crud_room_kotlin.modelo.Salud

@Dao
interface DaoAgua {
    @Query("Select * From aguaS ORDER BY id DESC LIMIT 1")
    suspend fun getLatestDoll(): Salud?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aguaS: Salud)
}