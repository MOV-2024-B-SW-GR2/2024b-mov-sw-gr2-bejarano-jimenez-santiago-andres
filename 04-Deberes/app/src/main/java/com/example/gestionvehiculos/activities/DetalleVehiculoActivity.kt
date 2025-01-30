package com.example.gestionvehiculos.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.button.MaterialButton

class DetalleVehiculoActivity : AppCompatActivity() {
    private lateinit var propietarioController: PropietarioController
    private lateinit var tvMarca: TextView
    private lateinit var tvModelo: TextView
    private lateinit var tvAnio: TextView
    private lateinit var tvPrecio: TextView
    private lateinit var tvMatriculado: TextView
    private lateinit var tvPropietario: TextView
    private lateinit var btnEditarVehiculo: MaterialButton
    private lateinit var btnConcesionario: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_vehiculo)

        propietarioController = PropietarioController(this)

        val vehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo
        
        tvMarca = findViewById(R.id.tvMarcaDetalleVehiculo)
        tvModelo = findViewById(R.id.tvModeloDetalleVehiculo)
        tvAnio = findViewById(R.id.tvAnioDetalleVehiculo)
        tvPrecio = findViewById(R.id.tvPrecioDetalleVehiculo)
        tvMatriculado = findViewById(R.id.tvMatriculadoDetalleVehiculo)
        tvPropietario = findViewById(R.id.tvPropietarioDetalleVehiculo)
        btnEditarVehiculo = findViewById(R.id.btnEditarDetalleVehiculo)
        btnConcesionario = findViewById(R.id.btnConcesionarioAutorizado)

        if (vehiculo != null) {
            mostrarDetallesVehiculo(vehiculo)
            setupBotones(vehiculo)
        } else {
            finish()
        }

        btnConcesionario.setOnClickListener {
            vehiculo?.let { veh ->
                Log.d("DetalleVehiculo", "Abriendo mapa para ${veh.marca} en (${veh.latitud}, ${veh.longitud})")
                
                if (veh.latitud == null || veh.longitud == null) {
                    Toast.makeText(this, "Este vehículo no tiene coordenadas asignadas", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                
                val intent = Intent(this, MapaVehiculoActivity::class.java).apply {
                    putExtra("latitud", veh.latitud)
                    putExtra("longitud", veh.longitud)
                    putExtra("marca", veh.marca)
                }
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Error: No se encontró información del vehículo", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarDetallesVehiculo(vehiculo: Vehiculo) {
        // Mostrar detalles del vehículo
        tvMarca.text = vehiculo.marca
        tvModelo.text = vehiculo.modelo
        tvAnio.text = vehiculo.anio.toString()
        tvPrecio.text = "$ ${vehiculo.precio}"
        tvMatriculado.text = 
            if (vehiculo.estaMatriculado) "Matriculado" else "No matriculado"

        // Mostrar información del propietario
        val propietario = vehiculo.propietarioId?.let { propietarioController.getPropietarioById(it) }
        tvPropietario.text = propietario?.let {
            "${it.nombre} (${it.identificacion})"
        } ?: "Sin propietario asignado"
    }

    private fun setupBotones(vehiculo: Vehiculo) {
        btnEditarVehiculo.setOnClickListener {
            val intent = Intent(this, EditarVehiculoActivity::class.java)
            intent.putExtra("vehiculo", vehiculo)
            startActivity(intent)
        }
    }
}
