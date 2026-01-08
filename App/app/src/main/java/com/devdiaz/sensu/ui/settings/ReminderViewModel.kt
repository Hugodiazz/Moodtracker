package com.devdiaz.sensu.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiaz.sensu.reminder.ReminderScheduler
import com.devdiaz.sensu.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReminderUiState(val hour: Int = 20, val minute: Int = 0, val isEnabled: Boolean = false)

@HiltViewModel
class ReminderViewModel
@Inject
constructor(
        private val reminderRepository: ReminderRepository,
        private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val (hour, minute) = reminderRepository.getReminderTime()
        val isEnabled = reminderRepository.isReminderEnabled()
        _uiState.update { it.copy(hour = hour, minute = minute, isEnabled = isEnabled) }
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update { it.copy(hour = hour, minute = minute) }
    }

    fun onToggleChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(isEnabled = isEnabled) }
        // If enabling, we might want to schedule immediately or wait for save?
        // UI says "Save Settings", so we probably wait.
        // But the toggle might imply immediate action.
        // Let's stick to "Save" button for persisting everything to avoid partial states.
        // Actually, for toggle, users expect immediate feedback in logic often,
        // but the "Save" button wrapper implies a batch save.
        // I will update state only here.
    }

    fun saveSettings() {
        viewModelScope.launch {
            val state = _uiState.value
            android.util.Log.d(
                    "ReminderViewModel",
                    "Saving reminder: hour=${state.hour}, minute=${state.minute}"
            )
            reminderRepository.saveReminderTime(state.hour, state.minute)
            reminderRepository.setReminderEnabled(state.isEnabled)

            if (state.isEnabled) {
                reminderScheduler.schedule()
            } else {
                reminderScheduler.cancel()
            }
        }
    }
}
