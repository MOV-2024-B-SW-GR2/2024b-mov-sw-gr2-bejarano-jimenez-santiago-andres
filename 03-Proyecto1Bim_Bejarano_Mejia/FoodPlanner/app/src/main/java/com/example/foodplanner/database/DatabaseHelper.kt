package com.example.foodplanner.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de recetas
        db.execSQL("""
            CREATE TABLE $TABLE_RECIPE (
                $COL_RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_RECIPE_NAME TEXT NOT NULL,
                $COL_RECIPE_CATEGORY TEXT NOT NULL,
                $COL_RECIPE_IMAGE_URI TEXT,
                $COL_RECIPE_STEPS TEXT NOT NULL
            )
        """.trimIndent())

        // Crear tabla de ingredientes
        db.execSQL("""
            CREATE TABLE $TABLE_INGREDIENT (
                $COL_INGREDIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_INGREDIENT_NAME TEXT NOT NULL,
                $COL_INGREDIENT_QUANTITY TEXT NOT NULL
            )
        """.trimIndent())

        // Crear tabla de relación entre recetas e ingredientes
        db.execSQL("""
            CREATE TABLE $TABLE_RECIPE_INGREDIENT (
                $COL_RECIPE_INGREDIENT_RECIPE_ID INTEGER,
                $COL_RECIPE_INGREDIENT_INGREDIENT_ID INTEGER,
                PRIMARY KEY ($COL_RECIPE_INGREDIENT_RECIPE_ID, $COL_RECIPE_INGREDIENT_INGREDIENT_ID),
                FOREIGN KEY ($COL_RECIPE_INGREDIENT_RECIPE_ID) REFERENCES $TABLE_RECIPE($COL_RECIPE_ID) ON DELETE CASCADE,
                FOREIGN KEY ($COL_RECIPE_INGREDIENT_INGREDIENT_ID) REFERENCES $TABLE_INGREDIENT($COL_INGREDIENT_ID) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPE_INGREDIENT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INGREDIENT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPE")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "foodplanner.db"
        const val DATABASE_VERSION = 1

        // Tabla de recetas
        const val TABLE_RECIPE = "recipes"
        const val COL_RECIPE_ID = "recipe_id"
        const val COL_RECIPE_NAME = "name"
        const val COL_RECIPE_CATEGORY = "category"
        const val COL_RECIPE_IMAGE_URI = "image_uri"
        const val COL_RECIPE_STEPS = "steps"

        // Tabla de ingredientes
        const val TABLE_INGREDIENT = "ingredients"
        const val COL_INGREDIENT_ID = "ingredient_id"
        const val COL_INGREDIENT_NAME = "name"
        const val COL_INGREDIENT_QUANTITY = "quantity"

        // Tabla de relación entre recetas e ingredientes
        const val TABLE_RECIPE_INGREDIENT = "recipe_ingredients"
        const val COL_RECIPE_INGREDIENT_RECIPE_ID = "recipe_id"
        const val COL_RECIPE_INGREDIENT_INGREDIENT_ID = "ingredient_id"
    }
}
