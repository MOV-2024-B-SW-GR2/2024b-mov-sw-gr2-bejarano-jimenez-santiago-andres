package com.example.gestionvehiculos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.adapters.VehiculoAdapter
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Propietario
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.button.MaterialButton

class DetallePropietarioActivity : AppCompatActivity() {
    private lateinit var propietario: Propietario
    private lateinit var vehiculoController: VehiculoController
    private lateinit var propietarioController: PropietarioController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VehiculoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_propietario)

        propietario = intent.getSerializableExtra("propietario") as? Propietario ?: run {
            finish()
            return
        }
        
        vehiculoController = VehiculoController(this)
        propietarioController = PropietarioController(this)

        // Mostrar información del propietario
        findViewById<TextView>(R.id.tvNombreDetallePropietario).text = propietario.nombre
        findViewById<TextView>(R.id.tvIdentificacionDetallePropietario).text = propietario.identificacion
        findViewById<TextView>(R.id.tvEdadDetallePropietario).text = "${propietario.edad} años"

        // Configurar el botón de editar propietario
        findViewById<MaterialButton>(R.id.btnEditarDetallePropietario).setOnClickListener {
            val intent = Intent(this, EditarPropietarioActivity::class.java)
            intent.putExtra("propietario", propietario)
            startActivityForResult(intent, EDITAR_PROPIETARIO_REQUEST)
        }

        // Configurar el botón de eliminar propietario
        findViewById<MaterialButton>(R.id.btnEliminarPropietario).setOnClickListener {
            // Primero eliminar todos los vehículos del propietario
            val vehiculos = vehiculoController.getVehiculosByPropietarioId(propietario.id)
            vehiculos.forEach { vehiculo ->
                vehiculoController.deleteVehiculo(vehiculo.id)
            }
            
            // Luego eliminar al propietario
            propietarioController.deletePropietario(propietario.id)
            
            // Finalizar la actividad y volver a la lista de propietarios
            setResult(RESULT_OK)
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // Obtener vehículos del propietario
        val vehiculos = vehiculoController.getVehiculosByPropietarioId(propietario.id)
        findViewById<TextView>(R.id.tvNumVehiculosDetallePropietario).text = 
            "Vehículos registrados: ${vehiculos.size}"

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerVehiculosPropietario)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Configurar adaptador con los handlers para los botones
        adapter = VehiculoAdapter(
            vehiculos = vehiculos,
            onVerClick = { vehiculo -> verVehiculo(vehiculo) },
            onEditarClick = { vehiculo -> editarVehiculo(vehiculo) },
            onEliminarClick = { vehiculo -> eliminarVehiculo(vehiculo) }
        )
        recyclerView.adapter = adapter
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
        setupRecyclerView()
    }

    companion object {
        const val EDITAR_VEHICULO_REQUEST = 1
        const val EDITAR_PROPIETARIO_REQUEST = 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                EDITAR_VEHICULO_REQUEST -> setupRecyclerView()
                EDITAR_PROPIETARIO_REQUEST -> {
                    // Actualizar la información del propietario
                    propietario = data?.getSerializableExtra("propietario") as? Propietario ?: return
                    findViewById<TextView>(R.id.tvNombreDetallePropietario).text = propietario.nombre
                    findViewById<TextView>(R.id.tvIdentificacionDetallePropietario).text = propietario.identificacion
                    findViewById<TextView>(R.id.tvEdadDetallePropietario).text = "${propietario.edad} años"
                    setupRecyclerView()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
}
