package com.devdiaz.sensu.enums

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.devdiaz.sensu.R
import com.devdiaz.sensu.ui.theme.*

enum class MoodEmotion(
    val id: String,                 // Valor para guardar en DB (ej: "HAPPY")
    @StringRes val label: Int,      // Texto traducible (ej: R.string.emotion_happy)
    val icon: String,     // Icono
    val color: Color     // Color asociado (ej: R.color.pastel_yellow)
){
    Happy("HAPPY",  R.string.happy, "\uD83D\uDE04", EmotionAlegria),
    Sad("SAD", R.string.sad, "\uD83D\uDE22", EmotionTristeza),
    Fearful("FEARFUL", R.string.fearful, "\uD83D\uDE28", EmotionMiedo),
    Angry("ANGRY", R.string.angry, "\uD83D\uDE21", EmotionEnojo),
    Surprised("SURPRISED", R.string.surprised, "\uD83D\uDE31", EmotionSorpresa),
    Disgusted("DISGUSTED", R.string.disgusted, "\uD83E\uDD22", EmotionAsco)
}

enum class MoodRating(
    val value: Int,                // Valor numérico (1 al 5)
    @StringRes val label: Int,     // Texto traducible (ej: R.string.rating_terrible)
    val icon: String,               // Icono (ej: "😞")
    val color: Color               // Color asociado (ej: R.color.pastel_red)
){
    Terrible(1, R.string.terrible, "\uD83E\uDEE9",RatingTerrible),
    Bad(2, R.string.bad, "\uD83E\uDEE4",RatingMal),
    Okay(3, R.string.okay, "\uD83D\uDE10",RatingRegular),
    Good(4, R.string.good, "\uD83D\uDE04",RatingBien),
    Incredible(5, R.string.great, "\uD83D\uDE0E",RatingIncreible)
}