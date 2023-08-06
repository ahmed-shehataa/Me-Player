package com.ashehata.me_player.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeDataModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}


data class RecipeDataModel(
    val name: String
)

interface RecipesDao