package com.example.foodplanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.models.Recipe

class RecipeAdapter(
    private val onItemClick: (Recipe) -> Unit,
    private val onOptionsClick: (View, Recipe) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvRecipeName)
        val categoryTextView: TextView = view.findViewById(R.id.tvRecipeCategory)
        val imageView: ImageView = view.findViewById(R.id.ivRecipeImage)
        val optionsButton: View = view.findViewById(R.id.btnOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        
        holder.nameTextView.text = recipe.name
        holder.categoryTextView.text = recipe.category
        
        // Load image using Glide if imageUri exists
        recipe.imageUri?.let { uri ->
            Glide.with(holder.imageView.context)
                .load(uri)
                .placeholder(R.drawable.placeholder_recipe)
                .error(R.drawable.placeholder_recipe)
                .centerCrop()
                .into(holder.imageView)
        } ?: run {
            holder.imageView.setImageResource(R.drawable.placeholder_recipe)
        }

        holder.itemView.setOnClickListener { onItemClick(recipe) }
        holder.optionsButton.setOnClickListener { view -> onOptionsClick(view, recipe) }
    }
}

private class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}
