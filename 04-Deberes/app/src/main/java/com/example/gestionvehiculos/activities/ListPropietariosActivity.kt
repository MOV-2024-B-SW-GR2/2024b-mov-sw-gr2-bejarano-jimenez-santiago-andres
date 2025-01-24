package com.example.gestionvehiculos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.adapters.PropietarioAdapter
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Propietario

class ListPropietariosActivity : AppCompatActivity() {
    private val propietarioController = PropietarioController()
    private lateinit var recyclerViewPropietarios: RecyclerView
    private lateinit var propietarioAdapter: PropietarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_propietarios)

        // Agregamos datos de ejemplo al controlador (si deseas iniciar con algo de data)
        propietarioController.addPropietario(
            Propietario(
                id = 1,
                nombre = "Juan Pérez",
                edad = 30,
                fechaNacimiento = "1993-01-15",
                identificacion = "1234567890",
                numVehiculos = 2
            )
        )
        propietarioController.addPropietario(
            Propietario(
                id = 2,
                nombre = "María Gómez",
                edad = 25,
                fechaNacimiento = "1998-05-10",
                identificacion = "0987654321",
                numVehiculos = 1
            )
        )

        recyclerViewPropietarios = findViewById(R.id.recyclerPropietarios)
        recyclerViewPropietarios.layoutManager = LinearLayoutManager(this)

        propietarioAdapter = PropietarioAdapter(
            propietarioController.getPropietarios(),
            onVerClick = { propietario -> verPropietario(propietario) },
            onEditarClick = { propietario -> editarPropietario(propietario) },
            onEliminarClick = { propietario -> eliminarPropietario(propietario) }
        )

        recyclerViewPropietarios.adapter = propietarioAdapter
    }

    private fun verPropietario(propietario: Propietario) {
        val intent = Intent(this, DetallePropietarioActivity::class.java)
        intent.putExtra("propietario", propietario)
        startActivity(intent)
    }

    private fun editarPropietario(propietario: Propietario) {
        val intent = Intent(this, EditarPropietarioActivity::class.java)
        intent.putExtra("propietario", propietario)
        startActivity(intent)
    }

    private fun eliminarPropietario(propietario: Propietario) {
        propietarioController.deletePropietario(propietario.id)
        // Notificamos cambios al adapter
        recyclerViewPropietarios.adapter = PropietarioAdapter(
            propietarioController.getPropietarios(),
            onVerClick = { verPropietario(it) },
            onEditarClick = { editarPropietario(it) },
            onEliminarClick = { eliminarPropietario(it) }
        )
    }
}
