package com.example.crud_room_kotlin

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.crud_room_kotlin.Daos.DaoAgua
import com.example.crud_room_kotlin.modelo.DatabaseProvider
import com.example.crud_room_kotlin.modelo.Salud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            nivelAgua++
            LlenarAguaL()

            //Guardar el llenado de agua

            val daoAgua = DatabaseProvider.getDatabase(this).daoAgua()
            GlobalScope.launch(Dispatchers.IO){
                daoAgua.insert(Salud(nivelAgua = nivelAgua))
            }
        }

        val daoAgua = DatabaseProvider.getDatabase(this).daoAgua()
        GlobalScope.launch(Dispatchers.IO) {
            val latestDoll = daoAgua.getLatestDoll()
            nivelAgua = latestDoll?.nivelAgua ?: 0
            LlenarAguaL()
        }
    }

    private fun LlenarAguaL(){


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