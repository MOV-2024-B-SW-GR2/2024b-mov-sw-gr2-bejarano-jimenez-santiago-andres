package com.example.foodplanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.models.Ingredient

class IngredientAdapter(
    private val ingredients: List<Ingredient>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.tvIngredientName)
        val quantityText: TextView = view.findViewById(R.id.tvIngredientQuantity)
        val deleteButton: ImageButton = view.findViewById(R.id.btnDeleteIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.nameText.text = ingredient.name
        holder.quantityText.text = ingredient.quantity
        holder.deleteButton.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount() = ingredients.size
}
