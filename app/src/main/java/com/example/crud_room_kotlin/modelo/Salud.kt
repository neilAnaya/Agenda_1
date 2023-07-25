package com.example.crud_room_kotlin.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "aguaS")
data class Salud(
    @PrimaryKey(autoGenerate = true)

    val id: Long = 0L,
    val nivelAgua: Int

)
