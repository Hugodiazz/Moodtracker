package com.devdiaz.sensu.ui.streak

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devdiaz.sensu.R
import com.devdiaz.sensu.ui.theme.SensuBackgroundDark
import com.devdiaz.sensu.ui.theme.SensuBackgroundLight
import com.devdiaz.sensu.ui.theme.SensuGreen

@Composable
fun StreakCelebrationScreen(
        streakDays: Int,
        onContinue: () -> Unit, // Actions for "Genial" and "Omitir" -> Go to Dashboard
        onDismiss: () -> Unit
) {
    // Determine Theme (For now assume Light or follow system if implemented, but hardcoded similar
    // to Dashboard for now)
    val isDarkTheme = false
    val backgroundColor = if (isDarkTheme) SensuBackgroundDark else SensuBackgroundLight
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0d1b14)
    val textSecondary = if (isDarkTheme) SensuGreen else Color(0xFF4c9a73)
    val cardColor = if (isDarkTheme) Color(0xFF1a2c24) else Color.White

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor).padding(24.dp)) {
        // Decorative Background Blobs
        Box(
                modifier =
                        Modifier.align(Alignment.TopStart)
                                .offset(x = (-40).dp, y = (-40).dp)
                                .size(250.dp)
                                .background(SensuGreen.copy(alpha = 0.1f), CircleShape)
                                .blur(64.dp)
        )
        Box(
                modifier =
                        Modifier.align(Alignment.BottomEnd)
                                .offset(x = 40.dp, y = 20.dp)
                                .size(300.dp)
                                .background(Color(0xFF90CAF9).copy(alpha = 0.1f), CircleShape)
                                .blur(64.dp)
        )

        // Floating Icons
//        Icon(
//                imageVector = Icons.Au,
//                contentDescription = null,
//                tint = SensuGreen.copy(alpha = 0.3f),
//                modifier =
//                        Modifier.align(Alignment.TopStart)
//                                .padding(top = 100.dp, start = 40.dp)
//                                .size(40.dp)
//                                .graphicsLayer(rotationZ = 12f)
//        )

        Column(
                modifier =
                        Modifier.align(Alignment.Center).fillMaxWidth().padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text(
                    text = "¡Racha activa,\nsigue con todo!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    letterSpacing = (-1).sp
            )

            // Central Card
            Card(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Image Container
                    Box(
                            modifier = Modifier.size(120.dp).padding(bottom = 24.dp),
                            contentAlignment = Alignment.Center
                    ) {
                        // Glow
                        Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .background(
                                                        SensuGreen.copy(alpha = 0.2f),
                                                        CircleShape
                                                )
                                                .blur(32.dp)
                        )
                        // Image
                        Image(
                                painter = painterResource(id = R.drawable.streak_on),
                                contentDescription = "Streak Character",
                                modifier = Modifier.size(72.dp),
                        )
                    }

                    // Streak Data
                    Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                                text = "¡EXCELENTE TRABAJO!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = textSecondary,
                                letterSpacing = 2.sp
                        )
                        Text(
                                text = "$streakDays Días",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                letterSpacing = (-1).sp
                        )
                    }

                    // Sub-badge
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(50), color = SensuGreen.copy(alpha = 0.1f)) {
                        Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                    text = "¡Estás en racha!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF36d082)
                            )
                        }
                    }
                }
            }

            // Body Text
            Text(
                    text =
                            "¡Objetivo cumplido por hoy! Sigue sumando días a tu racha y no dejes de registrar.",
                    fontSize = 16.sp,
                    color = textColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    lineHeight = 24.sp
            )

            // Button
            Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(50)),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = SensuGreen,
                                    contentColor = Color(0xFF0d1b14)
                            )
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                            text = "¡Genial! Continuar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
