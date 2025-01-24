package com.example.gestionvehiculos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.models.Vehiculo

class DetalleVehiculoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_vehiculo)

        val vehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo

        val textViewMarca = findViewById<TextView>(R.id.tvMarcaDetalleVehiculo)
        val textViewModelo = findViewById<TextView>(R.id.tvModeloDetalleVehiculo)
        val textViewAnio = findViewById<TextView>(R.id.tvAnioDetalleVehiculo)
        val textViewPrecio = findViewById<TextView>(R.id.tvPrecioDetalleVehiculo)
        val textViewMatriculado = findViewById<TextView>(R.id.tvMatriculadoDetalleVehiculo)

        vehiculo?.let {
            textViewMarca.text = "Marca: ${it.marca}"
            textViewModelo.text = "Modelo: ${it.modelo}"
            textViewAnio.text = "Año: ${it.anio}"
            textViewPrecio.text = "Precio: $${it.precio}"
            textViewMatriculado.text = "¿Matriculado?: ${if (it.estaMatriculado) "Sí" else "No"}"
        }
    }
}
