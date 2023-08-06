package com.ashehata.me_player.database.room

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    fun fromString(stringListString: String?): List<String> {
        return stringListString?.split(",")?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun toString(stringList: List<String>?): String {
        return stringList?.joinToString(separator = ",") ?: ""
    }
}