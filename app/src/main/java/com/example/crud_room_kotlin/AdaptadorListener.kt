package com.example.crud_room_kotlin

import com.example.crud_room_kotlin.modelo.Salud
import com.example.crud_room_kotlin.modelo.Usuario

interface AdaptadorListener {
    fun onEditItemClick(usuario: Usuario)
    fun onDeleteItemClick(usuario: Usuario)

}