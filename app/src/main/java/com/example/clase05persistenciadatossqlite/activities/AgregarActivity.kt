package com.example.clase05persistenciadatossqlite.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AgregarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var etNombre: EditText
    private lateinit var spEquipo: Spinner

    // aqui es el listado de equipos
    private val equipos =
        arrayOf("Karasuno", "Nekoma", "Aoba Jōsai", "Shiratorizawa", "Inarizaki", "Fukurōdani")
    private var equipoSeleccionado: String = ""
    private lateinit var tvPersonaje: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, equipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spEquipo.adapter = adapter
        spEquipo.onItemSelectedListener = this
        fabAgregar.setOnClickListener {
            insertarPersonaje(etNombre.text.toString(), equipoSeleccionado)
        }
    }

    val columnaID = "id"
    val columnaNombrePersonaje = "nombre"
    val columnaEquipo = "equipo"
    var id: Int = 0
    private fun insertarPersonaje(nombrePersonaje: String, equipo: String) {
        if (!TextUtils.isEmpty(equipo)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombrePersonaje, nombrePersonaje)
            contenido.put(columnaEquipo, equipo)
            //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
            if (id > 0) {
                Toast.makeText(this, "Personaje " + nombrePersonaje + " agregado", Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, "Ups no se pudo guardar el personaje", Toast.LENGTH_LONG).show()
            baseDatos.cerrarConexion()
        } else {
            Snackbar.make(tvPersonaje, "Favor seleccionar un equipo", 0).show()
        }
    }

    private fun inicializarVistas() {
        etNombre = findViewById(R.id.etNombre)
        fabAgregar = findViewById(R.id.fabAgregar)
        spEquipo = findViewById(R.id.spEquipo)
        tvPersonaje = findViewById(R.id.tvPersonaje)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        equipoSeleccionado = equipos[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}