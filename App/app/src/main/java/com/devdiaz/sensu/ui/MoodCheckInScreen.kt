package com.devdiaz.sensu.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.devdiaz.sensu.enums.MoodRating

@Composable
fun MoodCheckInScreen(
        viewModel: MoodViewModel = hiltViewModel(),
        onNavigateHome: () -> Unit = {},
        onNavigateToStreak: (Int) -> Unit = {}
) {
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                        when (event) {
                                is MoodViewModel.MoodNavigationEvent.NavigateHome ->
                                        onNavigateHome()
                                is MoodViewModel.MoodNavigationEvent.NavigateToStreakCelebration ->
                                        onNavigateToStreak(event.days)
                        }
                }
        }

        AnimatedContent(
                targetState = uiState.currentStep,
                label = "MoodCheckInFlow",
                transitionSpec = {
                        if (targetState.ordinal > initialState.ordinal) {
                                slideInHorizontally { it } + fadeIn() togetherWith
                                        slideOutHorizontally { -it } + fadeOut()
                        } else {
                                slideInHorizontally { -it } + fadeIn() togetherWith
                                        slideOutHorizontally { it } + fadeOut()
                        }
                }
        ) { step ->
                when (step) {
                        MoodCheckInStep.MoodSelection -> {
                                MoodSelectionContent(
                                        onMoodSelected = viewModel::onMoodSelected,
                                        onSkip = onNavigateHome
                                )
                        }
                        MoodCheckInStep.EmotionSelection -> {
                                EmotionSelectionScreen(
                                        selectedEmotion = uiState.selectedEmotion,
                                        onEmotionSelected = viewModel::onEmotionSelected,
                                        onBack = viewModel::onBackToMood,
                                        onSave = {
                                                viewModel.onSaveEntry()
                                                // Optional: onNavigateHome() if we want to leave
                                                // screen after save
                                        }
                                )
                        }
                        MoodCheckInStep.NoteInput -> {
                                NoteInputScreen(
                                        note = uiState.note,
                                        onNoteChanged = viewModel::onNoteChanged,
                                        onSave = viewModel::onSaveEntry,
                                        onSkip = viewModel::onSaveEntry,
                                        onBack = viewModel::onBackFromNote
                                )
                        }
                }
        }
}

@Composable
fun MoodSelectionContent(onMoodSelected: (MoodRating) -> Unit, onSkip: () -> Unit) {
        Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF6F8F7)) // background-light
        ) {
                // Background Blobs
                Box(
                        modifier =
                                Modifier.offset(x = 100.dp, y = (-100).dp)
                                        .size(300.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFF13ec80).copy(alpha = 0.1f),
                                                CircleShape
                                        ) // primary
                )
                Box(
                        modifier =
                                Modifier.offset(x = (-50).dp, y = 50.dp)
                                        .size(200.dp)
                                        .blur(80.dp)
                                        .background(
                                                Color(0xFFDBEAFE).copy(alpha = 0.5f),
                                                CircleShape
                                        ) // blue-100
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
                                        color = Color.White,
                                        modifier = Modifier.size(48.dp)
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Text(text = "🌿", fontSize = 24.sp)
                                        }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                        text = " ¿Cómo te\n" + "sientes hoy?",
                                        style =
                                                MaterialTheme.typography.displaySmall.copy(
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF1E293B), // slate-800
                                                        lineHeight = 40.sp
                                                ),
                                        textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                        text = "CHECK-IN DIARIO",
                                        style =
                                                MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF94A3B8), // slate-400
                                                        letterSpacing = 1.sp
                                                )
                                )
                        }

                        // Grid
                        Column(
                                modifier = Modifier.widthIn(max = 340.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                // Row 1
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        MoodButton(
                                                rating = MoodRating.Terrible,
                                                modifier = Modifier.weight(1f),
                                                onClick = { onMoodSelected(MoodRating.Terrible) }
                                        )
                                        MoodButton(
                                                rating = MoodRating.Bad,
                                                modifier = Modifier.weight(1f),
                                                onClick = { onMoodSelected(MoodRating.Bad) }
                                        )
                                }
                                // Row 2
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        MoodButton(
                                                rating = MoodRating.Okay,
                                                modifier = Modifier.weight(1f),
                                                onClick = { onMoodSelected(MoodRating.Okay) }
                                        )
                                        MoodButton(
                                                rating = MoodRating.Good,
                                                modifier = Modifier.weight(1f),
                                                onClick = { onMoodSelected(MoodRating.Good) }
                                        )
                                }

                                // Full width "Incredible"
                                MoodButtonFull(
                                        rating = MoodRating.Incredible,
                                        onClick = { onMoodSelected(MoodRating.Incredible) }
                                )
                        }

                        // Footer
                        TextButton(onClick = onSkip, modifier = Modifier.padding(bottom = 24.dp)) {
                                Text(
                                        text = "Omitir por ahora",
                                        color = Color(0xFF94A3B8),
                                        fontWeight = FontWeight.SemiBold
                                )
                        }
                }
        }
}

@Composable
fun MoodButton(rating: MoodRating, modifier: Modifier = Modifier, onClick: () -> Unit) {
        val backgroundColor = rating.color

        Column(
                modifier =
                        modifier.aspectRatio(1f)
                                .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(24.dp),
                                        spotColor = Color.Black.copy(alpha = 0.05f)
                                )
                                .clip(RoundedCornerShape(24.dp))
                                .background(backgroundColor.copy(alpha = 0.5f))
                                .clickable { onClick() }
                                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
                Text(text = rating.icon, fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = stringResource(id = rating.label),
                        style =
                                MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B) // slate-500
                                )
                )
        }
}

@Composable
fun MoodButtonFull(rating: MoodRating, onClick: () -> Unit) {
        val backgroundColor = rating.color

        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(96.dp)
                                .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(24.dp),
                                        spotColor = Color.Black.copy(alpha = 0.05f)
                                )
                                .clip(RoundedCornerShape(24.dp))
                                .background(backgroundColor.copy(alpha = 0.2f))
                                .clickable { onClick() }
                                .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
        ) {
                Text(text = rating.icon, fontSize = 48.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                        Text(
                                text = stringResource(id = rating.label),
                                style =
                                        MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E293B)
                                        )
                        )
                        Text(
                                text = "Me siento genial",
                                style =
                                        MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF64748B)
                                        )
                        )
                }
        }
}
