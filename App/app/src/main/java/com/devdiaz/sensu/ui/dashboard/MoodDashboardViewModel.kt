package com.devdiaz.sensu.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiaz.sensu.enums.MoodEmotion
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.model.UserStats
import com.devdiaz.sensu.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MoodDashboardViewModel @Inject constructor(private val moodRepository: MoodRepository) :
        ViewModel() {

    private val _uiState = MutableStateFlow(MoodDashboardUiState())
    val uiState: StateFlow<MoodDashboardUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        loadJob?.cancel()
        loadJob =
                viewModelScope.launch {
                    val filter = _uiState.value.selectedTimeFilter
                    val days = if (filter == TimeFilter.MONTH) 30 else 7
                    val end = System.currentTimeMillis()
                    val start = end - (days * 24L * 60 * 60 * 1000)

                    combine(
                                    moodRepository.getMoodEntriesInRange(start, end),
                                    moodRepository.getUserStats()
                            ) { entries, userStats -> Pair(entries, userStats) }
                            .collectLatest { (entries, userStats) ->
                                updateDashboardState(entries, userStats)
                            }
                }
    }

    private fun updateDashboardState(entries: List<MoodEntry>, userStats: UserStats?) {
        val distribution = calculateEmotionDistribution(entries)

        val today = LocalDate.now().toString()
        val isStreakActive = userStats?.lastLogDate == today
        val currentStreak = userStats?.currentStreak ?: 0

        _uiState.update {
            it.copy(
                    streak = currentStreak,
                    isStreakActive = isStreakActive,
                    recentMoods = entries,
                    emotionDistribution = distribution,
                    isLoading = false
            )
        }
    }

    private fun calculateEmotionDistribution(entries: List<MoodEntry>): Map<MoodEmotion, Int> {
        if (entries.isEmpty()) return emptyMap()

        val total = entries.size
        val counts = entries.groupingBy { it.emotion }.eachCount()

        return counts.mapValues { (_, count) -> (count * 100) / total }
    }

    fun onTimeFilterSelected(filter: TimeFilter) {
        _uiState.update { it.copy(selectedTimeFilter = filter) }
        loadData()
    }
}

data class MoodDashboardUiState(
        val streak: Int = 0,
        val isStreakActive: Boolean = false,
        val recentMoods: List<MoodEntry> = emptyList(),
        val emotionDistribution: Map<MoodEmotion, Int> = emptyMap(),
        val selectedTimeFilter: TimeFilter = TimeFilter.WEEK,
        val isLoading: Boolean = true
)

enum class TimeFilter(val label: String) {
    WEEK("Semana"),
    MONTH("Mes")
}
