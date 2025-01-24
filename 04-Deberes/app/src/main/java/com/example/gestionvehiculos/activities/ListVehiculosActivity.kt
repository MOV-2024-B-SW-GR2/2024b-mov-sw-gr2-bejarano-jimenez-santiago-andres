package com.example.gestionvehiculos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.adapters.VehiculoAdapter
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Vehiculo

class ListVehiculosActivity : AppCompatActivity() {
    private val vehiculoController = VehiculoController()
    private lateinit var recyclerViewVehiculos: RecyclerView
    private lateinit var vehiculoAdapter: VehiculoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_vehiculos)

        // Datos de ejemplo
        vehiculoController.addVehiculo(
            Vehiculo(
                id = 1,
                propietarioId = 1,
                marca = "Toyota",
                modelo = "Corolla",
                anio = 2020,
                precio = 15000.0,
                estaMatriculado = true
            )
        )
        vehiculoController.addVehiculo(
            Vehiculo(
                id = 2,
                propietarioId = 1,
                marca = "Chevrolet",
                modelo = "Spark",
                anio = 2019,
                precio = 12000.0,
                estaMatriculado = false
            )
        )

        recyclerViewVehiculos = findViewById(R.id.recyclerVehiculos)
        recyclerViewVehiculos.layoutManager = LinearLayoutManager(this)

        vehiculoAdapter = VehiculoAdapter(
            vehiculoController.getVehiculos(),
            onVerClick = { vehiculo -> verVehiculo(vehiculo) },
            onEditarClick = { vehiculo -> editarVehiculo(vehiculo) },
            onEliminarClick = { vehiculo -> eliminarVehiculo(vehiculo) }
        )

        recyclerViewVehiculos.adapter = vehiculoAdapter
    }

    private fun verVehiculo(vehiculo: Vehiculo) {
        val intent = Intent(this, DetalleVehiculoActivity::class.java)
        intent.putExtra("vehiculo", vehiculo)
        startActivity(intent)
    }

    private fun editarVehiculo(vehiculo: Vehiculo) {
        val intent = Intent(this, EditarVehiculoActivity::class.java)
        intent.putExtra("vehiculo", vehiculo)
        startActivity(intent)
    }

    private fun eliminarVehiculo(vehiculo: Vehiculo) {
        vehiculoController.deleteVehiculo(vehiculo.id)
        // Refrescamos la lista
        recyclerViewVehiculos.adapter = VehiculoAdapter(
            vehiculoController.getVehiculos(),
            onVerClick = { verVehiculo(it) },
            onEditarClick = { editarVehiculo(it) },
            onEliminarClick = { eliminarVehiculo(it) }
        )
    }
}
