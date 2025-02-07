package com.example.foodplanner.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.adapters.IngredientAdapter
import com.example.foodplanner.controllers.IngredientController
import com.example.foodplanner.controllers.RecipeController
import com.example.foodplanner.models.Ingredient
import com.example.foodplanner.models.Recipe
import com.google.android.material.textfield.TextInputEditText

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var recipeController: RecipeController
    private lateinit var ingredientController: IngredientController
    private var selectedImageUri: Uri? = null
    private var recipeId: Int = 0
    private val ingredients = mutableListOf<Ingredient>()
    private lateinit var ingredientAdapter: IngredientAdapter
    private var isViewMode = false

    // UI Components
    private lateinit var nameEditText: TextInputEditText
    private lateinit var stepsEditText: TextInputEditText

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        const val EXTRA_RECIPE_ID = "recipe_id"
        const val EXTRA_VIEW_MODE = "view_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipeController = RecipeController(this)
        ingredientController = IngredientController(this)
        recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0)
        isViewMode = intent.getBooleanExtra(EXTRA_VIEW_MODE, false)

        setupViews()
        setupRecyclerView()
        
        if (!isViewMode) {
            setupListeners()
        } else {
            setViewMode()
        }
        
        if (recipeId != 0) {
            loadRecipe()
        }
    }

    private fun setViewMode() {
        // Deshabilitar edición
        nameEditText.isEnabled = false
        stepsEditText.isEnabled = false
        
        // Ocultar botones de edición
        findViewById<View>(R.id.cvRecipeImage).isClickable = false
        findViewById<View>(R.id.btnAddIngredient).visibility = View.GONE
        findViewById<View>(R.id.btnSaveRecipe).visibility = View.GONE
        
        // Deshabilitar selección de categoría
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.rgCategory)
        chipGroup.isEnabled = false
        for (i in 0 until chipGroup.childCount) {
            chipGroup.getChildAt(i).isEnabled = false
        }
        
        // Cambiar título
        supportActionBar?.title = "Detalles de la Receta"
    }

    private fun setupViews() {
        nameEditText = findViewById(R.id.etRecipeName)
        stepsEditText = findViewById(R.id.etSteps)
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvIngredients)
        ingredientAdapter = IngredientAdapter(ingredients) { position ->
            if (!isViewMode) {
                ingredients.removeAt(position)
                ingredientAdapter.notifyItemRemoved(position)
            }
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditRecipeActivity)
            adapter = ingredientAdapter
        }
    }

    private fun setupListeners() {
        findViewById<com.google.android.material.card.MaterialCardView>(R.id.cvRecipeImage).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAddIngredient).setOnClickListener {
            showAddIngredientDialog()
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSaveRecipe).setOnClickListener {
            saveRecipe()
        }
    }

    private fun loadRecipe() {
        recipeController.getRecipeById(recipeId).onSuccess { recipe ->
            recipe?.let {
                nameEditText.setText(it.name)
                stepsEditText.setText(it.steps)
                
                val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.rgCategory)
                when (it.category) {
                    "Desayuno" -> chipGroup.check(R.id.chipBreakfast)
                    "Almuerzo" -> chipGroup.check(R.id.chipLunch)
                    "Merienda" -> chipGroup.check(R.id.chipSnack)
                }
                
                it.imageUri?.let { uri ->
                    selectedImageUri = Uri.parse(uri)
                    Glide.with(this)
                        .load(selectedImageUri)
                        .into(findViewById(R.id.ivRecipeImage))
                    if (!isViewMode) {
                        findViewById<android.widget.TextView>(R.id.tvAddImage).text = "Cambiar imagen"
                    } else {
                        findViewById<android.widget.TextView>(R.id.tvAddImage).visibility = View.GONE
                    }
                }
                
                loadIngredients()
            } ?: run {
                Toast.makeText(this, "No se encontró la receta", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.onFailure {
            Toast.makeText(this, "Error al cargar la receta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadIngredients() {
        ingredientController.getIngredientsForRecipe(recipeId).onSuccess { loadedIngredients ->
            ingredients.clear()
            ingredients.addAll(loadedIngredients)
            ingredientAdapter.notifyDataSetChanged()
        }.onFailure {
            Toast.makeText(this, "Error al cargar los ingredientes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddIngredientDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_ingredient, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.etIngredientName)
        val quantityInput = dialogView.findViewById<TextInputEditText>(R.id.etIngredientQuantity)

        AlertDialog.Builder(this)
            .setTitle("Agregar Ingrediente")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val name = nameInput.text.toString()
                val quantity = quantityInput.text.toString()
                if (name.isNotBlank() && quantity.isNotBlank()) {
                    val ingredient = Ingredient(name = name, quantity = quantity)
                    ingredients.add(ingredient)
                    ingredientAdapter.notifyItemInserted(ingredients.size - 1)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveRecipe() {
        val name = nameEditText.text.toString()
        val steps = stepsEditText.text.toString()
        
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.rgCategory)
        val category = when (chipGroup.checkedChipId) {
            R.id.chipBreakfast -> "Desayuno"
            R.id.chipLunch -> "Almuerzo"
            R.id.chipSnack -> "Merienda"
            else -> ""
        }

        if (name.isBlank() || category.isBlank() || steps.isBlank()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = Recipe(
            id = recipeId,
            name = name,
            category = category,
            imageUri = selectedImageUri?.toString(),
            steps = steps
        )

        val saveResult = if (recipeId == 0) {
            recipeController.insertRecipe(recipe)
        } else {
            recipeController.updateRecipe(recipe)
        }

        saveResult.onSuccess { newRecipeId ->
            if (recipeId == 0) recipeId = newRecipeId.toInt()
            
            // Guardar ingredientes
            ingredients.forEach { ingredient ->
                ingredientController.insertIngredient(ingredient).onSuccess { ingredientId ->
                    ingredientController.addIngredientToRecipe(recipeId, ingredientId.toInt())
                }
            }

            Toast.makeText(this, "Receta guardada exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        }.onFailure {
            Toast.makeText(this, "Error al guardar la receta", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                Glide.with(this)
                    .load(it)
                    .into(findViewById(R.id.ivRecipeImage))
                findViewById<android.widget.TextView>(R.id.tvAddImage).text = "Cambiar imagen"
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
