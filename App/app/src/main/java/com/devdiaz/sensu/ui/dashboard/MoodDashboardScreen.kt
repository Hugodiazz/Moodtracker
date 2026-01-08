package com.devdiaz.sensu.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devdiaz.sensu.R
import com.devdiaz.sensu.enums.MoodEmotion
import com.devdiaz.sensu.enums.MoodRating
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.ui.theme.*
import java.util.Calendar

@Composable
fun MoodDashboardScreen(
        onNavigateToScan: () -> Unit,
        viewModel: MoodDashboardViewModel = hiltViewModel()
) {
        val uiState by viewModel.uiState.collectAsState()
        val isDarkTheme = false // TODO: Get from system/settings

        val backgroundColor = if (isDarkTheme) SensuBackgroundDark else SensuBackgroundLight
        val contentColor = if (isDarkTheme) Color.White else Color(0xFF1e293b) // Slate 800

        Scaffold(
                containerColor = backgroundColor,
                floatingActionButton = { RegistrarButton(onNavigateToScan) }
        ) { _ ->
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 24.dp)
                ) {
                        // Header
                        DashboardHeader(
                                streak = uiState.streak,
                                isStreakActive = uiState.isStreakActive,
                                contentColor = contentColor,
                                isDarkTheme = isDarkTheme
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Time Filter
                        TimeFilterSelector(
                                selectedFilter = uiState.selectedTimeFilter,
                                onFilterSelected = viewModel::onTimeFilterSelected,
                                isDarkTheme = isDarkTheme
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Emotional Calendar
                        // Calendar
                        EmotionalCalendarCard(
                                selectedTimeFilter = uiState.selectedTimeFilter,
                                isDarkTheme = isDarkTheme,
                                recentMoods = uiState.recentMoods
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        // Trend Graph
                        TrendGraphCard(
                                selectedTimeFilter = uiState.selectedTimeFilter,
                                isDarkTheme = isDarkTheme,
                                recentMoods = uiState.recentMoods
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Emotion Distribution
                        EmotionDistributionCard(
                                distribution = uiState.emotionDistribution,
                                isDarkTheme = isDarkTheme,
                                contentColor = contentColor
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Stats Grid
                        StatsGrid(
                                streak = uiState.streak,
                                isStreakActive = uiState.isStreakActive,
                                isDarkTheme = isDarkTheme,
                                contentColor = contentColor
                        )

                        Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for FAB
                }
        }
}

@Composable
fun DashboardHeader(
        streak: Int,
        isStreakActive: Boolean,
        contentColor: Color,
        isDarkTheme: Boolean
) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                                text = "Sensu",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = contentColor,
                                letterSpacing = (-0.5).sp
                        )
                }

                // Streak Badge
                Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isDarkTheme) SensuCardDark else Color.White,
                        shadowElevation = 4.dp, // soft shadow approximation
                        border =
                                androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isDarkTheme) Color.White.copy(alpha = 0.05f)
                                        else Color(0xFFF1F5F9)
                                )
                ) {
                        Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                                Icon(
                                        painter =
                                                painterResource(
                                                        id =
                                                                if (isStreakActive)
                                                                        R.drawable.streak_on
                                                                else R.drawable.streak_off
                                                ),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(20.dp)
                                )
                                Text(
                                        text = streak.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color =
                                                if (isDarkTheme) Color(0xFFE2E8F0)
                                                else Color(0xFF334155)
                                )
                        }
                }
        }
}

@Composable
fun TimeFilterSelector(
        selectedFilter: TimeFilter,
        onFilterSelected: (TimeFilter) -> Unit,
        isDarkTheme: Boolean
) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        color = if (isDarkTheme) SensuCardDark else Color.White,
                        shadowElevation = 2.dp
                ) {
                        Row(modifier = Modifier.padding(6.dp)) {
                                TimeFilter.values().forEach { filter ->
                                        val isSelected = selectedFilter == filter
                                        val textColor =
                                                if (isSelected) Color(0xFF0F172A)
                                                else if (isDarkTheme) Color.Gray
                                                else Color(0xFF64748B) // Slate 500

                                        val backgroundColor =
                                                if (isSelected) SensuGreen else Color.Transparent

                                        Box(
                                                modifier =
                                                        Modifier.weight(1f)
                                                                .clip(RoundedCornerShape(50))
                                                                .background(backgroundColor)
                                                                .clickable {
                                                                        onFilterSelected(filter)
                                                                }
                                                                .padding(vertical = 10.dp),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Text(
                                                        text = filter.label,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = textColor
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun EmotionalCalendarCard(
        selectedTimeFilter: TimeFilter,
        isDarkTheme: Boolean,
        recentMoods: List<MoodEntry>
) {

        val cardBg = if (isDarkTheme) SensuCardDark else SensuCardLight

        Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(
                                                if (isDarkTheme) SensuCardDark else SensuCardLight
                                        )
                                        .padding(24.dp)
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Column {
                                        Text(
                                                text = "Calendario Emocional",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color =
                                                        if (isDarkTheme) Color.White
                                                        else Color(0xFF1E293B)
                                        )
                                        val calendar = Calendar.getInstance()
                                        val dateFormat =
                                                java.text.SimpleDateFormat(
                                                        "dd MMM",
                                                        java.util.Locale.getDefault()
                                                )
                                        val monthFormat =
                                                java.text.SimpleDateFormat(
                                                        "MMMM yyyy",
                                                        java.util.Locale.getDefault()
                                                )

                                        val dateText =
                                                if (selectedTimeFilter == TimeFilter.MONTH) {
                                                        monthFormat
                                                                .format(calendar.time)
                                                                .uppercase()
                                                } else {
                                                        val endDate =
                                                                dateFormat.format(calendar.time)
                                                        calendar.add(Calendar.DAY_OF_YEAR, -6)
                                                        val startDate =
                                                                dateFormat.format(calendar.time)
                                                        "$startDate - $endDate".uppercase()
                                                }

                                        Text(
                                                text = dateText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF94A3B8), // Slate 400
                                                letterSpacing = 1.sp
                                        )
                                }
                                Icon(
                                        imageVector = Icons.Filled.KeyboardArrowRight,
                                        contentDescription = "Ver más",
                                        tint = Color(0xFF94A3B8)
                                )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        AnimatedContent(
                                targetState = selectedTimeFilter,
                                transitionSpec = {
                                        if (targetState.ordinal > initialState.ordinal) {
                                                slideInHorizontally { it } + fadeIn() togetherWith
                                                        slideOutHorizontally { -it } + fadeOut()
                                        } else {
                                                slideInHorizontally { -it } + fadeIn() togetherWith
                                                        slideOutHorizontally { it } + fadeOut()
                                        }
                                },
                                label = "CalendarView"
                        ) { filter ->
                                if (filter == TimeFilter.WEEK) {
                                        WeekCalendarView(isDarkTheme, recentMoods)
                                } else {
                                        MonthCalendarView(isDarkTheme, recentMoods)
                                }
                        }
                }
        }
}

@Composable
fun WeekCalendarView(isDarkTheme: Boolean, recentMoods: List<MoodEntry>) {
        val days = listOf("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa")

        // Map moods to the last 7 days (Today is index 6)
        // Using "Year-DayOfYear" key to be safe across year boundaries
        val moodMap =
                remember(recentMoods) {
                        recentMoods.associateBy {
                                val c = Calendar.getInstance()
                                c.timeInMillis = it.timestamp
                                "${c.get(Calendar.YEAR)}-${c.get(Calendar.DAY_OF_YEAR)}"
                        }
                }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // We want to show last 7 days ending today.
                // Or specific week days? "L, M, M..." implies fixed Mon-Sun?
                // But "Week View" usually means "This Week" or "Last 7 Days".
                // Let's assume Last 7 Days for now ending today.

                for (i in 6 downTo 0) {
                        val c = Calendar.getInstance()
                        c.add(Calendar.DAY_OF_YEAR, -i)
                        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon...
                        // Map 1..7 to index in 'days' list (0..6) where 0=Do, 1=Lu...
                        val dayLabel = days[dayOfWeek - 1]

                        val year = c.get(Calendar.YEAR)
                        val dayOfYear = c.get(Calendar.DAY_OF_YEAR)
                        val key = "$year-$dayOfYear"
                        val mood = moodMap[key]
                        val isToday = i == 0

                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                // Pillar
                                Box(
                                        modifier =
                                                Modifier.width(40.dp)
                                                        .height(96.dp)
                                                        .clip(RoundedCornerShape(50))
                                                        .background(
                                                                if (isDarkTheme)
                                                                        Color.White.copy(
                                                                                alpha = 0.05f
                                                                        )
                                                                else SensuSecondary
                                                        ),
                                        contentAlignment = Alignment.BottomCenter
                                ) {
                                        if (isToday && mood == null) {
                                                // Add button style for today
                                                Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                ) {
                                                        Icon(
                                                                imageVector = Icons.Filled.Add,
                                                                contentDescription = "Add",
                                                                tint = Color(0xFFCBD5E1)
                                                        )
                                                }
                                        } else if (mood != null) {
                                                val rating = mood.rating
                                                // height based on value 1..5 -> 0.2f..1.0f
                                                val fillHeight =
                                                        (rating.value / 5f).coerceIn(0.2f, 1f)

                                                Box(
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .fillMaxHeight(fillHeight)
                                                                        .clip(
                                                                                RoundedCornerShape(
                                                                                        bottomStart =
                                                                                                50.dp,
                                                                                        bottomEnd =
                                                                                                50.dp
                                                                                )
                                                                        )
                                                                        .background(
                                                                                rating.color.copy(
                                                                                        alpha = 0.8f
                                                                                )
                                                                        )
                                                )
                                                // Face Icon
                                                Text(
                                                        text = rating.icon,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(bottom = 8.dp)
                                                )
                                        }
                                }

                                Text(
                                        text = dayLabel,
                                        fontSize = 12.sp,
                                        fontWeight =
                                                if (isToday) FontWeight.Bold
                                                else FontWeight.SemiBold,
                                        color = if (isToday) SensuGreen else Color(0xFF94A3B8)
                                )
                        }
                }
        }
}

@Composable
fun MonthCalendarView(isDarkTheme: Boolean, recentMoods: List<MoodEntry>) {
        // Days Header
        val weekDays = listOf("L", "M", "M", "J", "V", "S", "D")

        // Map moods by day of month
        // Map moods by day of month, filtering only for the current month
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val moodMap =
                remember(recentMoods) {
                        recentMoods
                                .filter {
                                        val c = Calendar.getInstance()
                                        c.timeInMillis = it.timestamp
                                        c.get(Calendar.MONTH) == currentMonth &&
                                                c.get(Calendar.YEAR) == currentYear
                                }
                                .associateBy {
                                        val c = Calendar.getInstance()
                                        c.timeInMillis = it.timestamp
                                        c.get(Calendar.DAY_OF_MONTH)
                                }
                }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Weekday Header
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        weekDays.forEach { day ->
                                Text(
                                        text = day,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF94A3B8), // slate-400
                                        modifier = Modifier.width(36.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                        }
                }

                // Calendar Calculation
                val c = Calendar.getInstance()
                c.set(Calendar.DAY_OF_MONTH, 1)

                // Determine day of week for the 1st (1=Sunday, 2=Monday, ..., 7=Saturday)
                val firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK)

                // Adjust for Monday start (Monday=0, Sunday=6)
                // If firstDayOfWeek is Mon(2) -> 0. Tue(3) -> 1. ... Sun(1) -> 6.
                val startOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

                val daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)

                // Previous Month Calculation
                val prevMonthCal = Calendar.getInstance()
                prevMonthCal.add(Calendar.MONTH, -1)
                val daysInPrevMonth = prevMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)

                // Grid Logic
                // If startOffset + daysInMonth > 35, we need 6 rows. Else 5 is enough (usually).
                val cellsNeeded = startOffset + daysInMonth
                val rows = if (cellsNeeded > 35) 6 else 5

                var dayCounter = 1
                var nextMonthCounter = 1

                for (row in 0 until rows) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                for (col in 0 until 7) {
                                        val index = row * 7 + col

                                        if (index < startOffset) {
                                                // Previous Month Days
                                                val prevDay =
                                                        daysInPrevMonth - (startOffset - index - 1)
                                                Text(
                                                        text = prevDay.toString(),
                                                        fontSize = 12.sp,
                                                        color =
                                                                Color(0xFF94A3B8)
                                                                        .copy(alpha = 0.5f),
                                                        modifier =
                                                                Modifier.size(36.dp)
                                                                        .wrapContentHeight(
                                                                                Alignment
                                                                                        .CenterVertically
                                                                        ),
                                                        textAlign =
                                                                androidx.compose.ui.text.style
                                                                        .TextAlign.Center
                                                )
                                        } else if (dayCounter <= daysInMonth) {
                                                // Current Month Days
                                                val dayNum = dayCounter
                                                val mood = moodMap[dayNum]
                                                val isToday =
                                                        dayNum ==
                                                                Calendar.getInstance()
                                                                        .get(
                                                                                Calendar.DAY_OF_MONTH
                                                                        ) &&
                                                                currentMonth ==
                                                                        Calendar.getInstance()
                                                                                .get(
                                                                                        Calendar.MONTH
                                                                                ) &&
                                                                currentYear ==
                                                                        Calendar.getInstance()
                                                                                .get(Calendar.YEAR)

                                                val todayBorderModifier =
                                                        if (isToday) {
                                                                Modifier.border(
                                                                        1.dp,
                                                                        if (isDarkTheme)
                                                                                Color.White.copy(
                                                                                        0.1f
                                                                                )
                                                                        else Color(0xFFF1F5F9),
                                                                        CircleShape
                                                                )
                                                        } else {
                                                                Modifier
                                                        }

                                                val bgColor =
                                                        mood?.rating?.color
                                                                ?: if (isToday) Color.Transparent
                                                                else Color.Transparent

                                                Box(
                                                        modifier =
                                                                Modifier.size(36.dp)
                                                                        .clip(CircleShape)
                                                                        .background(bgColor)
                                                                        .then(todayBorderModifier),
                                                        contentAlignment = Alignment.Center
                                                ) {
                                                        Text(
                                                                text = dayNum.toString(),
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color =
                                                                        if (isToday) SensuGreen
                                                                        else
                                                                                Color(0xFF334155)
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.7f
                                                                                        )
                                                        )
                                                }
                                                dayCounter++
                                        } else {
                                                // Next Month Days
                                                Text(
                                                        text = nextMonthCounter.toString(),
                                                        fontSize = 12.sp,
                                                        color =
                                                                Color(0xFF94A3B8)
                                                                        .copy(alpha = 0.5f),
                                                        modifier =
                                                                Modifier.size(36.dp)
                                                                        .wrapContentHeight(
                                                                                Alignment
                                                                                        .CenterVertically
                                                                        ),
                                                        textAlign =
                                                                androidx.compose.ui.text.style
                                                                        .TextAlign.Center
                                                )
                                                nextMonthCounter++
                                        }
                                }
                        }
                }
        }
}

@Composable
fun TrendGraphCard(
        selectedTimeFilter: TimeFilter,
        isDarkTheme: Boolean,
        recentMoods: List<MoodEntry>
) {
        val cardBg = if (isDarkTheme) SensuCardDark else SensuCardLight
        val contentColor = if (isDarkTheme) Color.White else Color(0xFF1E293B)

        // Dynamic Dates
        val calendar = Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
        val monthFormat = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault())

        val label =
                if (selectedTimeFilter == TimeFilter.MONTH) {
                        monthFormat.format(calendar.time).uppercase()
                } else {
                        val endDate = dateFormat.format(calendar.time)
                        calendar.add(Calendar.DAY_OF_YEAR, -6)
                        val startDate = dateFormat.format(calendar.time)
                        "$startDate - $endDate".uppercase()
                }

        // Calculate Average
        val avgVal =
                if (recentMoods.isNotEmpty()) {
                        recentMoods.map { it.rating.value }.average().toInt()
                } else 3
        val avgMood = MoodRating.fromValue(avgVal) ?: MoodRating.Okay

        Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
                Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Column {
                                        Text(
                                                text = "Tendencia",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = contentColor
                                        )
                                        Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                color = Color(0xFF94A3B8),
                                                fontWeight = FontWeight.Medium
                                        )
                                }

                                Surface(
                                        shape = RoundedCornerShape(50),
                                        color =
                                                if (isDarkTheme) Color.White.copy(alpha = 0.1f)
                                                else SensuSecondary
                                ) {
                                        Row(
                                                modifier =
                                                        Modifier.padding(
                                                                horizontal = 12.dp,
                                                                vertical = 4.dp
                                                        ),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                                Box(
                                                        modifier =
                                                                Modifier.size(8.dp)
                                                                        .clip(CircleShape)
                                                                        .background(avgMood.color)
                                                )
                                                Text(
                                                        text =
                                                                "Promedio: ${stringResource(avgMood.label)}",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color =
                                                                if (isDarkTheme) Color(0xFFCBD5E1)
                                                                else Color(0xFF475569)
                                                )
                                        }
                                }
                        }

                        // Graph Area
                        Box(modifier = Modifier.fillMaxWidth().height(192.dp)) {
                                // Y-Axis Grid
                                Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                        (5 downTo 1).forEach { level ->
                                                Row(
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Text(
                                                                text = level.toString(),
                                                                fontSize = 12.sp,
                                                                color = Color(0xFFCBD5E1),
                                                                modifier = Modifier.width(20.dp)
                                                        )
                                                        // Dotted Line
                                                        Canvas(
                                                                modifier =
                                                                        Modifier.fillMaxWidth()
                                                                                .height(1.dp)
                                                        ) {
                                                                drawLine(
                                                                        color = Color(0xFFE2E8F0),
                                                                        start = Offset(0f, 0f),
                                                                        end =
                                                                                Offset(
                                                                                        size.width,
                                                                                        0f
                                                                                ),
                                                                        pathEffect =
                                                                                androidx.compose.ui
                                                                                        .graphics
                                                                                        .PathEffect
                                                                                        .dashPathEffect(
                                                                                                floatArrayOf(
                                                                                                        10f,
                                                                                                        10f
                                                                                                ),
                                                                                                0f
                                                                                        )
                                                                )
                                                        }
                                                }
                                        }
                                }

                                // Curve
                                Canvas(
                                        modifier =
                                                Modifier.fillMaxSize()
                                                        .padding(
                                                                start = 24.dp,
                                                                top = 10.dp,
                                                                bottom = 10.dp
                                                        )
                                ) {
                                        // Decide points based on filter
                                        val pointCount =
                                                if (selectedTimeFilter == TimeFilter.MONTH) 30
                                                else 7
                                        val widthPerPoint = size.width / (pointCount - 1)
                                        val points = mutableListOf<Offset>()

                                        // Create a map for fast lookup: DayOfYear -> MoodRating
                                        val today = Calendar.getInstance()
                                        val moodMap =
                                                recentMoods.associateBy {
                                                        val c = Calendar.getInstance()
                                                        c.timeInMillis = it.timestamp
                                                        "${c.get(Calendar.YEAR)}-${c.get(Calendar.DAY_OF_YEAR)}"
                                                }

                                        for (i in 0 until pointCount) {
                                                // Calculate which day this index represents
                                                // i=0 is (pointCount-1) days ago. i=max is Today.
                                                val c = Calendar.getInstance()
                                                c.add(Calendar.DAY_OF_YEAR, -(pointCount - 1 - i))
                                                val year = c.get(Calendar.YEAR)
                                                val dayOfYear = c.get(Calendar.DAY_OF_YEAR)
                                                val key = "$year-$dayOfYear"

                                                val mood = moodMap[key]
                                                if (mood != null) {
                                                        val x = i * widthPerPoint
                                                        // 5 -> Top (0), 1 -> Bottom (height)
                                                        // Let's add padding so it doesn't touch
                                                        // edges exactly
                                                        val rating = mood.rating.value
                                                        val normalizedY = 1f - (rating / 5f)
                                                        // Scale y to be within 10% to 90% of height
                                                        // for aesthetics
                                                        val y =
                                                                size.height *
                                                                        (normalizedY * 0.8f + 0.1f)
                                                        points.add(Offset(x, y))
                                                }
                                        }

                                        val path =
                                                Path().apply {
                                                        if (points.isNotEmpty()) {
                                                                moveTo(
                                                                        points.first().x,
                                                                        points.first().y
                                                                )
                                                                // Smooth quadratic bezier or simple
                                                                // connections
                                                                for (i in 0 until points.size - 1) {
                                                                        val p1 = points[i]
                                                                        val p2 = points[i + 1]
                                                                        // Basic smoothing
                                                                        quadraticTo(
                                                                                p1.x +
                                                                                        (p2.x -
                                                                                                p1.x) /
                                                                                                2,
                                                                                p1.y,
                                                                                (p1.x + p2.x) / 2,
                                                                                (p1.y + p2.y) / 2
                                                                        )
                                                                        quadraticTo(
                                                                                p1.x +
                                                                                        (p2.x -
                                                                                                p1.x) /
                                                                                                2,
                                                                                p2.y,
                                                                                p2.x,
                                                                                p2.y
                                                                        )
                                                                }
                                                        }
                                                }

                                        // Stroke
                                        drawPath(
                                                path = path,
                                                color = SensuGreen,
                                                style =
                                                        Stroke(
                                                                width = 3.dp.toPx(),
                                                                cap = StrokeCap.Round
                                                        )
                                        )

                                        // Draw Gradient below path
                                        if (points.isNotEmpty()) {
                                                val fillPath =
                                                        Path().apply {
                                                                addPath(path)
                                                                lineTo(points.last().x, size.height)
                                                                lineTo(
                                                                        points.first().x,
                                                                        size.height
                                                                )
                                                                close()
                                                        }
                                                drawPath(
                                                        path = fillPath,
                                                        brush =
                                                                androidx.compose.ui.graphics.Brush
                                                                        .verticalGradient(
                                                                                colors =
                                                                                        listOf(
                                                                                                SensuGreen
                                                                                                        .copy(
                                                                                                                alpha =
                                                                                                                        0.3f
                                                                                                        ),
                                                                                                Color.Transparent
                                                                                        ),
                                                                                endY = size.height
                                                                        )
                                                )
                                        }

                                        // Points circles
                                        if (selectedTimeFilter == TimeFilter.WEEK) {
                                                points.forEach { point ->
                                                        drawCircle(
                                                                color = Color.White,
                                                                radius = 4.dp.toPx(),
                                                                center = point
                                                        )
                                                        drawCircle(
                                                                color = SensuGreen,
                                                                radius = 2.dp.toPx(),
                                                                center = point
                                                        )
                                                }
                                        }
                                }
                        }
                        // X-Axis Labels
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(start = 24.dp), // Align with graph start
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                val count = if (selectedTimeFilter == TimeFilter.MONTH) 6 else 7
                                val today = Calendar.getInstance()
                                // Normalize today to start of day if really needed, but here just
                                // label creation

                                val step = if (selectedTimeFilter == TimeFilter.MONTH) 5 else 1
                                val totalDays =
                                        if (selectedTimeFilter == TimeFilter.MONTH) 30 else 7

                                // We want 'count' labels.
                                // For Week: 7 labels, 1 day apart.
                                // For Month: 6 labels (0, 6, 12, 18, 24, 30 days ago? or 1, 7,
                                // 13...)

                                // Actually, let's just generate the list of days to show logic
                                val daysToShow = mutableListOf<String>()
                                for (i in (count - 1) downTo 0) {
                                        val c = Calendar.getInstance()
                                        // For month: i=5 (today), i=4 (today-5), i=3 (today-10)...
                                        // For week: i=6 (today), i=5 (today-1)...

                                        val daysAgo = i * step
                                        c.add(Calendar.DAY_OF_YEAR, -daysAgo)
                                        val dayStr = c.get(Calendar.DAY_OF_MONTH).toString()
                                        daysToShow.add(dayStr)
                                }
                                // Reversing because we iterate backwards in time above, but Row is
                                // LTR
                                // Wait, the graph draws Left (old) to Right (new)
                                // So we need labels from Oldest -> Newest

                                val labels = mutableListOf<String>()
                                // Start from oldest
                                val startOffset = totalDays - 1
                                for (i in 0 until count) {
                                        val c = Calendar.getInstance() // Today
                                        val daysAgo = startOffset - (i * step)
                                        c.add(Calendar.DAY_OF_YEAR, -daysAgo)
                                        labels.add(c.get(Calendar.DAY_OF_MONTH).toString())
                                }

                                labels.forEach { dayLabel ->
                                        Text(
                                                text = dayLabel,
                                                fontSize = 10.sp,
                                                color = Color(0xFF94A3B8)
                                        )
                                }
                        }
                }
        }
}

@Composable
fun EmotionDistributionCard(
        distribution: Map<MoodEmotion, Int>,
        isDarkTheme: Boolean,
        contentColor: Color
) {
        val cardBg = if (isDarkTheme) SensuCardDark else SensuCardLight

        Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
                Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                                text = "Distribución de Emociones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = contentColor,
                                modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                                // Donut Chart
                                Box(
                                        modifier = Modifier.size(128.dp),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                                // Background circle
                                                drawCircle(
                                                        color =
                                                                if (isDarkTheme)
                                                                        Color.White.copy(
                                                                                alpha = 0.05f
                                                                        )
                                                                else SensuSecondary,
                                                        style = Stroke(width = 16.dp.toPx())
                                                )

                                                // Arcs
                                                var startAngle = -90f
                                                val total = distribution.values.sum()
                                                if (total > 0) {
                                                        distribution.forEach { (emotion, count) ->
                                                                val sweepAngle =
                                                                        (count.toFloat() / total) *
                                                                                360f
                                                                drawArc(
                                                                        color = emotion.color,
                                                                        startAngle = startAngle,
                                                                        sweepAngle = sweepAngle,
                                                                        useCenter = false,
                                                                        style =
                                                                                Stroke(
                                                                                        width =
                                                                                                16.dp.toPx(),
                                                                                        cap =
                                                                                                StrokeCap
                                                                                                        .Round
                                                                                )
                                                                )
                                                                startAngle += sweepAngle
                                                        }
                                                }
                                        }
                                        // Center Text (e.g. Major Emotion)
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                val topEmotion =
                                                        distribution.maxByOrNull { it.value }?.key
                                                val topPct =
                                                        if (distribution.values.sum() > 0)
                                                                (distribution.values.maxOrNull()
                                                                        ?: 0) * 100 /
                                                                        distribution.values.sum()
                                                        else 0

                                                Text(
                                                        text = "$topPct%",
                                                        fontSize = 30.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = contentColor
                                                )
                                                if (topEmotion != null) {
                                                        Text(
                                                                text =
                                                                        stringResource(
                                                                                        id =
                                                                                                topEmotion
                                                                                                        .label
                                                                                )
                                                                                .uppercase(),
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF94A3B8),
                                                                letterSpacing = 1.sp
                                                        )
                                                }
                                        }
                                }

                                // Legend
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        distribution.forEach { (emotion, count) ->
                                                val pct =
                                                        if (distribution.values.sum() > 0)
                                                                (count * 100 /
                                                                        distribution.values.sum())
                                                        else 0
                                                LegendItem(
                                                        stringResource(id = emotion.label)
                                                                .uppercase(),
                                                        "$pct%",
                                                        emotion.color,
                                                        contentColor
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun LegendItem(label: String, pct: String, color: Color, textColor: Color) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                Column {
                        Text(
                                text = label,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                        )
                        Text(
                                text = pct,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                        )
                }
        }
}

@Composable
fun StatsGrid(streak: Int, isStreakActive: Boolean, isDarkTheme: Boolean, contentColor: Color) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                StatCard(
                        iconRes =
                                if (isStreakActive) R.drawable.streak_on else R.drawable.streak_off,
                        iconColor = Color(0xFFf97316),
                        iconBg = Color(0xFFffedd5),
                        value = streak.toString(),
                        label = "Días de Racha",
                        modifier = Modifier.weight(1f),
                        isDarkTheme,
                        contentColor
                )
        }
}

@Composable
fun StatCard(
        @androidx.annotation.DrawableRes iconRes: Int,
        iconColor: Color,
        iconBg: Color,
        value: String,
        label: String,
        modifier: Modifier = Modifier,
        isDarkTheme: Boolean,
        contentColor: Color
) {
        val cardBg = if (isDarkTheme) SensuCardDark else Color.White
        Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = modifier
        ) {
                Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Box(
                                modifier =
                                        Modifier.size(64.dp)
                                                .clip(CircleShape)
                                                .background(
                                                        if (isDarkTheme)
                                                                iconColor.copy(alpha = 0.2f)
                                                        else iconBg
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Icon(
                                        painterResource(
                                                id = iconRes,
                                        ),
                                        contentDescription = null,
                                        tint = iconColor,
                                        modifier = Modifier.size(48.dp)
                                )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                        text = value,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                )
                                Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF64748B)
                                )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                }
        }
}

@Composable
fun RegistrarButton(onClick: () -> Unit) {
        FloatingActionButton(
                onClick = onClick,
                containerColor = Color(0xFF0F172A), // Slate 900
                contentColor = Color.White,
                modifier = Modifier.height(64.dp).width(160.dp)
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                        Box(
                                modifier =
                                        Modifier.size(32.dp)
                                                .clip(CircleShape)
                                                .background(SensuGreen),
                                contentAlignment = Alignment.Center
                        ) {
                                Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null,
                                        tint = Color.Black
                                )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Registrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
        }
}

fun getIconForLevel(level: Float): String {
        return when {
                level > 0.8f -> "😄" // Happy
                level > 0.6f -> "🙂" // Good
                level > 0.4f -> "😐" // Neutral
                else -> "😞" // Sad
        }
}
