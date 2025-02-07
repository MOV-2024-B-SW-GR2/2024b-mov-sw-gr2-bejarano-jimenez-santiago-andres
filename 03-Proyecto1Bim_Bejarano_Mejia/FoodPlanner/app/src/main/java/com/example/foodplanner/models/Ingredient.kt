package com.example.foodplanner.models

import android.content.ContentValues
import android.database.Cursor
import com.example.foodplanner.database.DatabaseHelper

data class Ingredient(
    var id: Int = 0,
    var name: String,
    var quantity: String
) {
    fun validate(): Boolean {
        return name.isNotBlank() && quantity.isNotBlank()
    }

    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put(DatabaseHelper.COL_INGREDIENT_NAME, name)
            put(DatabaseHelper.COL_INGREDIENT_QUANTITY, quantity)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): Ingredient {
            return Ingredient(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENT_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENT_NAME)),
                quantity = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENT_QUANTITY))
            )
        }
    }
}
