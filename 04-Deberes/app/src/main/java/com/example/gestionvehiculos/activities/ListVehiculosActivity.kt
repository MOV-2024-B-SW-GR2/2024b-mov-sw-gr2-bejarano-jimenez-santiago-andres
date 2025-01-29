package com.example.gestionvehiculos.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.adapters.VehiculoAdapter
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListVehiculosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehiculoAdapter: VehiculoAdapter
    private lateinit var vehiculoController: VehiculoController

    companion object {
        const val EDITAR_VEHICULO_REQUEST = 1
        const val CREAR_VEHICULO_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_vehiculos)

        vehiculoController = VehiculoController(this)
        recyclerView = findViewById(R.id.recyclerVehiculos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupRecyclerView()

        findViewById<FloatingActionButton>(R.id.fabAddVehiculo).setOnClickListener {
            val intent = Intent(this, EditarVehiculoActivity::class.java)
            startActivityForResult(intent, CREAR_VEHICULO_REQUEST)
        }
    }

    private fun setupRecyclerView() {
        vehiculoAdapter = VehiculoAdapter(
            vehiculos = vehiculoController.getVehiculos(),
            onVerClick = { vehiculo -> verVehiculo(vehiculo) },
            onEditarClick = { vehiculo -> editarVehiculo(vehiculo) },
            onEliminarClick = { vehiculo -> eliminarVehiculo(vehiculo) }
        )
        recyclerView.adapter = vehiculoAdapter
    }

    private fun verVehiculo(vehiculo: Vehiculo) {
        val intent = Intent(this, DetalleVehiculoActivity::class.java)
        intent.putExtra("vehiculo", vehiculo)
        startActivity(intent)
    }

    private fun editarVehiculo(vehiculo: Vehiculo) {
        val intent = Intent(this, EditarVehiculoActivity::class.java)
        intent.putExtra("vehiculo", vehiculo)
        startActivityForResult(intent, EDITAR_VEHICULO_REQUEST)
    }

    private fun eliminarVehiculo(vehiculo: Vehiculo) {
        vehiculoController.deleteVehiculo(vehiculo.id)
        setupRecyclerView() // Actualizar la lista después de eliminar
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && 
            (requestCode == EDITAR_VEHICULO_REQUEST || requestCode == CREAR_VEHICULO_REQUEST)) {
            setupRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
}
