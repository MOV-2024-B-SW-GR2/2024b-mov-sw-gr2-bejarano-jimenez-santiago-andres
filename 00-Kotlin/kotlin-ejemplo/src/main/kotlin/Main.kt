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


    calcularSueldo(10.00) // solo parámetro requerido
    calcularSueldo(10.00, 15.00, 20.00) // parámetro requerido y sobrescribir parámetros opcionales

// Named parameters
// calcularSueldo(sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parámetro bonoEspecial en 2da posición
// gracias a los parámetros nombrados

    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)
// usando el parámetro bonoEspecial en 1ra posición
// usando el parámetro sueldo en 2da posición
// usando el parámetro tasa en 3ra posición
// gracias a los parámetros nombrados

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

fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial: Double? = null // Opcional (nullable)
): Double {
    // Variable? -> "?" Es Nullable (o sea que puede en algún momento ser nulo)
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)

    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {
        return sueldo * (100 / tasa) * bonoEspecial
    }
}

abstract class NumerosJava{
    protected val numeroUno:Int
    private val numeroDos: Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract class Numeros( // Constructor Primario
    // Caso 1) Parametro normal
    // uno:Int , (parametro (sin modificador acceso))

    // Caso 2) Parametro y propiedad (atributo) (protected)
    // private var uno: Int (propiedad "instancia.uno")
    protected val numeroUno: Int, // instancia.numeroUno
    protected val numeroDos: Int, // instancia.numeroDos
    parametroNoUsadoNoPropiedadDeLaClase:Int? = null
){
    init { // bloque constructor primario OPCIONAL
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}
