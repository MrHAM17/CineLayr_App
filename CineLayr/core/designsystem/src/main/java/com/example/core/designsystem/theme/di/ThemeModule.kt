package com.example.core.designsystem.theme.di

import com.example.core.designsystem.theme.ThemeManager
import com.example.core.designsystem.theme.ThemePreferences
import com.example.core.designsystem.theme.ui.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Core_DesignSystem_ThemeModule = module {
    single { ThemePreferences(get()) }
    single { ThemeManager(get()) }
    viewModel { ThemeViewModel(get()) }
}
