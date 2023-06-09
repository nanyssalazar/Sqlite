package com.example.clase05persistenciadatossqlite.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.example.clase05persistenciadatossqlite.modelos.Personaje
import com.google.android.material.snackbar.Snackbar

class EditarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private lateinit var etNombre: EditText
    private lateinit var spEquipo: Spinner
    private val equipos =
        arrayOf("Karasuno", "Nekoma", "Aoba Jōsai", "Shiratorizawa", "Inarizaki", "Fukurōdani")
    private var equipoSeleccionado: String = ""
    private lateinit var tvEquipo: TextView
    var personaje: Personaje? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        //  setSupportActionBar(toolbar)
        getSupportActionBar()?.title = "Edición"
        getSupportActionBar()?.setHomeButtonEnabled(true);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarPersonaje(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etNombre.setText(personaje?.nombre)
        val position = equipos.indexOf(personaje?.equipo)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, equipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEquipo.adapter = adapter
        spEquipo.onItemSelectedListener = this
        if (position >= 0) {
            spEquipo.setSelection(position)
            equipoSeleccionado = equipos[position]
        }
    }

    private fun inicializarVistas() {
        etNombre = findViewById(R.id.etNombre)
        bnGuardar = findViewById(R.id.bnGuardar)
        spEquipo = findViewById(R.id.spEquipo)
        tvEquipo = findViewById(R.id.tvEquipo)
        bnGuardar.setOnClickListener {
            actualizarPersonaje(etNombre.text.toString(), equipoSeleccionado)
        }
    }

    val columnaNombrePersonaje = "nombre"
    val columnaEquipo = "equipo"

    private fun actualizarPersonaje(nombrePersonaje: String, equipo: String) {
        if (!TextUtils.isEmpty(equipo)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombrePersonaje, nombrePersonaje)
            contenido.put(columnaEquipo, equipo)
            if (id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etNombre, "Personaje actualizado", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atención")
                        .setMessage("No fue posible actualizarlo")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "no hiciste id", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarPersonaje(idPersonaje: Int) {

        if (idPersonaje > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "equipo")
            val condicion = " id = ?"
            val argumentos = arrayOf(idPersonaje.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val personaje_id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    val equipo = cursor.getString(cursor.getColumnIndex("equipo"))
                    personaje = Personaje(personaje_id, nombre, equipo)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        equipoSeleccionado = equipos[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}