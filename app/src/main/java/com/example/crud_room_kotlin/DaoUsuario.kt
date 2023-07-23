package com.example.crud_room_kotlin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoUsuario {

    // Método para obtener todos los usuarios almacenados en la tabla "usuarios"
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): MutableList<Usuario>

    // Método para agregar un nuevo usuario a la tabla "usuarios"
    @Insert
    suspend fun agregarUsuario(usuario: Usuario)

    // Método para actualizar el campo "pais" de un usuario en la tabla "usuarios" según su nombre de usuario
    @Query("UPDATE usuarios set pais=:pais WHERE usuario=:usuario")
    suspend fun actualizarUsuario(usuario: String, pais: String)

    // Método para eliminar un usuario de la tabla "usuarios" según su nombre de usuario
    @Query("DELETE FROM usuarios WHERE usuario=:usuario")
    suspend fun borrarUsuario(usuario: String)


}