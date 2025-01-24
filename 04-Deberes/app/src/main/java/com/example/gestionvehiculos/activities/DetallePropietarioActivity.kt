package com.example.gestionvehiculos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.models.Propietario

class DetallePropietarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_propietario)

        val propietario = intent.getSerializableExtra("propietario") as? Propietario

        val textViewNombre = findViewById<TextView>(R.id.tvNombreDetallePropietario)
        val textViewIdentificacion = findViewById<TextView>(R.id.tvIdentificacionDetallePropietario)
        val textViewEdad = findViewById<TextView>(R.id.tvEdadDetallePropietario)
        val textViewNumVehiculos = findViewById<TextView>(R.id.tvNumVehiculosDetallePropietario)

        propietario?.let {
            textViewNombre.text = "Nombre: ${it.nombre}"
            textViewIdentificacion.text = "Identificación: ${it.identificacion}"
            textViewEdad.text = "Edad: ${it.edad}"
            textViewNumVehiculos.text = "Vehículos registrados: ${it.numVehiculos}"
        }
    }
}
