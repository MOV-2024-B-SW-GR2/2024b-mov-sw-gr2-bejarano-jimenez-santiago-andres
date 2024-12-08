package org.example

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

data class Propietario(
    val id: Int,
    var nombre: String,
    var edad: Int,
    var fechaNacimiento: Date,
    var identificacion: String,
    var numVehiculos: Int
)

data class Vehiculo(
    val id: Int,
    var propietarioId: Int,
    var marca: String,
    var modelo: String,
    var anio: Int,
    var precio: Double,
    var estaMatriculado: Boolean
)



class CRUD {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd") // Formato de fecha
    private val propietariosFile = File("propietarios.txt")
    private val vehiculosFile = File("vehiculos.txt")

    private val propietarios = mutableListOf<Propietario>()
    private val vehiculos = mutableListOf<Vehiculo>()

    init {
        loadFiles()
    }

    private fun calcularEdad(fechaNacimiento: Date): Int {
        val nacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val hoy = LocalDate.now()
        val edad = Period.between(nacimiento, hoy).years
        if (edad <= 0) {
            throw IllegalArgumentException("La fecha de nacimiento no puede resultar en una edad de 0 años o menos.")
        }
        return edad
    }


    // Cargar datos desde archivos
    private fun loadFiles() {
        if (propietariosFile.exists()) {
            propietariosFile.readLines().forEach { line ->
                val data = line.split(",")
                propietarios.add(
                    Propietario(
                        data[0].toInt(),
                        data[1],
                        data[2].toInt(),
                        dateFormatter.parse(data[3]), // Convertimos el string a Date
                        data[4],
                        data[5].toInt()
                    )
                )
            }
        }

        if (vehiculosFile.exists()) {
            vehiculosFile.readLines().forEach { line ->
                val data = line.split(",")
                vehiculos.add(
                    Vehiculo(
                        data[0].toInt(),
                        data[1].toInt(),
                        data[2],
                        data[3],
                        data[4].toInt(),
                        data[5].toDouble(),
                        data[6].toBoolean()
                    )
                )
            }
        }
    }

    // Guardar datos en archivos
    private fun saveFiles() {
        propietariosFile.writeText(propietarios.joinToString("\n") { p ->
            "${p.id},${p.nombre},${p.edad},${dateFormatter.format(p.fechaNacimiento)},${p.identificacion},${p.numVehiculos}"
        })

        vehiculosFile.writeText(vehiculos.joinToString("\n") { v ->
            "${v.id},${v.propietarioId},${v.marca},${v.modelo},${v.anio},${v.precio},${v.estaMatriculado}"
        })
    }

    // CRUD Propietarios
    fun createPropietario() {
        println("Ingrese los datos del propietario:")
        val nombre = leerTextoNoVacio("Nombre: ")
        val fechaNacimiento = leerFechaConValidacion("Fecha de nacimiento (YYYY-MM-DD): ")
        val edad = calcularEdad(fechaNacimiento)
        println("Edad calculada: $edad años")
        val identificacion = leerIdentificacion("Identificación (10 dígitos): ")

        val id = if (propietarios.isEmpty()) 1 else propietarios.maxOf { it.id } + 1
        val nuevoPropietario = Propietario(id, nombre, edad, fechaNacimiento, identificacion, 0)
        propietarios.add(nuevoPropietario)
        saveFiles()
        println("Propietario creado exitosamente.")
    }


    fun displayPropietarios() {
        if (propietarios.isEmpty()) {
            println("No hay propietarios registrados.")
        } else {
            println("\n--- Lista de Propietarios ---")
            propietarios.forEach { p ->
                println("""
                Propietario ID: ${p.id}
                Nombre: ${p.nombre}
                Edad: ${p.edad} años
                Fecha de Nacimiento: ${dateFormatter.format(p.fechaNacimiento)}
                Identificación: ${p.identificacion}
                Número de Vehículos Registrados: ${p.numVehiculos}
                -------------------------------
            """.trimIndent())
            }
        }
    }

    fun updatePropietario() {
        val id = leerEntero("Ingrese el ID del propietario a actualizar: ")
        val propietario = propietarios.find { it.id == id }
        if (propietario != null) {
            while (true) {
                println("\nSeleccione el campo que desea actualizar:")
                println("1. Nombre")
                println("2. Fecha de nacimiento (recalcular edad)")
                println("3. Identificación")
                println("4. Salir")

                when (leerEntero("Opción: ")) {
                    1 -> propietario.nombre = leerTextoNoVacio("Nuevo nombre: ")
                    2 -> {
                        propietario.fechaNacimiento = leerFechaConValidacion("Nueva fecha de nacimiento (YYYY-MM-DD): ")
                        propietario.edad = calcularEdad(propietario.fechaNacimiento)
                        println("Edad recalculada: ${propietario.edad} años")
                    }
                    3 -> propietario.identificacion = leerIdentificacion("Nueva identificación (10 dígitos): ")
                    4 -> {
                        saveFiles()
                        println("Propietario actualizado exitosamente.")
                        return
                    }
                    else -> println("Opción inválida.")
                }
            }
        } else {
            println("Propietario no encontrado.")
        }
    }


    fun deletePropietario() {
        println("Ingrese el ID del propietario a eliminar:")
        val id = readln().toInt()
        if (propietarios.removeIf { it.id == id }) {
            vehiculos.removeIf { it.propietarioId == id }
            saveFiles()
            println("Propietario eliminado exitosamente.")
        } else {
            println("Propietario no encontrado.")
        }
    }

    // CRUD Vehículos
    fun createVehiculo() {
        val propietarioId = leerEntero("Ingrese el ID del propietario: ")
        if (propietarios.any { it.id == propietarioId }) {
            println("Ingrese los datos del vehículo:")
            val marca = leerTextoNoVacio("Marca: ")
            val modelo = leerTextoNoVacio("Modelo: ")
            val anio = leerEntero("Año: ")
            val precio = leerDouble("Precio: ")
            val estaMatriculado = leerBooleano("¿Está matriculado? (true/false): ")

            val id = if (vehiculos.isEmpty()) 1 else vehiculos.maxOf { it.id } + 1
            val nuevoVehiculo = Vehiculo(id, propietarioId, marca, modelo, anio, precio, estaMatriculado)
            vehiculos.add(nuevoVehiculo)
            val propietario = propietarios.find { it.id == propietarioId }!!
            propietario.numVehiculos++
            saveFiles()
            println("Vehículo creado exitosamente.")
        } else {
            println("Propietario no encontrado.")
        }
    }

    fun displayVehiculos() {
        if (vehiculos.isEmpty()) {
            println("No hay vehículos registrados.")
        } else {
            println("\n--- Lista de Vehículos ---")
            vehiculos.forEach { v ->
                println("""
                Vehículo ID: ${v.id}
                ID del Propietario: ${v.propietarioId}
                Marca: ${v.marca}
                Modelo: ${v.modelo}
                Año: ${v.anio}
                Precio: $${"%.2f".format(v.precio)}
                ¿Está Matriculado?: ${if (v.estaMatriculado) "Sí" else "No"}
                -------------------------------
                """.trimIndent())
            }
        }
    }

    fun updateVehiculo() {
        val id = leerEntero("Ingrese el ID del vehículo a actualizar: ")
        val vehiculo = vehiculos.find { it.id == id }
        if (vehiculo != null) {
            while (true) {
                println("\nSeleccione el campo que desea actualizar:")
                println("1. Marca")
                println("2. Modelo")
                println("3. Año")
                println("4. Precio")
                println("5. ¿Está matriculado?")
                println("6. Salir")

                when (leerEntero("Opción: ")) {
                    1 -> vehiculo.marca = leerTextoNoVacio("Nueva marca: ")
                    2 -> vehiculo.modelo = leerTextoNoVacio("Nuevo modelo: ")
                    3 -> vehiculo.anio = leerEntero("Nuevo año: ")
                    4 -> vehiculo.precio = leerDouble("Nuevo precio: ")
                    5 -> vehiculo.estaMatriculado = leerBooleano("¿Está matriculado? (true/false): ")
                    6 -> {
                        saveFiles()
                        println("Vehículo actualizado exitosamente.")
                        return
                    }
                    else -> println("Opción inválida.")
                }
            }
        } else {
            println("Vehículo no encontrado.")
        }
    }

    fun deleteVehiculo() {
        println("Ingrese el ID del vehículo a eliminar:")
        val id = readln().toInt()
        if (vehiculos.removeIf { it.id == id }) {
            saveFiles()
            println("Vehículo eliminado exitosamente.")
        } else {
            println("Vehículo no encontrado.")
        }
    }

    // Validaciones

    private fun leerEntero(prompt: String): Int {
        while (true) {
            try {
                print(prompt)
                val valor = readln().toInt()
                if (valor <= 0) throw IllegalArgumentException("El número debe ser mayor a 0.")
                return valor
            } catch (e: NumberFormatException) {
                println("Por favor, ingrese un número válido.")
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun leerDouble(prompt: String): Double {
        while (true) {
            try {
                print(prompt)
                val valor = readln().toDouble()
                if (valor < 0) throw IllegalArgumentException("El valor debe ser mayor o igual a 0.")
                return valor
            } catch (e: NumberFormatException) {
                println("Por favor, ingrese un número decimal válido.")
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun leerBooleano(prompt: String): Boolean {
        while (true) {
            try {
                print(prompt)
                val entrada = readln().lowercase()
                if (entrada == "true" || entrada == "false") {
                    return entrada.toBoolean()
                } else {
                    throw IllegalArgumentException("Por favor, ingrese 'true' o 'false'.")
                }
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun leerFechaConValidacion(prompt: String): Date {
        while (true) {
            try {
                print(prompt)
                val fecha = dateFormatter.parse(readln())
                val localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if (localDate.isAfter(LocalDate.now())) {
                    println("La fecha no puede estar en el futuro.")
                } else {
                    calcularEdad(fecha) // Validamos que la edad sea mayor a 0.
                    return fecha
                }
            } catch (e: Exception) {
                println("Formato de fecha inválido. Asegúrese de usar el formato YYYY-MM-DD y que la fecha sea válida.")
            }
        }
    }


    private fun leerTextoNoVacio(prompt: String): String {
        while (true) {
            print(prompt)
            val entrada = readln().trim()
            if (entrada.isNotEmpty()) return entrada
            println("Este campo no puede estar vacío. Intente nuevamente.")
        }
    }

    private fun leerIdentificacion(prompt: String): String {
        while (true) {
            print(prompt)
            val identificacion = readln().trim()
            if (identificacion.matches(Regex("\\d{10}"))) {
                return identificacion
            } else {
                println("La identificación debe contener exactamente 10 dígitos numéricos.")
            }
        }
    }


}

fun main() {
    val crud = CRUD()
    while (true) {
        println("\nSeleccione una opción:")
        println("1. Gestión de Propietarios")
        println("2. Gestión de Vehículos")
        println("3. Salir")
        println("Ingrese su opción: ")

        when (readln().toInt()) {
            1 -> submenuPropietarios(crud)
            2 -> submenuVehiculos(crud)
            3 -> {
                println("¡Hasta luego!")
                return
            }
            else -> println("Opción inválida.")
        }
    }
}

fun submenuPropietarios(crud: CRUD) {
    while (true) {
        println("\n--- Gestión de Propietarios ---")
        println("1. Crear Propietario")
        println("2. Leer Propietarios")
        println("3. Actualizar Propietario")
        println("4. Eliminar Propietario")
        println("5. Volver al menú principal")
        println("Ingrese su opción: ")

        when (readln().toInt()) {
            1 -> crud.createPropietario()
            2 -> crud.displayPropietarios()
            3 -> crud.updatePropietario()
            4 -> crud.deletePropietario()
            5 -> return
            else -> println("Opción inválida.")
        }
    }
}

fun submenuVehiculos(crud: CRUD) {
    while (true) {
        println("\n--- Gestión de Vehículos ---")
        println("1. Crear Vehículo")
        println("2. Leer Vehículos")
        println("3. Actualizar Vehículo")
        println("4. Eliminar Vehículo")
        println("5. Volver al menú principal")
        println("Ingrese su opción: ")

        when (readln().toInt()) {
            1 -> crud.createVehiculo()
            2 -> crud.displayVehiculos()
            3 -> crud.updateVehiculo()
            4 -> crud.deleteVehiculo()
            5 -> return
            else -> println("Opción inválida.")
        }
    }
}
