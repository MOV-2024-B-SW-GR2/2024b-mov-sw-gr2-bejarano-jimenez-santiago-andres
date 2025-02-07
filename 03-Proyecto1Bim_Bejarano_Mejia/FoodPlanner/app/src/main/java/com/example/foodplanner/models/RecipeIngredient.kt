package com.example.foodplanner.models

data class RecipeIngredient(
    var id: Int = 0,
    var recipeId: Int,
    var ingredientId: Int,
    var quantity: Double,
    var unit: String
)
