package com.example.gestionvehiculos.controllers

import com.example.gestionvehiculos.models.Vehiculo

class VehiculoController {
    private val vehiculoList = mutableListOf<Vehiculo>()

    fun addVehiculo(vehiculo: Vehiculo) {
        vehiculoList.add(vehiculo)
    }

    fun getVehiculos(): List<Vehiculo> {
        return vehiculoList
    }

    fun deleteVehiculo(vehiculoId: Int) {
        vehiculoList.removeIf { it.id == vehiculoId }
    }

    fun updateVehiculo(vehiculoId: Int, updatedVehiculo: Vehiculo) {
        val index = vehiculoList.indexOfFirst { it.id == vehiculoId }
        if (index != -1) {
            vehiculoList[index] = updatedVehiculo
        }
    }
}
