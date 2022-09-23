package com.example.newsapp.dp.room

import androidx.room.TypeConverter
import com.example.newsapp.model.Source

class ArticleTypeConverter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}