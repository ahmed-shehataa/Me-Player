package com.ashehata.me_player.database.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeDataModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}


@Entity
data class RecipeDataModel(
    @PrimaryKey
    val id: Int,
    val name: String,
)

@Dao
interface RecipesDao