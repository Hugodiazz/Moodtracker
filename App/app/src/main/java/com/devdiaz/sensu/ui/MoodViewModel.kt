package com.devdiaz.sensu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiaz.sensu.enums.MoodEmotion
import com.devdiaz.sensu.enums.MoodRating
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MoodViewModel @Inject constructor(private val moodRepository: MoodRepository) : ViewModel() {

        private val _uiState = MutableStateFlow(MoodUiState())
        val uiState: StateFlow<MoodUiState> = _uiState.asStateFlow()

        sealed class MoodNavigationEvent {
                object NavigateHome : MoodNavigationEvent()
                data class NavigateToStreakCelebration(val days: Int) : MoodNavigationEvent()
        }

        private val _navigationEvent = Channel<MoodNavigationEvent>()
        val navigationEvent = _navigationEvent.receiveAsFlow()

        fun onMoodSelected(rating: MoodRating) {
                _uiState.update {
                        it.copy(
                                selectedMood = rating,
                                currentStep = MoodCheckInStep.EmotionSelection
                        )
                }
        }

        fun onEmotionSelected(emotion: MoodEmotion) {
                _uiState.update {
                        it.copy(selectedEmotion = emotion, currentStep = MoodCheckInStep.NoteInput)
                }
        }

        fun onNoteChanged(note: String) {
                _uiState.update { it.copy(note = note) }
        }

        fun onBackToMood() {
                _uiState.update {
                        it.copy(
                                selectedMood = null,
                                selectedEmotion = null,
                                currentStep = MoodCheckInStep.MoodSelection
                        )
                }
        }

        fun onBackFromNote() {
                _uiState.update { it.copy(currentStep = MoodCheckInStep.EmotionSelection) }
        }

        fun onSaveEntry() {
                val currentState = uiState.value
                val rating = currentState.selectedMood ?: return
                val emotion = currentState.selectedEmotion ?: return
                val note = currentState.note

                viewModelScope.launch {
                        // Check previous stats to decide on celebration
                        val oldStats = moodRepository.getUserStats().firstOrNull()
                        val today = LocalDate.now().toString()
                        val alreadyLoggedToday = oldStats?.lastLogDate == today

                        val entry =
                                MoodEntry(
                                        rating = rating,
                                        emotion = emotion,
                                        note = note,
                                        dateString = today
                                )
                        moodRepository.insertMoodEntry(entry)

                        // Reset state
                        _uiState.update {
                                it.copy(
                                        selectedMood = null,
                                        selectedEmotion = null,
                                        note = "",
                                        currentStep = MoodCheckInStep.MoodSelection
                                )
                        }

                        // Navigate away logic
                        if (!alreadyLoggedToday) {
                                // Fetch updated stats
                                val newStats = moodRepository.getUserStats().firstOrNull()
                                val streak = newStats?.currentStreak ?: 1
                                _navigationEvent.send(
                                        MoodNavigationEvent.NavigateToStreakCelebration(streak)
                                )
                        } else {
                                _navigationEvent.send(MoodNavigationEvent.NavigateHome)
                        }
                }
        }
}

data class MoodUiState(
        val currentStep: MoodCheckInStep = MoodCheckInStep.MoodSelection,
        val selectedMood: MoodRating? = null,
        val selectedEmotion: MoodEmotion? = null,
        val note: String = "",
        val isLoading: Boolean = false
)

enum class MoodCheckInStep {
        MoodSelection,
        EmotionSelection,
        NoteInput
}
