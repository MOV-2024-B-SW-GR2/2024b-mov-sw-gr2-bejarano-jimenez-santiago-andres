package org.example

import java.util.*

fun main(args: Array<String>) {
    //INMUTABLES (No se pueden cambiar, solo se pueden leer, no se REASIGNAN "=")
    val inmutable: String = "Santiago"
    //inmutable = "Bejarano" //Error!

    //MUTABLES (Se pueden cambiar, se pueden leer y reasignar)
    var mutable: String = "Bejarano"
    mutable = "Santiago" //Correcto


    // Duck Typing
    val ejemploVariable = " Santiago Bejarano "
    ejemploVariable.trim()

    val edadEjemplo: Int = 12
    // ejemploVariable = edadEjemplo // Error!

    // Variables Primitivas
    val nombreProfesor: String = "Santiago Bejarano"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    // Clases en Java
    val fechaNacimiento: Date = Date()


    // Condicionales
    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        "C" -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }

    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito

    imprimirNombre("SANTIAGO")
}

fun imprimirNombre(nombre: String): Unit {
    fun otraFuncionAdentro() {
        println("Otra funcion adentro")
    }

    println("Nombre: $nombre") // Uso sin llaves
    println("Nombre: ${nombre}") // Uso con llaves opcional
    println("Nombre: ${nombre + nombre}") // Uso con llaves (concatenando)
    println("Nombre: ${nombre.toString()}") // Uso con llaves (funcion)
    println("Nombre: $nombre.toString()") // INCORRECTO! (no pueden usar sin llaves)

    otraFuncionAdentro()
}