package com.example.gestionvehiculos.controllers

import com.example.gestionvehiculos.models.Propietario

class PropietarioController {
    private val propietarioList = mutableListOf<Propietario>()

    fun addPropietario(propietario: Propietario) {
        propietarioList.add(propietario)
    }

    fun getPropietarios(): List<Propietario> {
        return propietarioList
    }

    fun deletePropietario(propietarioId: Int) {
        propietarioList.removeIf { it.id == propietarioId }
    }

    fun updatePropietario(propietarioId: Int, updatedPropietario: Propietario) {
        val index = propietarioList.indexOfFirst { it.id == propietarioId }
        if (index != -1) {
            propietarioList[index] = updatedPropietario
        }
    }
}
