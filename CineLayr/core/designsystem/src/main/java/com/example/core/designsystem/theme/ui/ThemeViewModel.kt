package com.example.core.designsystem.theme.ui

import androidx.lifecycle.ViewModel
import com.example.core.designsystem.theme.ThemeManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class ThemeViewModel(
    private val themeManager: ThemeManager
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = themeManager.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, false)

    fun toggleTheme(currentlyDark: Boolean) {
        viewModelScope.launch {
            themeManager.toggleTheme(currentlyDark)
        }
    }
}