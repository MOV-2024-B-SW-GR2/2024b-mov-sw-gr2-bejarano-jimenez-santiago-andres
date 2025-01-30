package com.example.gestionvehiculos.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.PropietarioController
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Propietario
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.button.MaterialButton

class EditarVehiculoActivity : AppCompatActivity() {
    private lateinit var vehiculoController: VehiculoController
    private lateinit var propietarioController: PropietarioController
    private var currentVehiculo: Vehiculo? = null
    private var selectedPropietarioId: Int? = null

    // Coordenadas de concesionarios por marca
    private val coordenadasPorMarca = mapOf(
        "BYD" to Pair(-0.16328037230738046, -78.46409657195679),
        "CHANGAN" to Pair(-0.16353122878216622, -78.4642849448021),
        "SUSUKI" to Pair(-0.16321582258698988, -78.46482447210695),
        "TOYOTA" to Pair(-0.1636715635643306, -78.46455351702981),
        "FORD" to Pair(-0.16485347627556404, -78.46561662803389),
        "AMBACAR" to Pair(-0.1645256603191884, -78.46580127962027),
        "KIA" to Pair(-0.16468639035102312, -78.4653176997908),
        "SINOTRUCK" to Pair(-0.16538906981238766, -78.46677223588343),
        "CHEVROLET" to Pair(-0.16555681438051725, -78.4679061280807),
        "GWM" to Pair(-0.1662764345326828, -78.46873541023623),
        "MG" to Pair(-0.16605680110745694, -78.46914974496825),
        "JAC" to Pair(-0.16602260308678954, -78.46936231003363),
        "AUDI" to Pair(-0.16794788735268443, -78.47142478035467),
        "VOLKSWAGEN" to Pair(-0.1680397526147777, -78.47160582946398),
        "GEELY" to Pair(-0.16855660555067054, -78.47268104449452),
        "SUBARU" to Pair(-0.16861024949772352, -78.47296267644235),
        "MAZDA" to Pair(-0.16832063422091117, -78.47397313747251),
        "CHERY" to Pair(-0.16836623157645772, -78.4741763148063),
        "DONGFENG" to Pair(-0.16845442771465383, -78.47436127254727),
        "RAM" to Pair(-0.1685241648465135, -78.47458456644826),
        "DODGE" to Pair(-0.1685241648465135, -78.47458456644826),
        "JEEP" to Pair(-0.1685241648465135, -78.47458456644826),
        "FIAT" to Pair(-0.1685241648465135, -78.47458456644826),
        "NISSAN" to Pair(-0.16900741087889484, -78.4750559570922)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_vehiculo)

        // Inicializar controladores
        vehiculoController = VehiculoController(this)
        propietarioController = PropietarioController(this)

        // Obtener el vehículo si estamos editando
        currentVehiculo = intent.getSerializableExtra("vehiculo") as? Vehiculo

        // Configurar Spinner de marcas
        val spinnerMarca = findViewById<Spinner>(R.id.spinnerMarca)
        val marcas = coordenadasPorMarca.keys.toList().sorted()
        val adapterMarcas = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapterMarcas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarca.adapter = adapterMarcas

        // Configurar campos de entrada
        val etModelo = findViewById<EditText>(R.id.etModeloEditarVehiculo)
        val etAnio = findViewById<EditText>(R.id.etAnioEditarVehiculo)
        val etPrecio = findViewById<EditText>(R.id.etPrecioEditarVehiculo)
        val cbMatriculado = findViewById<CheckBox>(R.id.cbMatriculadoEditarVehiculo)
        val spinnerPropietario = findViewById<Spinner>(R.id.spinnerPropietarioEditarVehiculo)

        // Cargar propietarios en el spinner
        val propietarios = propietarioController.getPropietarios()
        val propietariosAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            propietarios.map { "${it.nombre} (${it.identificacion})" }
        )
        propietariosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPropietario.adapter = propietariosAdapter

        // Si estamos editando, cargar datos del vehículo
        currentVehiculo?.let { vehiculo ->
            // Seleccionar la marca actual
            val marcaIndex = marcas.indexOf(vehiculo.marca)
            if (marcaIndex != -1) {
                spinnerMarca.setSelection(marcaIndex)
            }

            etModelo.setText(vehiculo.modelo)
            etAnio.setText(vehiculo.anio.toString())
            etPrecio.setText(vehiculo.precio.toString())
            cbMatriculado.isChecked = vehiculo.estaMatriculado

            // Seleccionar el propietario actual
            vehiculo.propietarioId?.let { propId ->
                val propietario = propietarioController.getPropietarioById(propId)
                propietario?.let {
                    val index = propietarios.indexOf(it)
                    if (index != -1) {
                        spinnerPropietario.setSelection(index)
                    }
                }
            }
        }

        // Manejar selección de propietario
        spinnerPropietario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPropietarioId = propietarios[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPropietarioId = null
            }
        }

        // Configurar botón de guardar
        findViewById<MaterialButton>(R.id.btnGuardarVehiculo).setOnClickListener {
            val marca = marcas[spinnerMarca.selectedItemPosition]
            val coordenadas = coordenadasPorMarca[marca]

            val vehiculo = Vehiculo(
                id = currentVehiculo?.id ?: 0,
                propietarioId = selectedPropietarioId,
                marca = marca,
                modelo = etModelo.text.toString(),
                anio = etAnio.text.toString().toIntOrNull() ?: 0,
                precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0,
                estaMatriculado = cbMatriculado.isChecked,
                latitud = coordenadas?.first,
                longitud = coordenadas?.second
            )

            if (currentVehiculo == null) {
                vehiculoController.addVehiculo(vehiculo)
            } else {
                vehiculoController.updateVehiculo(currentVehiculo!!.id, vehiculo)
            }

            finish()
        }
    }
}
