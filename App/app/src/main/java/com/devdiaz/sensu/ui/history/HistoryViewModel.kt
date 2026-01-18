package com.devdiaz.sensu.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(private val moodRepository: MoodRepository) :
        ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            moodRepository.getAllMoodEntries().collectLatest { entries ->
                val grouped = groupEntries(entries)
                _uiState.update { it.copy(historyItems = grouped, isLoading = false) }
            }
        }
    }

    private fun groupEntries(entries: List<MoodEntry>): List<HistoryItem> {
        if (entries.isEmpty()) return emptyList()

        // Sort descending by timestamp
        val sorted = entries.sortedByDescending { it.timestamp }
        val groupedItems = mutableListOf<HistoryItem>()

        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val oneWeekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }

        var currentSection = ""

        sorted.forEach { entry ->
            val entryDate = Calendar.getInstance().apply { timeInMillis = entry.timestamp }

            val section =
                    when {
                        isSameDay(entryDate, today) -> "Today, " + formatDate(entryDate)
                        isSameDay(entryDate, yesterday) -> "Yesterday, " + formatDate(entryDate)
                        entryDate.after(oneWeekAgo) -> "This Week"
                        else -> "Older" // Or "Last Week", "Last Month" logic
                    }

            // Refined Logic for "This Week" vs "Last Week" purely based on Calendar weeks not just
            // 7 days?
            // For now, let's stick to simple "This Week" (last 7 days excluding today/yesterday) vs
            // "Last Week" (7-14 days ago)
            // But to match the design "This Week", "Last Week", let's use a simpler heuristic or
            // specific sections.

            // Design Reference has: "This Week" (Header) -> "Today", "Yesterday".
            // Then "Last Week" (Header) -> "Monday, Oct 21", "Sunday, Oct 20".

            // Let's adopt a structure where we assume headers map to broader timeframes if needed,
            // OR we just map strictly to the date if that's what the design "Today, Oct 24" implies
            // is the item header.

            // Looking at design:
            // H3: "This Week"
            //   Card: "Today, Oct 24" ...
            //   Card: "Yesterday, Oct 23" ...
            // H3: "Last Week"
            //   Card: "Monday, Oct 21" ...

            // So we have Broad Sections (H3) and then Cards.
            // Let's calculate the Broad Section.

            val broadSection = getBroadSection(entryDate, today)

            if (broadSection != currentSection) {
                groupedItems.add(HistoryItem.SectionHeader(broadSection))
                currentSection = broadSection
            }

            groupedItems.add(HistoryItem.Entry(entry))
        }

        return groupedItems
    }

    private fun getBroadSection(date: Calendar, today: Calendar): String {
        val diffMillis = today.timeInMillis - date.timeInMillis
        val diffDays = diffMillis / (24 * 60 * 60 * 1000)

        return when {
            diffDays < 7 -> "Esta semana"
            diffDays < 14 -> "Semana pasada"
            diffDays < 30 -> "Este mes"
            else -> "Más antiguo"
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
}

data class HistoryUiState(
        val historyItems: List<HistoryItem> = emptyList(),
        val isLoading: Boolean = true
)

sealed class HistoryItem {
    data class SectionHeader(val title: String) : HistoryItem()
    data class Entry(val entry: MoodEntry) : HistoryItem()
}
