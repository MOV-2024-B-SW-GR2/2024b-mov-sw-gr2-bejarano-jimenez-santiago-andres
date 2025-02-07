package com.example.foodplanner.controllers

import android.content.Context
import com.example.foodplanner.database.DatabaseHelper
import com.example.foodplanner.models.Ingredient

class IngredientController(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertIngredient(ingredient: Ingredient): Result<Long> {
        return try {
            if (!ingredient.validate()) {
                Result.failure(IllegalArgumentException("Datos de ingrediente inválidos"))
            } else {
                val db = dbHelper.writableDatabase
                val id = db.insert(DatabaseHelper.TABLE_INGREDIENT, null, ingredient.toContentValues())
                db.close()
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getIngredientsForRecipe(recipeId: Int): Result<List<Ingredient>> {
        return try {
            val ingredients = mutableListOf<Ingredient>()
            val db = dbHelper.readableDatabase
            
            val query = """
                SELECT i.* FROM ${DatabaseHelper.TABLE_INGREDIENT} i
                INNER JOIN ${DatabaseHelper.TABLE_RECIPE_INGREDIENT} ri 
                ON i.${DatabaseHelper.COL_INGREDIENT_ID} = ri.${DatabaseHelper.COL_RECIPE_INGREDIENT_INGREDIENT_ID}
                WHERE ri.${DatabaseHelper.COL_RECIPE_INGREDIENT_RECIPE_ID} = ?
            """.trimIndent()
            
            val cursor = db.rawQuery(query, arrayOf(recipeId.toString()))
            
            cursor.use {
                while (it.moveToNext()) {
                    ingredients.add(Ingredient.fromCursor(it))
                }
            }
            
            db.close()
            Result.success(ingredients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun addIngredientToRecipe(recipeId: Int, ingredientId: Int): Result<Long> {
        return try {
            val db = dbHelper.writableDatabase
            val values = android.content.ContentValues().apply {
                put(DatabaseHelper.COL_RECIPE_INGREDIENT_RECIPE_ID, recipeId)
                put(DatabaseHelper.COL_RECIPE_INGREDIENT_INGREDIENT_ID, ingredientId)
            }
            val id = db.insert(DatabaseHelper.TABLE_RECIPE_INGREDIENT, null, values)
            db.close()
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun removeIngredientFromRecipe(recipeId: Int, ingredientId: Int): Result<Int> {
        return try {
            val db = dbHelper.writableDatabase
            val rowsAffected = db.delete(
                DatabaseHelper.TABLE_RECIPE_INGREDIENT,
                "${DatabaseHelper.COL_RECIPE_INGREDIENT_RECIPE_ID} = ? AND ${DatabaseHelper.COL_RECIPE_INGREDIENT_INGREDIENT_ID} = ?",
                arrayOf(recipeId.toString(), ingredientId.toString())
            )
            db.close()
            Result.success(rowsAffected)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
