package com.stellarpath.android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.stellarpath.android.model.AppSettings
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.NotificationPreferences
import com.stellarpath.android.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore by preferencesDataStore(name = "stellarpath_settings")

class DataStoreSettingsRepository(
    private val context: Context,
) : SettingsRepository {
    private val dataStore = context.settingsDataStore

    override val settings: Flow<AppSettings> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map(::mapSettings)

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { prefs ->
            prefs[SettingKeys.ThemeMode] = themeMode.name
        }
    }

    override suspend fun updateDefaultHouseSystem(houseSystem: HouseSystem) {
        dataStore.edit { prefs ->
            prefs[SettingKeys.DefaultHouseSystem] = houseSystem.name
        }
    }

    override suspend fun updateDefaultProfile(profileId: String?) {
        dataStore.edit { prefs ->
            if (profileId == null) {
                prefs.remove(SettingKeys.DefaultProfileId)
            } else {
                prefs[SettingKeys.DefaultProfileId] = profileId
            }
        }
    }

    override suspend fun updateShowPremiumPrompts(showPremiumPrompts: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingKeys.ShowPremiumPrompts] = showPremiumPrompts
        }
    }

    override suspend fun updateNotificationPreference(
        dailyReadingEnabled: Boolean?,
        transitAlertsEnabled: Boolean?,
        specialEventAlertsEnabled: Boolean?,
        marketingEnabled: Boolean?,
    ) {
        dataStore.edit { prefs ->
            dailyReadingEnabled?.let { prefs[SettingKeys.DailyReadingEnabled] = it }
            transitAlertsEnabled?.let { prefs[SettingKeys.TransitAlertsEnabled] = it }
            specialEventAlertsEnabled?.let { prefs[SettingKeys.SpecialEventAlertsEnabled] = it }
            marketingEnabled?.let { prefs[SettingKeys.MarketingEnabled] = it }
        }
    }

    private fun mapSettings(prefs: Preferences): AppSettings {
        val notifications = NotificationPreferences(
            dailyReadingEnabled = prefs[SettingKeys.DailyReadingEnabled] ?: true,
            transitAlertsEnabled = prefs[SettingKeys.TransitAlertsEnabled] ?: true,
            specialEventAlertsEnabled = prefs[SettingKeys.SpecialEventAlertsEnabled] ?: true,
            marketingEnabled = prefs[SettingKeys.MarketingEnabled] ?: false,
        )

        return AppSettings(
            themeMode = enumValueOrDefault(prefs[SettingKeys.ThemeMode], ThemeMode.System),
            notifications = notifications,
            defaultProfileId = prefs[SettingKeys.DefaultProfileId],
            defaultHouseSystem = enumValueOrDefault(
                prefs[SettingKeys.DefaultHouseSystem],
                HouseSystem.Placidus,
            ),
            showPremiumPrompts = prefs[SettingKeys.ShowPremiumPrompts] ?: true,
        )
    }
}

private object SettingKeys {
    val ThemeMode = stringPreferencesKey("theme_mode")
    val DefaultProfileId = stringPreferencesKey("default_profile_id")
    val DefaultHouseSystem = stringPreferencesKey("default_house_system")
    val ShowPremiumPrompts = booleanPreferencesKey("show_premium_prompts")

    val DailyReadingEnabled = booleanPreferencesKey("daily_reading_enabled")
    val TransitAlertsEnabled = booleanPreferencesKey("transit_alerts_enabled")
    val SpecialEventAlertsEnabled = booleanPreferencesKey("special_event_alerts_enabled")
    val MarketingEnabled = booleanPreferencesKey("marketing_enabled")
}

private inline fun <reified T : Enum<T>> enumValueOrDefault(
    raw: String?,
    fallback: T,
): T = if (raw == null) {
    fallback
} else {
    runCatching { enumValueOf<T>(raw) }.getOrDefault(fallback)
}
