package com.example.foodplanner.models

import android.content.ContentValues
import android.database.Cursor
import com.example.foodplanner.database.DatabaseHelper

data class Recipe(
    var id: Int = 0,
    var name: String,
    var category: String,
    var imageUri: String? = null,
    var steps: String
) {
    fun validate(): Boolean {
        return name.isNotBlank() && steps.isNotBlank() && isValidCategory(category)
    }

    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put(DatabaseHelper.COL_RECIPE_NAME, name)
            put(DatabaseHelper.COL_RECIPE_CATEGORY, category)
            put(DatabaseHelper.COL_RECIPE_IMAGE_URI, imageUri)
            put(DatabaseHelper.COL_RECIPE_STEPS, steps)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): Recipe {
            return Recipe(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME)),
                category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_CATEGORY)),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_IMAGE_URI)),
                steps = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_STEPS))
            )
        }

        private fun isValidCategory(category: String): Boolean {
            return category in listOf("Desayuno", "Almuerzo", "Merienda")
        }
    }
}
