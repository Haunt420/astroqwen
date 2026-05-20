package com.stellarpath.android.data.repository

import com.stellarpath.android.model.AppSettings
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun updateThemeMode(themeMode: ThemeMode)

    suspend fun updateDefaultHouseSystem(houseSystem: HouseSystem)

    suspend fun updateDefaultProfile(profileId: String?)

    suspend fun updateNotificationPreference(
        dailyReadingEnabled: Boolean? = null,
        transitAlertsEnabled: Boolean? = null,
        specialEventAlertsEnabled: Boolean? = null,
        marketingEnabled: Boolean? = null,
    )
}
