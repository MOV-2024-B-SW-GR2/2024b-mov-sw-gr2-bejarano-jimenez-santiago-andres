package com.example.gestionvehiculos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvehiculos.R
import com.example.gestionvehiculos.models.Propietario

class PropietarioAdapter(
    private val propietarioList: List<Propietario>,
    private val onVerClick: (Propietario) -> Unit,
    private val onEditarClick: (Propietario) -> Unit,
    private val onEliminarClick: (Propietario) -> Unit
) : RecyclerView.Adapter<PropietarioAdapter.PropietarioViewHolder>() {

    class PropietarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombre: TextView = itemView.findViewById(R.id.tvNombreItemPropietario)
        val buttonVer: Button = itemView.findViewById(R.id.btnVerItemPropietario)
        val buttonEditar: Button = itemView.findViewById(R.id.btnEditarItemPropietario)
        val buttonEliminar: Button = itemView.findViewById(R.id.btnEliminarItemPropietario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropietarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_propietario, parent, false)
        return PropietarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropietarioViewHolder, position: Int) {
        val propietario = propietarioList[position]
        holder.textViewNombre.text = propietario.nombre
        holder.buttonVer.setOnClickListener { onVerClick(propietario) }
        holder.buttonEditar.setOnClickListener { onEditarClick(propietario) }
        holder.buttonEliminar.setOnClickListener { onEliminarClick(propietario) }
    }

    override fun getItemCount(): Int = propietarioList.size
}
