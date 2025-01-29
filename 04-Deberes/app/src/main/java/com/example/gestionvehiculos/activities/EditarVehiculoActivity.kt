package com.example.gestionvehiculos.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Propietario
import com.example.gestionvehiculos.models.Vehiculo

class EditarVehiculoActivity : AppCompatActivity() {
    private lateinit var vehiculoController: VehiculoController
    private lateinit var propietarioController: PropietarioController
    private var currentVehiculo: Vehiculo? = null
    private lateinit var propietarios: List<Propietario>
    private lateinit var spinnerPropietario: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_vehiculo)

        vehiculoController = VehiculoController(this)
        propietarioController = PropietarioController(this)
        currentVehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo

        // Inicializar vistas
        spinnerPropietario = findViewById(R.id.spinnerPropietario)
        val editTextMarca = findViewById<EditText>(R.id.etMarcaEditarVehiculo)
        val editTextModelo = findViewById<EditText>(R.id.etModeloEditarVehiculo)
        val editTextAnio = findViewById<EditText>(R.id.etAnioEditarVehiculo)
        val editTextPrecio = findViewById<EditText>(R.id.etPrecioEditarVehiculo)
        val checkBoxMatriculado = findViewById<CheckBox>(R.id.cbMatriculadoEditarVehiculo)
        val buttonGuardar = findViewById<Button>(R.id.btnGuardarVehiculo)

        // Cargar propietarios en el spinner
        cargarPropietarios()

        // Cargar datos si es una edición
        currentVehiculo?.let { vehiculo ->
            editTextMarca.setText(vehiculo.marca)
            editTextModelo.setText(vehiculo.modelo)
            editTextAnio.setText(vehiculo.anio.toString())
            editTextPrecio.setText(vehiculo.precio.toString())
            checkBoxMatriculado.isChecked = vehiculo.estaMatriculado
            
            // Seleccionar el propietario actual en el spinner
            val propietarioIndex = propietarios.indexOfFirst { it.id == vehiculo.propietarioId }
            if (propietarioIndex >= 0) {
                spinnerPropietario.setSelection(propietarioIndex)
            }
        }

        buttonGuardar.setOnClickListener {
            val marca = editTextMarca.text.toString().trim()
            val modelo = editTextModelo.text.toString().trim()
            val anioStr = editTextAnio.text.toString().trim()
            val precioStr = editTextPrecio.text.toString().trim()

            if (marca.isBlank() || modelo.isBlank() || anioStr.isBlank() || precioStr.isBlank()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (spinnerPropietario.selectedItemPosition == -1) {
                Toast.makeText(this, "Por favor seleccione un propietario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val anio = anioStr.toInt()
                val precio = precioStr.toDouble()
                val propietarioSeleccionado = propietarios[spinnerPropietario.selectedItemPosition]

                if (currentVehiculo != null) {
                    // Actualizar vehículo existente
                    val vehiculoActualizado = Vehiculo(
                        id = currentVehiculo!!.id,
                        propietarioId = propietarioSeleccionado.id,
                        marca = marca,
                        modelo = modelo,
                        anio = anio,
                        precio = precio,
                        estaMatriculado = checkBoxMatriculado.isChecked
                    )
                    
                    val resultado = vehiculoController.updateVehiculo(currentVehiculo!!.id, vehiculoActualizado)
                    if (resultado > 0) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al actualizar el vehículo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Crear nuevo vehículo
                    val nuevoVehiculo = Vehiculo(
                        id = 0,
                        propietarioId = propietarioSeleccionado.id,
                        marca = marca,
                        modelo = modelo,
                        anio = anio,
                        precio = precio,
                        estaMatriculado = checkBoxMatriculado.isChecked
                    )
                    
                    val resultado = vehiculoController.addVehiculo(nuevoVehiculo)
                    if (resultado > 0) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al crear el vehículo", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Por favor ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarPropietarios() {
        propietarios = propietarioController.getPropietarios()
        
        if (propietarios.isEmpty()) {
            Toast.makeText(this, "No hay propietarios registrados. Por favor, registre un propietario primero.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Crear el adaptador para el spinner
        val nombresPropietarios = propietarios.map { "${it.nombre} (${it.identificacion})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresPropietarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPropietario.adapter = adapter
    }
}
