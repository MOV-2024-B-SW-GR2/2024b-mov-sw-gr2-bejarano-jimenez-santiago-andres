package com.example.gestionvehiculos.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvehiculos.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapaVehiculoActivity : AppCompatActivity(), OnMapReadyCallback {
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var marca: String = ""
    private lateinit var mMap: GoogleMap

    companion object {
        private const val TAG = "MapaVehiculoActivity"
        private const val DEFAULT_ZOOM = 17f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_vehiculo)

        // Obtener datos del intent
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)
        marca = intent.getStringExtra("marca") ?: ""

        Log.d(TAG, "onCreate - Coordenadas recibidas: ($latitud, $longitud) para marca: $marca")

        // Validar coordenadas
        if (latitud == 0.0 && longitud == 0.0) {
            Log.e(TAG, "Coordenadas inválidas recibidas")
            Toast.makeText(this, "Error: Coordenadas no válidas", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Inicializar el mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady llamado")
        mMap = googleMap

        try {
            // Configurar el mapa
            mMap.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL
                isTrafficEnabled = true
                uiSettings.apply {
                    isZoomControlsEnabled = true
                    isZoomGesturesEnabled = true
                    isScrollGesturesEnabled = true
                    isCompassEnabled = true
                    isMapToolbarEnabled = true
                    isMyLocationButtonEnabled = true
                }
            }

            // Crear ubicación y marcador
            val ubicacion = LatLng(latitud, longitud)
            Log.d(TAG, "Creando marcador en ubicación: $ubicacion")

            // Añadir marcador con color personalizado
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(ubicacion)
                    .title("Concesionario $marca")
                    .snippet("Ubicación del concesionario autorizado")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )

            // Mover cámara inmediatamente a la ubicación
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, DEFAULT_ZOOM))

            // Mostrar el info window del marcador
            marker?.showInfoWindow()

            // Realizar un zoom suave adicional
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(ubicacion, DEFAULT_ZOOM),
                1500,
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        Log.d(TAG, "Animación de cámara completada")
                    }
                    override fun onCancel() {
                        Log.d(TAG, "Animación de cámara cancelada")
                    }
                }
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error al configurar el mapa: ${e.message}", e)
            Toast.makeText(
                this,
                "Error al mostrar la ubicación: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
