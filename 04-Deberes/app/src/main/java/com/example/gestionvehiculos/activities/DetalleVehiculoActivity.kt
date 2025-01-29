package com.example.gestionvehiculos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.button.MaterialButton

class DetalleVehiculoActivity : AppCompatActivity() {
    private lateinit var propietarioController: PropietarioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_vehiculo)

        propietarioController = PropietarioController(this)

        val vehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo
        if (vehiculo != null) {
            mostrarDetallesVehiculo(vehiculo)
            setupBotones(vehiculo)
        } else {
            finish()
        }
    }

    private fun mostrarDetallesVehiculo(vehiculo: Vehiculo) {
        // Mostrar detalles del vehículo
        findViewById<TextView>(R.id.tvMarcaDetalleVehiculo).text = vehiculo.marca
        findViewById<TextView>(R.id.tvModeloDetalleVehiculo).text = vehiculo.modelo
        findViewById<TextView>(R.id.tvAnioDetalleVehiculo).text = vehiculo.anio.toString()
        findViewById<TextView>(R.id.tvPrecioDetalleVehiculo).text = "$ ${vehiculo.precio}"
        findViewById<TextView>(R.id.tvMatriculadoDetalleVehiculo).text = 
            if (vehiculo.estaMatriculado) "Matriculado" else "No matriculado"

        // Mostrar información del propietario
        val propietario = vehiculo.propietarioId?.let { propietarioController.getPropietarioById(it) }
        findViewById<TextView>(R.id.tvPropietarioDetalleVehiculo).text = propietario?.let {
            "${it.nombre} (${it.identificacion})"
        } ?: "Sin propietario asignado"
    }

    private fun setupBotones(vehiculo: Vehiculo) {
        findViewById<MaterialButton>(R.id.btnEditarDetalleVehiculo)?.setOnClickListener {
            val intent = Intent(this, EditarVehiculoActivity::class.java)
            intent.putExtra("vehiculo", vehiculo)
            startActivity(intent)
        }
    }
}
