package com.example.crud_room_kotlin

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SaludAgua : AppCompatActivity() {

    private lateinit var p1: ImageView
    private lateinit var llenarAgua: Button
    private lateinit var animacionLlenado: AnimationDrawable
    private var nivelAgua = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salud_agua)

        p1 = findViewById(R.id.p1)
        llenarAgua = findViewById(R.id.btnLlenarAgua)

        llenarAgua.setOnClickListener {
            LlenarAguaL()
        }
    }

    private fun LlenarAguaL(){
        nivelAgua++;

        animacionLlenado = AnimationDrawable()
        when(nivelAgua){

            1 -> p1.setImageResource(R.drawable.persona2)
            2 -> p1.setImageResource(R.drawable.persona3)
            3 -> p1.setImageResource(R.drawable.persona4)
            4 -> p1.setImageResource(R.drawable.persona5)
            5 -> p1.setImageResource(R.drawable.persona6)
            6 -> p1.setImageResource(R.drawable.persona7)
            7 -> p1.setImageResource(R.drawable.persona8)
            else -> p1.setImageResource(R.drawable.persona9)

        }
    }
}