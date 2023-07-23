package com.example.crud_room_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.crud_room_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdaptadorListener {

    lateinit var binding: ActivityMainBinding

    var listaUsuarios: MutableList<Usuario> = mutableListOf()

    lateinit var adatador: AdaptadorUsuarios

    lateinit var room: DBPrueba

    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {

        usuario = Usuario("", "", "")

//  realizar la configuración inicial de la actividad
        super.onCreate(savedInstanceState)


// Inflar el diseño de la actividad utilizando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
// Establecer la vista raíz de la actividad a la vista inflada mediante ViewBinding
        setContentView(binding.root)
// Configurar el RecyclerView (rvUsuarios) con un LinearLayoutManager
        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)
// Crear una instancia de la base de datos Room utilizando databaseBuilder
        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()

// obtener la lista de usuarios desde la base de datos y mostrarla en el RecyclerView
        obtenerUsuarios(room)


// Configuración del clic en el botón btnAddUpdate
        binding.btnAddUpdate.setOnClickListener {


            // Verificar si los campos de usuario y país están vacíos
            if (binding.etUsuario.text.isNullOrEmpty() || binding.etPais.text.isNullOrEmpty()) {
                // Si alguno de los campos está vacío, mostrar un mensaje de error y salir de la función
                Toast.makeText(this, "DEBES LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si el texto del botón es "agregar"
            if (binding.btnAddUpdate.text == "agregar") {
                // Crear un nuevo objeto Usuario con los valores ingresados en los campos de texto
                val usuario = Usuario(
                    binding.etUsuario.text.toString().trim(),
                    binding.etPais.text.toString().trim(),
                    binding.etFecha.text.toString().trim()

                )

                // Llamar a la función agregarUsuario() para agregar el nuevo usuario a la base de datos
                agregarUsuario(room, usuario)
            } else if (binding.btnAddUpdate.text == "actualizar") {
                // Si el texto del botón es "actualizar", actualizar el campo "pais" del usuario existente
                usuario.pais = binding.etPais.text.toString().trim()
                usuario.fecha = binding.etFecha.text.toString().trim()

                // Llamar a la función actualizarUsuario() para actualizar el usuario en la base de datos
                actualizarUsuario(room, usuario)
            }
        }


    }

    // Función para obtener la lista de usuarios desde la base de datos y mostrarla en un RecyclerView
    fun obtenerUsuarios(room: DBPrueba) {
        // Iniciamos un nuevo coroutine en el ámbito del ciclo de vida actual
        lifecycleScope.launch {
            // Llamamos a la función obtenerUsuarios() del DAO (Data Access Object) para obtener la lista de usuarios desde la base de datos
            listaUsuarios = room.daoUsuario().obtenerUsuarios()

            // Creamos un nuevo adaptador para el RecyclerView utilizando la lista de usuarios obtenida
            adatador = AdaptadorUsuarios(listaUsuarios, this@MainActivity)

            // Asignamos el adaptador al RecyclerView en la interfaz de usuario
            binding.rvUsuarios.adapter = adatador
        }
    }


    // Función para agregar un nuevo usuario a la base de datos utilizando Room
    fun agregarUsuario(room: DBPrueba, usuario: Usuario) {
        // Iniciamos un nuevo coroutine en el ámbito del ciclo de vida actual
        lifecycleScope.launch {
            // Llamamos a la función agregarUsuario() del DAO (Data Access Object) para insertar el nuevo usuario en la base de datos
            room.daoUsuario().agregarUsuario(usuario)

            // Después de agregar el usuario, llamamos a la función obtenerUsuarios() para actualizar la lista de usuarios en la interfaz
            obtenerUsuarios(room)

            // Finalmente, llamamos a la función limpiarCampos() para borrar los campos del formulario y prepararlo para agregar un nuevo usuario
            limpiarCampos()
        }
    }


    fun actualizarUsuario(room: DBPrueba, usuario: Usuario ) {
        lifecycleScope.launch {
            room.daoUsuario().actualizarUsuario(usuario.usuario, usuario.pais, usuario.fecha)
            obtenerUsuarios(room)
            limpiarCampos()
        }
    }

    fun limpiarCampos() {
        // Reiniciar los valores del objeto usuario (si es que existe)
        usuario.usuario = ""
        usuario.pais = ""
        usuario.fecha = ""

        // Borrar el texto de los campos de entrada en la interfaz de usuario
        binding.etUsuario.setText("")
        binding.etPais.setText("")
        binding.etFecha.setText("")

        // Verificar si el botón actual es de actualización y cambiarlo a "agregar"
        // También se habilita el campo de usuario para permitir la adición de un nuevo usuario
        if (binding.btnAddUpdate.text == "actualizar") {
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
        binding.etFecha.setText(this.usuario.fecha)
    }

    override fun onDeleteItemClick(usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().borrarUsuario(usuario.usuario)
            adatador.notifyDataSetChanged()
            obtenerUsuarios(room)
        }
    }
}