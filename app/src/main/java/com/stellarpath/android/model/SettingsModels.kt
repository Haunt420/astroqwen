package com.stellarpath.android.model

import androidx.compose.runtime.Immutable

enum class ThemeMode {
    System,
    Light,
    Dark,
}

@Immutable
data class NotificationPreferences(
    val dailyReadingEnabled: Boolean = true,
    val transitAlertsEnabled: Boolean = true,
    val specialEventAlertsEnabled: Boolean = true,
    val marketingEnabled: Boolean = false,
)

@Immutable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val notifications: NotificationPreferences = NotificationPreferences(),
    val defaultProfileId: String? = null,
    val defaultHouseSystem: HouseSystem = HouseSystem.Placidus,
    val showPremiumPrompts: Boolean = true,
)
