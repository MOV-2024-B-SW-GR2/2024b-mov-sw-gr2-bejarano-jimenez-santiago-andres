package com.example.gestionvehiculos.models

import java.io.Serializable

data class Vehiculo(
    val id: Int,
    var propietarioId: Int?,
    var marca: String,
    var modelo: String,
    var anio: Int,
    var precio: Double,
    var estaMatriculado: Boolean
) : Serializable
