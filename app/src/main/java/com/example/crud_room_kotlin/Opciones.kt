package com.example.crud_room_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Opciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.opciones)
    }

    fun redirectToForm1(view: View){
        val intent = Intent(this, SaludAgua::class.java)
        startActivity(intent)
    }

    fun redirectToForm2(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}