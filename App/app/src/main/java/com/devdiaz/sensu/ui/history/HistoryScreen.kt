package com.devdiaz.sensu.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.ui.theme.SensuBackgroundDark
import com.devdiaz.sensu.ui.theme.SensuBackgroundLight
import com.devdiaz.sensu.ui.theme.SensuGreen
import java.util.Calendar
import java.util.Locale

@Composable
fun HistoryScreen(modifier: Modifier, onBack: () -> Unit, viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = false // TODO: Get from system/settings
    val backgroundColor = if (isDarkTheme) SensuBackgroundDark else SensuBackgroundLight
    val contentColor = if (isDarkTheme) Color.White else Color(0xFF2C3E50)

    Scaffold(
            containerColor = backgroundColor,
            topBar = {
                HistoryHeader(
                        modifier = modifier,
                        onBack = onBack,
                        contentColor = contentColor,
                        isDarkTheme = isDarkTheme
                )
            }
    ) { paddingValues ->
        LazyColumn(
                modifier =
                        Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.historyItems) { item ->
                when (item) {
                    is HistoryItem.SectionHeader -> {
                        Text(
                                text = item.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDarkTheme) SensuGreen else Color(0xFF0db160),
                                modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    is HistoryItem.Entry -> {
                        HistoryCard(
                                entry = item.entry,
                                isDarkTheme = isDarkTheme,
                                contentColor = contentColor
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                // End of list indicator
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                            modifier =
                                    Modifier.size(width = 48.dp, height = 4.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(SensuGreen.copy(alpha = 0.2f))
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HistoryHeader(modifier: Modifier, onBack: () -> Unit, contentColor: Color, isDarkTheme: Boolean) {
    Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(48.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "regresar",
                    tint = Color(0xFF0E1B14)
                )
            }
        }

        Text(
                text = "Historial",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Spacer to balance the title
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun HistoryCard(entry: MoodEntry, isDarkTheme: Boolean, contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }

    val cardBg = if (isDarkTheme) Color(0xFF193326) else Color.White
    val borderColor =
            if (isDarkTheme) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.2f)

    // Date formatting for the card logic
    val cal = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val dayTitle =
            when {
                isSameDay(cal, today) -> "Hoy, " + formatDate(cal)
                isSameDay(cal, yesterday) -> "Ayer, " + formatDate(cal)
                else -> formatDateFull(cal)
            }

    Surface(
            shape = RoundedCornerShape(16.dp),
            color = cardBg,
            shadowElevation = 4.dp, // "Soft UI" shadow approximation
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            // Header Row
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                            text = dayTitle.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDarkTheme) SensuGreen else Color(0xFF2C3E50),
                            letterSpacing = 1.sp
                    )

                    Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Emoji Box
                        Box(
                                modifier =
                                        Modifier.size(48.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(entry.rating.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                        ) { Text(text = entry.rating.icon, fontSize = 24.sp) }

                        // Text Info
                        Column {
                            Text(
                                    text = stringResource(entry.rating.label),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF2C3E50),
                                    lineHeight = 16.sp
                            )
                            Text(
                                    text = stringResource(entry.emotion.label),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) SensuGreen else Color(0xFF555555)
                            )
                        }
                    }
                }

                // Expand/Collapse Button
                // Only show if there is a note
                if (!entry.note.isNullOrBlank()) {
                    Box(
                            modifier =
                                    Modifier.size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                    if (isDarkTheme) SensuGreen.copy(alpha = 0.1f)
                                                    else Color(0xFFE2E8F0)
                                            )
                                            .clickable { expanded = !expanded },
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                                imageVector =
                                        if (expanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand",
                                tint = if (isDarkTheme) SensuGreen else Color(0xFF2C3E50),
                                modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Note Section (Animated)
            AnimatedVisibility(
                    visible = expanded && !entry.note.isNullOrBlank(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Box(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                    if (isDarkTheme) Color(0xFF0d1b14)
                                                    else Color(0xFFF1F5F9)
                                            )
                                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                    text = "Nota:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = if (isDarkTheme) SensuGreen else Color(0xFF2C3E50),
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                    text = entry.note ?: "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color =
                                            if (isDarkTheme) Color(0xFFE2E8F0)
                                            else Color(0xFF555555),
                                    lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun formatDate(cal: Calendar): String {
    val fmt = java.text.SimpleDateFormat("MMM d", Locale.getDefault())
    return fmt.format(cal.time)
}

private fun formatDateFull(cal: Calendar): String {
    val fmt = java.text.SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
    return fmt.format(cal.time)
}
