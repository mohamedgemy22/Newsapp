package com.example.specialnotes.ui.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.specialnotes.ui.model.Source


class Converters {

    @TypeConverter
    fun fromSource(source: com.example.specialnotes.ui.model.Source): String{
        return source.name
    }
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}