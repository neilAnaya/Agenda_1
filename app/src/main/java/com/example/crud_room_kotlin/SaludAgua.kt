package com.example.crud_room_kotlin

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.crud_room_kotlin.Daos.DaoAgua
import com.example.crud_room_kotlin.modelo.DatabaseProvider
import com.example.crud_room_kotlin.modelo.Salud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.media.MediaPlayer
import android.content.Context

class SaludAgua : AppCompatActivity() {

    private lateinit var p1: ImageView
    private lateinit var llenarAgua: Button
    //private lateinit var reinicio: Button
    private lateinit var animacionLlenado: AnimationDrawable
    private var nivelAgua = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salud_agua)

        val mp = MediaPlayer.create(this, R.raw.beberagua)


        p1 = findViewById(R.id.p1)
        llenarAgua = findViewById(R.id.btnLlenarAgua)
        //einicio = findViewById(R.id.btnReiniciar)

        llenarAgua.setOnClickListener {
            nivelAgua++

            LlenarAguaL()
            saveWaterLevelToDatabase()
            mp.start()
            //Guardar el llenado de agua

            /*val daoAgua = DatabaseProvider.getDatabase(this).daoAgua()
            GlobalScope.launch(Dispatchers.IO){
                daoAgua.insert(Salud(nivelAgua = nivelAgua))
            }*/
            if (nivelAgua == MAX_WATER_LEVEL) {

                showCongratulationsMessage()
                //
            // showConfirmationDialog()
            }


        }

        /*reinicio.setOnClickListener {
            showConfirmationDialog()
        }*/

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
            8 -> p1.setImageResource(R.drawable.persona9)
            9 ->resetWaterLevel()

        }
    }

    private fun saveWaterLevelToDatabase() {
        val daoAgua = DatabaseProvider.getDatabase(this).daoAgua()
        GlobalScope.launch(Dispatchers.IO) {
            daoAgua.insert(Salud(nivelAgua = nivelAgua))
        }
    }

    private fun resetWaterLevel() {
        nivelAgua = 0
        LlenarAguaL()
        saveWaterLevelToDatabase()
    }

    private fun showConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("¿Desea reiniciar el nivel de llenado del muñeco?")
        alertDialogBuilder.setPositiveButton("Sí") { _, _ ->
            p1.setImageResource(R.drawable.persona1)
            resetWaterLevel()
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            // No hacer nada, simplemente cerrar el diálogo.
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showCongratulationsMessage() {
        val toast = Toast.makeText(this, "FELICITACIONES",Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val MAX_WATER_LEVEL = 8 // Nivel máximo de llenado del muñeco.
    }



}