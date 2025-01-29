package com.example.gestionvehiculos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Propietario
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EditarPropietarioActivity : AppCompatActivity() {

    private lateinit var propietarioController: PropietarioController
    private var currentPropietario: Propietario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_propietario)

        propietarioController = PropietarioController(this)
        currentPropietario = intent.getSerializableExtra("propietario") as? Propietario

        val editTextNombre = findViewById<EditText>(R.id.etNombreEditarPropietario)
        val editTextFecha = findViewById<EditText>(R.id.etFechaEditarPropietario)
        val editTextIdentificacion = findViewById<EditText>(R.id.etIdentificacionEditarPropietario)
        val buttonGuardar = findViewById<Button>(R.id.btnGuardarPropietario)

        // Cargamos datos existentes si es una edición
        currentPropietario?.let {
            editTextNombre.setText(it.nombre)
            editTextFecha.setText(it.fechaNacimiento)
            editTextIdentificacion.setText(it.identificacion)
        }

        buttonGuardar.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val fechaNacimiento = editTextFecha.text.toString().trim()
            val identificacion = editTextIdentificacion.text.toString().trim()

            if (nombre.isBlank() || fechaNacimiento.isBlank() || identificacion.isBlank()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Calcular edad
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fechaNac = LocalDate.parse(fechaNacimiento, formatter)
                val edad = Period.between(fechaNac, LocalDate.now()).years

                if (currentPropietario != null) {
                    // Actualizar propietario existente
                    val propietarioActualizado = Propietario(
                        id = currentPropietario!!.id,
                        nombre = nombre,
                        edad = edad,
                        fechaNacimiento = fechaNacimiento,
                        identificacion = identificacion,
                        numVehiculos = currentPropietario!!.numVehiculos
                    )

                    val resultado = propietarioController.updatePropietario(currentPropietario!!.id, propietarioActualizado)
                    if (resultado > 0) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al actualizar el propietario", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Crear nuevo propietario
                    val nuevoPropietario = Propietario(
                        id = 0, // El ID será asignado por la base de datos
                        nombre = nombre,
                        edad = edad,
                        fechaNacimiento = fechaNacimiento,
                        identificacion = identificacion,
                        numVehiculos = 0
                    )

                    val resultado = propietarioController.addPropietario(nuevoPropietario)
                    if (resultado > 0) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al crear el propietario", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: DateTimeParseException) {
                Toast.makeText(this, "Formato de fecha inválido. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
