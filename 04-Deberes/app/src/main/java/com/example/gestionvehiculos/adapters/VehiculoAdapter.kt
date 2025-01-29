package com.example.gestionvehiculos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.models.Vehiculo
import com.google.android.material.button.MaterialButton

class VehiculoAdapter(
    private var vehiculos: List<Vehiculo>,
    private val onVerClick: (Vehiculo) -> Unit,
    private val onEditarClick: (Vehiculo) -> Unit,
    private val onEliminarClick: (Vehiculo) -> Unit
) : RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>() {

    inner class VehiculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMarca: TextView = itemView.findViewById(R.id.tvMarcaItemVehiculo)
        val btnVer: MaterialButton = itemView.findViewById(R.id.btnVerItemVehiculo)
        val btnEditar: MaterialButton = itemView.findViewById(R.id.btnEditarItemVehiculo)
        val btnEliminar: MaterialButton = itemView.findViewById(R.id.btnEliminarItemVehiculo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehiculo, parent, false)
        return VehiculoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        val vehiculo = vehiculos[position]
        holder.tvMarca.text = "${vehiculo.marca} ${vehiculo.modelo} (${vehiculo.anio})"
        
        holder.btnVer.setOnClickListener { onVerClick(vehiculo) }
        holder.btnEditar.setOnClickListener { onEditarClick(vehiculo) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(vehiculo) }
    }

    override fun getItemCount() = vehiculos.size

    fun updateVehiculos(newVehiculos: List<Vehiculo>) {
        vehiculos = newVehiculos
        notifyDataSetChanged()
    }
}
