package com.example.foodplanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.models.Ingredient

class IngredientDetailAdapter(
    private val ingredients: List<Ingredient>
) : RecyclerView.Adapter<IngredientDetailAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.tvIngredientName)
        private val quantityTextView: TextView = view.findViewById(R.id.tvIngredientQuantity)

        fun bind(ingredient: Ingredient) {
            nameTextView.text = ingredient.name
            quantityTextView.text = ingredient.quantity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount() = ingredients.size
}
