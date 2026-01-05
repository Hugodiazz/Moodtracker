package com.devdiaz.sensu.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val intensity: Int,        // Valor del termómetro (1 al 5)
    val emotionIcon: String,   // Identificador del icono (ej: "ic_happy", "😊")
    val emotionLabel: String,  // Texto (ej: "Contento", "Ansioso")
    val note: String? = null,  // Nota opcional

    // Guardamos fecha y hora en milisegundos para facilitar ordenamiento
    val timestamp: Long = System.currentTimeMillis(),

    // Campo auxiliar para facilitar consultas de "Racha" (ej: "2023-10-27")
    // Esto evita tener que convertir timestamps complejos en cada consulta SQL
    val dateString: String
)