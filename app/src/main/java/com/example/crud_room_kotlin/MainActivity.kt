package com.example.crud_room_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.crud_room_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import com.example.crud_room_kotlin.modelo.Usuario


import java.util.Calendar
import android.widget.EditText
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.text.SimpleDateFormat
import android.content.Context
import java.util.Locale
import android.app.PendingIntent
import android.app.AlarmManager

import android.util.Log




class MainActivity : AppCompatActivity(), AdaptadorListener {

    lateinit var binding: ActivityMainBinding

    var listaUsuarios: MutableList<Usuario> = mutableListOf()

    lateinit var adatador: AdaptadorUsuarios

    lateinit var room: DBPrueba

    //lateinit var usuario: Usuario
    //private lateinit var usuario: Usuario
    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)

        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()

        obtenerUsuarios(room)


        binding.btnAddUpdate.setOnClickListener {
            if(binding.etUsuario.text.isNullOrEmpty() || binding.etPais.text.isNullOrEmpty()) {
                Toast.makeText(this, "DEBES LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.btnAddUpdate.text.equals("agregar")) {
                usuario = Usuario(
                    binding.etUsuario.text.toString().trim(),
                    binding.etPais.text.toString().trim()
                )

                agregarUsuario(room, usuario)
            } else if(binding.btnAddUpdate.text.equals("actualizar")) {
                usuario.pais = binding.etPais.text.toString().trim()

                actualizarUsuario(room, usuario)
            }
        }

        /////////>>>>>>>>>>
        val etPais = findViewById<EditText>(R.id.etPais)

        fun showTimePickerDialog() {
            // Obtenemos la hora actual para establecerla como valor predeterminado en el TimePicker
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Creamos el TimePickerDialog con el valor predeterminado de la hora actual
            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    // Aquí obtenemos la hora seleccionada por el usuario
                    val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                    // Concatenamos la fecha seleccionada previamente con la hora seleccionada y la establecemos en el EditText
                    val selectedDateTime = "${etPais.text} $selectedTime"
                    etPais.setText(selectedDateTime)
                },
                hour,
                minute,
                true // true para usar el formato de 24 horas, false para usar el formato de 12 horas
            )

            timePickerDialog.show()


        }
        fun showDatePickerDialog() {
            // Obtenemos la fecha actual para establecerla como valor predeterminado en el calendario
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Creamos el DatePickerDialog con el valor predeterminado de la fecha actual

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Aquí obtenemos la fecha seleccionada por el usuario y la establecemos en el EditText
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etPais.setText(selectedDate)
                    // Abrir el TimePickerDialog después de seleccionar la fecha
                    showTimePickerDialog()
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }



        etPais.setOnClickListener {
            // Aquí mostramos el DatePickerDialog cuando se hace clic en el EditText
            showDatePickerDialog()


        }

    }



    fun setupAlarmForUsuario(usuario: Usuario) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        // Obtener la fecha y hora del registro actual
        val fechaHora = usuario.pais

        // Convertir la fecha y hora a milisegundos
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = sdf.parse(fechaHora)
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Generar un identificador único para el PendingIntent utilizando el hash de usuario.pais
        val uniqueId = usuario.pais.hashCode()

        // Configurar la alarma utilizando PendingIntent y BroadcastReceiver
        val pendingIntent = PendingIntent.getBroadcast(this, uniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }


    fun obtenerUsuarios(room: DBPrueba) {
        lifecycleScope.launch {
            listaUsuarios = room.daoUsuario().obtenerUsuarios()
            adatador = AdaptadorUsuarios(listaUsuarios, this@MainActivity)
            binding.rvUsuarios.adapter = adatador

        }
    }

    fun agregarUsuario(room: DBPrueba, usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().agregarUsuario(usuario)
            setupAlarmForUsuario(usuario)
            Log.d("TAG", "Usuario agregado: ${usuario.usuario}, ${usuario.pais}")
            obtenerUsuarios(room)
            limpiarCampos()
        }
    }

    fun actualizarUsuario(room: DBPrueba, usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().actualizarUsuario(usuario.usuario, usuario.pais)
            obtenerUsuarios(room)

            limpiarCampos()
        }
    }

    fun limpiarCampos() {
        usuario.usuario = ""
        usuario.pais = ""
        binding.etUsuario.setText("")
        binding.etPais.setText("")

        if (binding.btnAddUpdate.text.equals("actualizar")) {
            binding.btnAddUpdate.setText("agregar")
            binding.etUsuario.isEnabled = true
        }

    }

    override fun onEditItemClick(usuario: Usuario) {
        binding.btnAddUpdate.setText("actualizar")
        binding.etUsuario.isEnabled = false
        this.usuario = usuario
        binding.etUsuario.setText(this.usuario.usuario)
        binding.etPais.setText(this.usuario.pais)
    }

    override fun onDeleteItemClick(usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().borrarUsuario(usuario.usuario)
            adatador.notifyDataSetChanged()
            obtenerUsuarios(room)
            }
        }
}