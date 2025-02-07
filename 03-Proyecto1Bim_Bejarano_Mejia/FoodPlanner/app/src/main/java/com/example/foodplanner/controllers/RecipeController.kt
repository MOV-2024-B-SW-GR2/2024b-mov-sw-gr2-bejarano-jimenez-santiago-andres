package com.example.foodplanner.controllers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.foodplanner.database.DatabaseHelper
import com.example.foodplanner.models.Recipe

class RecipeController(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertRecipe(recipe: Recipe): Result<Long> {
        return try {
            if (!recipe.validate()) {
                Result.failure(IllegalArgumentException("Datos de receta inválidos"))
            } else {
                val db = dbHelper.writableDatabase
                val id = db.insert(DatabaseHelper.TABLE_RECIPE, null, recipe.toContentValues())
                db.close()
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllRecipes(): Result<List<Recipe>> {
        return try {
            val recipes = mutableListOf<Recipe>()
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                DatabaseHelper.TABLE_RECIPE,
                null,
                null,
                null,
                null,
                null,
                "${DatabaseHelper.COL_RECIPE_NAME} ASC"
            )

            cursor.use {
                while (it.moveToNext()) {
                    recipes.add(Recipe.fromCursor(it))
                }
            }
            db.close()
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getRecipeById(id: Int): Result<Recipe?> {
        return try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                DatabaseHelper.TABLE_RECIPE,
                null,
                "${DatabaseHelper.COL_RECIPE_ID} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

            val recipe = if (cursor.moveToFirst()) {
                Recipe.fromCursor(cursor)
            } else {
                null
            }
            cursor.close()
            db.close()
            Result.success(recipe)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateRecipe(recipe: Recipe): Result<Int> {
        return try {
            if (!recipe.validate()) {
                Result.failure(IllegalArgumentException("Datos de receta inválidos"))
            } else {
                val db = dbHelper.writableDatabase
                val rowsAffected = db.update(
                    DatabaseHelper.TABLE_RECIPE,
                    recipe.toContentValues(),
                    "${DatabaseHelper.COL_RECIPE_ID} = ?",
                    arrayOf(recipe.id.toString())
                )
                db.close()
                Result.success(rowsAffected)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun deleteRecipe(recipeId: Int): Result<Int> {
        return try {
            val db = dbHelper.writableDatabase
            val rowsAffected = db.delete(
                DatabaseHelper.TABLE_RECIPE,
                "${DatabaseHelper.COL_RECIPE_ID} = ?",
                arrayOf(recipeId.toString())
            )
            db.close()
            Result.success(rowsAffected)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
