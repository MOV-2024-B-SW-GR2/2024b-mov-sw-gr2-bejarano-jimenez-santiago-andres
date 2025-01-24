package com.example.gestionvehiculos.models

import java.io.Serializable

data class Propietario(
    val id: Int,
    var nombre: String,
    var edad: Int,
    var fechaNacimiento: String,
    var identificacion: String,
    var numVehiculos: Int
) : Serializable
