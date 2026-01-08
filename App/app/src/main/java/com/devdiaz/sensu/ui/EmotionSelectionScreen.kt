package com.devdiaz.sensu.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devdiaz.sensu.enums.MoodEmotion

@Composable
fun EmotionSelectionScreen(
        onEmotionSelected: (MoodEmotion) -> Unit,
        onBack: () -> Unit,
        onSave: () -> Unit,
        selectedEmotion: MoodEmotion?
) {
        Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC)) // background-light
        ) {
                // Blobs (Pastel Yellow, Blue, Pink)
                Box(
                        modifier =
                                Modifier.offset(x = 100.dp, y = (-50).dp)
                                        .size(300.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFFFEF9C3).copy(alpha = 0.4f),
                                                CircleShape
                                        ) // pastel-yellow
                )
                Box(
                        modifier =
                                Modifier.offset(x = (-50).dp, y = 100.dp)
                                        .size(250.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFFDBEAFE).copy(alpha = 0.4f),
                                                CircleShape
                                        ) // pastel-blue
                )
                Box(
                        modifier =
                                Modifier.align(Alignment.BottomEnd)
                                        .offset(x = 50.dp, y = 50.dp)
                                        .size(200.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFFFEE2E2).copy(alpha = 0.4f),
                                                CircleShape
                                        ) // pastel-pink/red
                )

                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                        // Header
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 40.dp)
                        ) {
                                Surface(
                                        shape = CircleShape,
                                        shadowElevation = 2.dp,
                                        color = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(56.dp)
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                        text = "👀",
                                                        fontSize = 28.sp
                                                ) // Fallback icon instead of material symbol for
                                                // now
                                        }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                        text = "¿Qué emoción\nsientes?",
                                        style =
                                                MaterialTheme.typography.displaySmall.copy(
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF1E293B), // slate-800
                                                        lineHeight = 40.sp
                                                ),
                                        textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                        text = "IDENTIFICA TU ESTADO ACTUAL",
                                        style =
                                                MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF94A3B8), // slate-400
                                                        letterSpacing = 1.sp
                                                )
                                )
                        }

                        // Grid (2 cols)
                        Column(
                                modifier = Modifier.widthIn(max = 340.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                val emotions = MoodEmotion.entries
                                // Sencillo chunk(2) para el grid 2x3
                                emotions.chunked(2).forEach { rowEmotions ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                                rowEmotions.forEach { emotion ->
                                                        EmotionButton(
                                                                emotion = emotion,
                                                                isSelected =
                                                                        emotion == selectedEmotion,
                                                                modifier = Modifier.weight(1f),
                                                                onClick = {
                                                                        onEmotionSelected(emotion)
                                                                }
                                                        )
                                                }
                                        }
                                }
                        }

                        // Footer
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 24.dp)
                        ) {
                                TextButton(onClick = onBack) {
                                        Text(
                                                text = "Regresar",
                                                color = Color(0xFF64748B),
                                                fontWeight = FontWeight.SemiBold
                                        )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                AnimatedVisibility(
                                        visible = selectedEmotion != null,
                                        enter = slideInVertically { it } + fadeIn()
                                ) {
                                        Button(
                                                onClick = onSave,
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor =
                                                                        Color(
                                                                                0xFF13EC80
                                                                        ) // Primary Green
                                                        ),
                                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                                shape = RoundedCornerShape(16.dp)
                                        ) {
                                                Text(
                                                        text = "Guardar",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                        }
                                }
                                // Spacer to keep layout stable if button is hidden, or use spacing
                                if (selectedEmotion == null) {
                                        Spacer(modifier = Modifier.height(56.dp))
                                }
                        }
                }
        }
}

@Composable
fun EmotionButton(
        emotion: MoodEmotion,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
) {
        val backgroundColor = emotion.color
        // Si está seleccionado, borde o efecto extra
        val borderWidth = if (isSelected) 2.dp else 0.dp
        val borderColor = if (isSelected) Color(0xFF1E293B) else Color.Transparent
        val scale = if (isSelected) 1.05f else 1f

        Column(
                modifier =
                        modifier.aspectRatio(1f)
                                .shadow(
                                        elevation = 6.dp,
                                        shape = RoundedCornerShape(32.dp),
                                        spotColor = Color.Black.copy(alpha = 0.05f)
                                )
                                .clip(RoundedCornerShape(32.dp)) // Squircle-ish
                                .background(backgroundColor)
                                .border(borderWidth, borderColor, RoundedCornerShape(32.dp))
                                .clickable { onClick() }
                                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
                // En el diseño HTML, el icono rota al hover, aquí solo mostramos estático
                Text(text = emotion.icon, fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = stringResource(id = emotion.label),
                        style =
                                MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color =
                                                Color.Black.copy(
                                                        alpha = 0.7f
                                                ) // Adaptado para legibilidad
                                )
                )
        }
}
