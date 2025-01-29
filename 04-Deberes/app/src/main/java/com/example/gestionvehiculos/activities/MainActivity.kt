package com.example.gestionvehiculos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.gestionvehiculos.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardPropietarios = findViewById<CardView>(R.id.btnPropietariosMain)
        val cardVehiculos = findViewById<CardView>(R.id.btnVehiculosMain)

        cardPropietarios.setOnClickListener {
            val intent = Intent(this, ListPropietariosActivity::class.java)
            startActivity(intent)
        }

        cardVehiculos.setOnClickListener {
            val intent = Intent(this, ListVehiculosActivity::class.java)
            startActivity(intent)
        }
    }
}
