package com.example.gestionvehiculos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.gestionvehiculos.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonPropietarios = findViewById<Button>(R.id.btnPropietariosMain)
        val buttonVehiculos = findViewById<Button>(R.id.btnVehiculosMain)

        buttonPropietarios.setOnClickListener {
            startActivity(Intent(this, ListPropietariosActivity::class.java))
        }

        buttonVehiculos.setOnClickListener {
            startActivity(Intent(this, ListVehiculosActivity::class.java))
        }
    }
}
