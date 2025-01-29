package com.example.gestionvehiculos.controllers

import android.content.Context
import com.example.gestionvehiculos.database.DatabaseHelper
import com.example.gestionvehiculos.models.Propietario

class PropietarioController(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addPropietario(propietario: Propietario): Long {
        return dbHelper.insertPropietario(propietario)
    }

    fun getPropietarios(): List<Propietario> {
        return dbHelper.getAllPropietarios()
    }

    fun getPropietarioById(id: Int): Propietario? {
        return dbHelper.getPropietarioById(id)
    }

    fun deletePropietario(propietarioId: Int): Int {
        return dbHelper.deletePropietario(propietarioId)
    }

    fun updatePropietario(propietarioId: Int, updatedPropietario: Propietario): Int {
        return dbHelper.updatePropietario(updatedPropietario)
    }
}
