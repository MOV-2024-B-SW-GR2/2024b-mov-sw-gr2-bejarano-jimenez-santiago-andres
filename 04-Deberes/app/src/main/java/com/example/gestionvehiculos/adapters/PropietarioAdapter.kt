package com.example.gestionvehiculos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.controllers.VehiculoController
import com.example.gestionvehiculos.models.Propietario

class PropietarioAdapter(
    private var propietarios: List<Propietario>,
    private val onItemClick: (Propietario) -> Unit
) : RecyclerView.Adapter<PropietarioAdapter.PropietarioViewHolder>() {

    inner class PropietarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNombre: TextView = itemView.findViewById(R.id.tvNombreItemPropietario)
        private val textViewIdentificacion: TextView = itemView.findViewById(R.id.tvIdentificacionItemPropietario)
        private val textViewVehiculos: TextView = itemView.findViewById(R.id.tvVehiculosItemPropietario)
        private val vehiculoController = VehiculoController(itemView.context)

        fun bind(propietario: Propietario) {
            textViewNombre.text = propietario.nombre
            textViewIdentificacion.text = "ID: ${propietario.identificacion}"
            
            // Obtener y mostrar la cantidad de vehículos
            val vehiculos = vehiculoController.getVehiculosByPropietarioId(propietario.id)
            textViewVehiculos.text = "Vehículos: ${vehiculos.size}"

            itemView.setOnClickListener { onItemClick(propietario) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropietarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_propietario, parent, false)
        return PropietarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropietarioViewHolder, position: Int) {
        holder.bind(propietarios[position])
    }

    override fun getItemCount() = propietarios.size

    fun updatePropietarios(newPropietarios: List<Propietario>) {
        propietarios = newPropietarios
        notifyDataSetChanged()
    }
}
