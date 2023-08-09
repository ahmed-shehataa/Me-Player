package com.ashehata.me_player.database.room

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    fun fromString(intList: String?): List<Int> {
        return intList?.split(",")?.map { it.toIntOrNull() ?: 0 } ?: emptyList()
    }

    @TypeConverter
    fun toString(intList: List<Int>?): String {
        return intList?.joinToString(separator = ",") ?: ""
    }
}