package com.ashehata.me_player.database.room

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    fun fromString(intList: String?): List<Int> {
        return if (intList.isNullOrEmpty()) emptyList() else intList.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(intList: List<Int>?): String {
        return if (intList.isNullOrEmpty()) "" else intList.joinToString(separator = ",")
    }
}