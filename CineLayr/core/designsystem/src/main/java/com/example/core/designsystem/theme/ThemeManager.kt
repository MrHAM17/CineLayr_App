package com.example.core.designsystem.theme

import kotlinx.coroutines.flow.Flow

class ThemeManager(private val themePreferences: ThemePreferences) {
    val darkModeFlow: Flow<Boolean> = themePreferences.isDarkMode

    suspend fun toggleTheme(currentlyDark: Boolean) {
        themePreferences.setDarkMode(!currentlyDark)
    }
}
