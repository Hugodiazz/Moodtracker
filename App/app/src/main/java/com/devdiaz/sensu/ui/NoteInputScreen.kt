package com.devdiaz.sensu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteInputScreen(
        note: String,
        onNoteChanged: (String) -> Unit,
        onSave: () -> Unit,
        onSkip: () -> Unit,
        onBack: () -> Unit
) {
        Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAF9)) // background-light
        ) {
                // Blobs
                Box(
                        modifier =
                                Modifier.offset(x = 100.dp, y = (-50).dp)
                                        .size(300.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFF13ec80).copy(alpha = 0.1f),
                                                CircleShape
                                        ) // primary
                )
                Box(
                        modifier =
                                Modifier.align(Alignment.BottomStart)
                                        .offset(x = (-50).dp, y = (-150).dp)
                                        .size(200.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFFDBEAFE).copy(alpha = 0.4f),
                                                CircleShape
                                        ) // blue-100
                )

                Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        // Header
                        Row(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(top = 48.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                // Back Button
                                Surface(
                                        shape = CircleShape,
                                        shadowElevation = 2.dp,
                                        color = Color.White,
                                        modifier =
                                                Modifier.size(40.dp).clip(CircleShape).clickable {
                                                        onBack()
                                                }
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                        imageVector = Icons.Rounded.ArrowBack,
                                                        contentDescription = "Back",
                                                        tint = Color(0xFF94A3B8), // slate-400
                                                        modifier = Modifier.size(20.dp)
                                                )
                                        }
                                }

                                Spacer(modifier = Modifier.size(40.dp)) // Balance layout
                        }

                        // Title
                        Column(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(top = 16.dp, bottom = 32.dp),
                                horizontalAlignment = Alignment.Start
                        ) {
                                Text(
                                        text = "¿Qué sucedió\nhoy?",
                                        style =
                                                MaterialTheme.typography.displaySmall.copy(
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF1E293B), // slate-800
                                                        lineHeight = 40.sp,
                                                        fontSize = 36.sp
                                                )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                        text = "Puedes agregar una nota sobre tu estado de ánimo.",
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        color = Color(0xFF94A3B8), // slate-400
                                                        fontWeight = FontWeight.Medium,
                                                ),
                                        modifier =
                                                Modifier.run {
                                                        // Italic logic via font style if needed,
                                                        // default okay for now
                                                        this
                                                }
                                )
                        }

                        // Soft Text Area
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(250.dp) // min-h-[200px] + extra
                                                .shadow(
                                                        elevation = 0.dp, // Using custom logic via
                                                        // border/color to
                                                        // simulate soft UI?
                                                        // The prompt asks for "soft-textarea" CSS
                                                        // simulation.
                                                        // Compose doesn't have inset shadow easily.
                                                        // We will approximate with a background and
                                                        // border.
                                                        shape = RoundedCornerShape(24.dp)
                                                )
                                                .background(
                                                        Color(0xFFF8FAF9),
                                                        RoundedCornerShape(24.dp)
                                                )
                                                .border(
                                                        1.dp,
                                                        Color.White.copy(alpha = 0.5f),
                                                        RoundedCornerShape(24.dp)
                                                )
                        ) {
                                TextField(
                                        value = note,
                                        onValueChange = {
                                                if (it.length <= 2000) onNoteChanged(it)
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                        placeholder = {
                                                Text(
                                                        "Escribe aquí tus pensamientos...",
                                                        color = Color(0xFFCBD5E1) // slate-300
                                                )
                                        },
                                        colors =
                                                TextFieldDefaults.colors(
                                                        focusedContainerColor = Color.Transparent,
                                                        unfocusedContainerColor = Color.Transparent,
                                                        focusedIndicatorColor = Color.Transparent,
                                                        unfocusedIndicatorColor = Color.Transparent
                                                ),
                                        textStyle =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        fontSize = 18.sp,
                                                        color = Color(0xFF1E293B)
                                                )
                                )
                        }

                        Text(
                                text = "${note.length} / 2000",
                                style =
                                        MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF94A3B8),
                                                fontWeight = FontWeight.Medium
                                        ),
                                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Footer
                        Column(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                Button(
                                        onClick = onSave,
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(64.dp) // py-5 approx
                                                        .shadow(
                                                                10.dp,
                                                                RoundedCornerShape(16.dp),
                                                                spotColor =
                                                                        Color(0xFF13ec80)
                                                                                .copy(alpha = 0.2f)
                                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor =
                                                                Color(0xFF13ec80) // primary
                                                )
                                ) {
                                        Text(
                                                text = "Guardar",
                                                fontSize = 20.sp, // text-xl
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                }
                        }
                }
        }
}
