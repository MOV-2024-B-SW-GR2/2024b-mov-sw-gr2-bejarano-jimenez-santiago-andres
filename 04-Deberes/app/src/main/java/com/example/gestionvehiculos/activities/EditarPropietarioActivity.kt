package com.example.gestionvehiculos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Propietario

class EditarPropietarioActivity : AppCompatActivity() {

    private val propietarioController = PropietarioController()
    private var currentPropietario: Propietario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_propietario)

        currentPropietario = intent.getSerializableExtra("propietario") as? Propietario

        val editTextNombre = findViewById<EditText>(R.id.etNombreEditarPropietario)
        val editTextFecha = findViewById<EditText>(R.id.etFechaEditarPropietario)
        val editTextIdentificacion = findViewById<EditText>(R.id.etIdentificacionEditarPropietario)

        val buttonGuardar = findViewById<Button>(R.id.btnGuardarPropietario)

        // Cargamos datos existentes
        currentPropietario?.let {
            editTextNombre.setText(it.nombre)
            editTextFecha.setText(it.fechaNacimiento)
            editTextIdentificacion.setText(it.identificacion)
        }

        buttonGuardar.setOnClickListener {
            currentPropietario?.let {
                it.nombre = editTextNombre.text.toString()
                it.fechaNacimiento = editTextFecha.text.toString()
                it.identificacion = editTextIdentificacion.text.toString()
                // Podrías recalcular la edad si lo deseas

                // Guardamos en el controlador
                propietarioController.updatePropietario(it.id, it)
            }
            finish()
        }
    }
}
