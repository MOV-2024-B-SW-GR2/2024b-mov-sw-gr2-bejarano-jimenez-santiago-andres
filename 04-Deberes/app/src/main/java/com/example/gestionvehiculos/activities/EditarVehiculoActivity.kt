package com.example.gestionvehiculos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Vehiculo

class EditarVehiculoActivity : AppCompatActivity() {

    private val vehiculoController = VehiculoController()
    private var currentVehiculo: Vehiculo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_vehiculo)

        currentVehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo

        val editTextMarca = findViewById<EditText>(R.id.etMarcaEditarVehiculo)
        val editTextModelo = findViewById<EditText>(R.id.etModeloEditarVehiculo)
        val editTextAnio = findViewById<EditText>(R.id.etAnioEditarVehiculo)
        val editTextPrecio = findViewById<EditText>(R.id.etPrecioEditarVehiculo)
        val buttonGuardar = findViewById<Button>(R.id.btnGuardarVehiculo)

        currentVehiculo?.let {
            editTextMarca.setText(it.marca)
            editTextModelo.setText(it.modelo)
            editTextAnio.setText(it.anio.toString())
            editTextPrecio.setText(it.precio.toString())
        }

        buttonGuardar.setOnClickListener {
            currentVehiculo?.let {
                it.marca = editTextMarca.text.toString()
                it.modelo = editTextModelo.text.toString()
                it.anio = editTextAnio.text.toString().toInt()
                it.precio = editTextPrecio.text.toString().toDouble()
                // Si quisieras cambiar la matrícula, podrías añadir un checkbox o algo similar

                vehiculoController.updateVehiculo(it.id, it)
            }
            finish()
        }
    }
}
