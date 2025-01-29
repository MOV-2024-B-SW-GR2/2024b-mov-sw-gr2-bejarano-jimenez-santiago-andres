package com.example.gestionvehiculos.controllers

import android.content.Context
import com.example.gestionvehiculos.database.DatabaseHelper
import com.example.gestionvehiculos.models.Vehiculo

class VehiculoController(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addVehiculo(vehiculo: Vehiculo): Long {
        return dbHelper.insertVehiculo(vehiculo)
    }

    fun getVehiculos(): List<Vehiculo> {
        return dbHelper.getAllVehiculos()
    }

    fun getVehiculoById(id: Int): Vehiculo? {
        return dbHelper.getVehiculoById(id)
    }

    fun getVehiculosByPropietarioId(propietarioId: Int): List<Vehiculo> {
        return dbHelper.getVehiculosByPropietarioId(propietarioId)
    }

    fun updateVehiculo(vehiculoId: Int, updatedVehiculo: Vehiculo): Int {
        return dbHelper.updateVehiculo(updatedVehiculo)
    }

    fun deleteVehiculo(vehiculoId: Int): Int {
        return dbHelper.deleteVehiculo(vehiculoId)
    }
}
