package com.example.foodplanner.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.adapters.RecipeAdapter
import com.example.foodplanner.controllers.RecipeController
import com.example.foodplanner.models.Recipe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recipeController: RecipeController
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeController = RecipeController(this)
        setupRecyclerView()
        setupFab()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvRecipes)
        adapter = RecipeAdapter(
            onItemClick = { recipe -> showRecipeDetails(recipe) },
            onOptionsClick = { view, recipe -> showRecipeOptions(view, recipe) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        loadRecipes()
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAddRecipe).setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showRecipeDetails(recipe: Recipe) {
        val intent = Intent(this, EditRecipeActivity::class.java).apply {
            putExtra(EditRecipeActivity.EXTRA_RECIPE_ID, recipe.id)
            putExtra(EditRecipeActivity.EXTRA_VIEW_MODE, true)
        }
        startActivity(intent)
    }

    private fun showRecipeOptions(view: android.view.View, recipe: Recipe) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.menu_recipe_options, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        val intent = Intent(this@MainActivity, EditRecipeActivity::class.java).apply {
                            putExtra(EditRecipeActivity.EXTRA_RECIPE_ID, recipe.id)
                            putExtra(EditRecipeActivity.EXTRA_VIEW_MODE, false)
                        }
                        startActivity(intent)
                        true
                    }
                    R.id.action_delete -> {
                        deleteRecipe(recipe)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        recipeController.deleteRecipe(recipe.id).onSuccess {
            loadRecipes()
        }
    }

    private fun loadRecipes() {
        recipeController.getAllRecipes().onSuccess { recipes ->
            adapter.submitList(recipes)
        }
    }

    override fun onResume() {
        super.onResume()
        loadRecipes()
    }
}
