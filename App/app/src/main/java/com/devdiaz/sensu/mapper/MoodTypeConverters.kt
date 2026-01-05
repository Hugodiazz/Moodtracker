package com.devdiaz.sensu.mapper

import androidx.room.TypeConverter
import com.devdiaz.sensu.enums.MoodEmotion
import com.devdiaz.sensu.enums.MoodRating

class MoodTypeConverters {

    // --- RATING (Guardamos el Int: 1, 2, 3...) ---
    @TypeConverter
    fun fromRating(rating: MoodRating): Int {
        return rating.value
    }

    @TypeConverter
    fun toRating(value: Int): MoodRating {
        // Busca por valor, si falla devuelve uno por defecto (ej: Okay)
        return MoodRating.entries.find { it.value == value } ?: MoodRating.Okay
    }

    // --- EMOTION (Guardamos el ID String: "HAPPY", "SAD"...) ---
    @TypeConverter
    fun fromEmotion(emotion: MoodEmotion): String {
        return emotion.id
    }

    @TypeConverter
    fun toEmotion(id: String): MoodEmotion {
        // Busca por ID, si falla devuelve uno por defecto
        return MoodEmotion.entries.find { it.id == id } ?: MoodEmotion.Happy
    }
}