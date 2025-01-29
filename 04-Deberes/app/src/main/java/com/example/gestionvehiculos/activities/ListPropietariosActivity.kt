package com.example.gestionvehiculos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.adapters.PropietarioAdapter
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.models.Propietario
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListPropietariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PropietarioAdapter
    private lateinit var propietarioController: PropietarioController

    companion object {
        const val EDITAR_PROPIETARIO_REQUEST = 1
        const val CREAR_PROPIETARIO_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_propietarios)

        propietarioController = PropietarioController(this)
        recyclerView = findViewById(R.id.recyclerPropietarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el adaptador
        adapter = PropietarioAdapter(propietarioController.getPropietarios()) { propietario ->
            val intent = Intent(this, DetallePropietarioActivity::class.java)
            intent.putExtra("propietario", propietario)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Configurar FAB
        findViewById<FloatingActionButton>(R.id.fabAddPropietario).setOnClickListener {
            val intent = Intent(this, EditarPropietarioActivity::class.java)
            startActivityForResult(intent, CREAR_PROPIETARIO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == EDITAR_PROPIETARIO_REQUEST || requestCode == CREAR_PROPIETARIO_REQUEST)) {
            actualizarListaPropietarios()
        }
    }

    private fun actualizarListaPropietarios() {
        adapter.updatePropietarios(propietarioController.getPropietarios())
    }

    override fun onResume() {
        super.onResume()
        actualizarListaPropietarios()
    }
}
