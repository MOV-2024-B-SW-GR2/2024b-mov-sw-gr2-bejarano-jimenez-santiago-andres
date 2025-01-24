package com.example.gestionvehiculos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.models.Vehiculo

class VehiculoAdapter(
    private val vehiculoList: List<Vehiculo>,
    private val onVerClick: (Vehiculo) -> Unit,
    private val onEditarClick: (Vehiculo) -> Unit,
    private val onEliminarClick: (Vehiculo) -> Unit
) : RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>() {

    class VehiculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMarca: TextView = itemView.findViewById(R.id.tvMarcaItemVehiculo)
        val buttonVer: Button = itemView.findViewById(R.id.btnVerItemVehiculo)
        val buttonEditar: Button = itemView.findViewById(R.id.btnEditarItemVehiculo)
        val buttonEliminar: Button = itemView.findViewById(R.id.btnEliminarItemVehiculo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehiculo, parent, false)
        return VehiculoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        val vehiculo = vehiculoList[position]
        holder.textViewMarca.text = vehiculo.marca
        holder.buttonVer.setOnClickListener { onVerClick(vehiculo) }
        holder.buttonEditar.setOnClickListener { onEditarClick(vehiculo) }
        holder.buttonEliminar.setOnClickListener { onEliminarClick(vehiculo) }
    }

    override fun getItemCount(): Int = vehiculoList.size
}
