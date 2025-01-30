package com.example.gestionvehiculos.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gestionvehiculos.models.Propietario
import com.example.gestionvehiculos.models.Vehiculo

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GestionVehiculos.db"
        private const val DATABASE_VERSION = 2

        // Tabla Propietarios
        private const val TABLE_PROPIETARIOS = "propietarios"
        private const val COLUMN_PROPIETARIO_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_EDAD = "edad"
        private const val COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_IDENTIFICACION = "identificacion"
        private const val COLUMN_NUM_VEHICULOS = "num_vehiculos"

        // Tabla Vehículos
        private const val TABLE_VEHICULOS = "vehiculos"
        private const val COLUMN_VEHICULO_ID = "id"
        private const val COLUMN_PROPIETARIO_ID_FK = "propietario_id"
        private const val COLUMN_MARCA = "marca"
        private const val COLUMN_MODELO = "modelo"
        private const val COLUMN_ANIO = "anio"
        private const val COLUMN_PRECIO = "precio"
        private const val COLUMN_MATRICULADO = "matriculado"
        private const val COLUMN_LATITUD = "latitud"
        private const val COLUMN_LONGITUD = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de propietarios
        val createPropietariosTable = """
            CREATE TABLE $TABLE_PROPIETARIOS (
                $COLUMN_PROPIETARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_EDAD INTEGER NOT NULL,
                $COLUMN_FECHA_NACIMIENTO TEXT NOT NULL,
                $COLUMN_IDENTIFICACION TEXT NOT NULL,
                $COLUMN_NUM_VEHICULOS INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()

        // Crear tabla de vehículos
        val createVehiculosTable = """
            CREATE TABLE $TABLE_VEHICULOS (
                $COLUMN_VEHICULO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PROPIETARIO_ID_FK INTEGER,
                $COLUMN_MARCA TEXT NOT NULL,
                $COLUMN_MODELO TEXT NOT NULL,
                $COLUMN_ANIO INTEGER NOT NULL,
                $COLUMN_PRECIO REAL NOT NULL,
                $COLUMN_MATRICULADO INTEGER NOT NULL,
                $COLUMN_LATITUD REAL,
                $COLUMN_LONGITUD REAL,
                FOREIGN KEY($COLUMN_PROPIETARIO_ID_FK) REFERENCES $TABLE_PROPIETARIOS($COLUMN_PROPIETARIO_ID)
            )
        """.trimIndent()

        db.execSQL(createPropietariosTable)
        db.execSQL(createVehiculosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Agregar columnas latitud y longitud a la tabla vehículos
            db.execSQL("ALTER TABLE $TABLE_VEHICULOS ADD COLUMN $COLUMN_LATITUD REAL")
            db.execSQL("ALTER TABLE $TABLE_VEHICULOS ADD COLUMN $COLUMN_LONGITUD REAL")
        }
    }

    // Métodos para Propietarios
    fun insertPropietario(propietario: Propietario): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, propietario.nombre)
            put(COLUMN_EDAD, propietario.edad)
            put(COLUMN_FECHA_NACIMIENTO, propietario.fechaNacimiento)
            put(COLUMN_IDENTIFICACION, propietario.identificacion)
            put(COLUMN_NUM_VEHICULOS, propietario.numVehiculos)
        }
        return db.insert(TABLE_PROPIETARIOS, null, values)
    }

    fun getAllPropietarios(): List<Propietario> {
        val propietarios = mutableListOf<Propietario>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PROPIETARIOS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val propietario = Propietario(
                    id = getInt(getColumnIndexOrThrow(COLUMN_PROPIETARIO_ID)),
                    nombre = getString(getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    edad = getInt(getColumnIndexOrThrow(COLUMN_EDAD)),
                    fechaNacimiento = getString(getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)),
                    identificacion = getString(getColumnIndexOrThrow(COLUMN_IDENTIFICACION)),
                    numVehiculos = getInt(getColumnIndexOrThrow(COLUMN_NUM_VEHICULOS))
                )
                propietarios.add(propietario)
            }
        }
        cursor.close()
        return propietarios
    }

    fun updatePropietario(propietario: Propietario): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, propietario.nombre)
            put(COLUMN_EDAD, propietario.edad)
            put(COLUMN_FECHA_NACIMIENTO, propietario.fechaNacimiento)
            put(COLUMN_IDENTIFICACION, propietario.identificacion)
            put(COLUMN_NUM_VEHICULOS, propietario.numVehiculos)
        }
        return db.update(
            TABLE_PROPIETARIOS,
            values,
            "$COLUMN_PROPIETARIO_ID = ?",
            arrayOf(propietario.id.toString())
        )
    }

    fun deletePropietario(propietarioId: Int): Int {
        val db = this.writableDatabase
        // Primero eliminar los vehículos asociados
        db.delete(
            TABLE_VEHICULOS,
            "$COLUMN_PROPIETARIO_ID_FK = ?",
            arrayOf(propietarioId.toString())
        )
        // Luego eliminar el propietario
        return db.delete(
            TABLE_PROPIETARIOS,
            "$COLUMN_PROPIETARIO_ID = ?",
            arrayOf(propietarioId.toString())
        )
    }

    fun getPropietarioById(id: Int): Propietario? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PROPIETARIOS,
            null,
            "$COLUMN_PROPIETARIO_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val propietario = Propietario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROPIETARIO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                edad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD)),
                fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)),
                identificacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IDENTIFICACION)),
                numVehiculos = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM_VEHICULOS))
            )
            cursor.close()
            propietario
        } else {
            cursor.close()
            null
        }
    }

    // Métodos para Vehículos
    fun insertVehiculo(vehiculo: Vehiculo): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            vehiculo.propietarioId?.let { propId ->
                put(COLUMN_PROPIETARIO_ID_FK, propId)
            }
            put(COLUMN_MARCA, vehiculo.marca)
            put(COLUMN_MODELO, vehiculo.modelo)
            put(COLUMN_ANIO, vehiculo.anio)
            put(COLUMN_PRECIO, vehiculo.precio)
            put(COLUMN_MATRICULADO, if (vehiculo.estaMatriculado) 1 else 0)
            put(COLUMN_LATITUD, vehiculo.latitud)
            put(COLUMN_LONGITUD, vehiculo.longitud)
        }
        return db.insert(TABLE_VEHICULOS, null, values)
    }

    fun getAllVehiculos(): List<Vehiculo> {
        val vehiculos = mutableListOf<Vehiculo>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_VEHICULOS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val columnIndex = getColumnIndexOrThrow(COLUMN_PROPIETARIO_ID_FK)
                val propietarioId = if (isNull(columnIndex)) null else getInt(columnIndex)
                
                val vehiculo = Vehiculo(
                    id = getInt(getColumnIndexOrThrow(COLUMN_VEHICULO_ID)),
                    propietarioId = propietarioId,
                    marca = getString(getColumnIndexOrThrow(COLUMN_MARCA)),
                    modelo = getString(getColumnIndexOrThrow(COLUMN_MODELO)),
                    anio = getInt(getColumnIndexOrThrow(COLUMN_ANIO)),
                    precio = getDouble(getColumnIndexOrThrow(COLUMN_PRECIO)),
                    estaMatriculado = getInt(getColumnIndexOrThrow(COLUMN_MATRICULADO)) == 1,
                    latitud = getDouble(getColumnIndexOrThrow(COLUMN_LATITUD)),
                    longitud = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUD))
                )
                vehiculos.add(vehiculo)
            }
        }
        cursor.close()
        return vehiculos
    }

    fun updateVehiculo(vehiculo: Vehiculo): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            vehiculo.propietarioId?.let { propId ->
                put(COLUMN_PROPIETARIO_ID_FK, propId)
            } ?: putNull(COLUMN_PROPIETARIO_ID_FK)
            put(COLUMN_MARCA, vehiculo.marca)
            put(COLUMN_MODELO, vehiculo.modelo)
            put(COLUMN_ANIO, vehiculo.anio)
            put(COLUMN_PRECIO, vehiculo.precio)
            put(COLUMN_MATRICULADO, if (vehiculo.estaMatriculado) 1 else 0)
            put(COLUMN_LATITUD, vehiculo.latitud)
            put(COLUMN_LONGITUD, vehiculo.longitud)
        }
        return db.update(
            TABLE_VEHICULOS,
            values,
            "$COLUMN_VEHICULO_ID = ?",
            arrayOf(vehiculo.id.toString())
        )
    }

    fun deleteVehiculo(vehiculoId: Int): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_VEHICULOS,
            "$COLUMN_VEHICULO_ID = ?",
            arrayOf(vehiculoId.toString())
        )
    }

    fun getVehiculoById(id: Int): Vehiculo? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_VEHICULOS,
            null,
            "$COLUMN_VEHICULO_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(COLUMN_PROPIETARIO_ID_FK)
            val propietarioId = if (cursor.isNull(columnIndex)) null else cursor.getInt(columnIndex)
            
            val vehiculo = Vehiculo(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VEHICULO_ID)),
                propietarioId = propietarioId,
                marca = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MARCA)),
                modelo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODELO)),
                anio = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANIO)),
                precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECIO)),
                estaMatriculado = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MATRICULADO)) == 1,
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD)),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD))
            )
            cursor.close()
            vehiculo
        } else {
            cursor.close()
            null
        }
    }

    fun getVehiculosByPropietarioId(propietarioId: Int): List<Vehiculo> {
        val vehiculos = mutableListOf<Vehiculo>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_VEHICULOS,
            null,
            "$COLUMN_PROPIETARIO_ID_FK = ?",
            arrayOf(propietarioId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val vehiculo = Vehiculo(
                    id = getInt(getColumnIndexOrThrow(COLUMN_VEHICULO_ID)),
                    propietarioId = propietarioId,
                    marca = getString(getColumnIndexOrThrow(COLUMN_MARCA)),
                    modelo = getString(getColumnIndexOrThrow(COLUMN_MODELO)),
                    anio = getInt(getColumnIndexOrThrow(COLUMN_ANIO)),
                    precio = getDouble(getColumnIndexOrThrow(COLUMN_PRECIO)),
                    estaMatriculado = getInt(getColumnIndexOrThrow(COLUMN_MATRICULADO)) == 1,
                    latitud = getDouble(getColumnIndexOrThrow(COLUMN_LATITUD)),
                    longitud = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUD))
                )
                vehiculos.add(vehiculo)
            }
        }
        cursor.close()
        return vehiculos
    }
}
