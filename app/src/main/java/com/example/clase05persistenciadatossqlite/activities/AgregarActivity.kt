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
    private lateinit var etJuego: EditText
    private lateinit var spEquipo: Spinner

    // aqui es el listado de equipos
    private val equipos =
        arrayOf("Karasuno", "Nekoma", "Aoba Jōsai", "Shiratorizawa", "Inarizaki", "Fukurōdani")
    private var equipoSeleccionado: String = ""
    private lateinit var tvJuego: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, equipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spEquipo.adapter = adapter
        spEquipo.onItemSelectedListener = this
        fabAgregar.setOnClickListener {
            insertarJuego(etJuego.text.toString(), equipoSeleccionado)
        }
    }

    val columnaID = "id"
    val columnaNombreJuego = "nombre"
    val columnaEquipo = "equipo"
    var id: Int = 0
    private fun insertarJuego(nombreJuego: String, equipo: String) {
        if (!TextUtils.isEmpty(equipo)) {
            val baseDatos = ManejadorBaseDatos(this)
            //  val columnas = arrayOf(columnaID, columnaNombreJuego, columnaPrecio, columnaConsola)
            val contenido = ContentValues()
            contenido.put(columnaNombreJuego, nombreJuego)
            contenido.put(columnaEquipo, equipo)
            //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
            if (id > 0) {
                Toast.makeText(this, "juego " + nombreJuego + " agregado", Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, "Ups no se pudo guardar el juego", Toast.LENGTH_LONG).show()
            baseDatos.cerrarConexion()
        } else {
            Snackbar.make(tvJuego, "Favor seleccionar una consola", 0).show()
        }
    }

    private fun inicializarVistas() {
        etJuego = findViewById(R.id.etJuego)
        fabAgregar = findViewById(R.id.fabAgregar)
        spEquipo = findViewById(R.id.spEquipo)
        tvJuego = findViewById(R.id.tvJuego)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        equipoSeleccionado = equipos[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}