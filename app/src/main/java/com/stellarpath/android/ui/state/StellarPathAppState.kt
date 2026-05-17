package com.stellarpath.android.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.stellarpath.android.data.SampleAstroData
import com.stellarpath.android.model.AppSettings
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.ChartSnapshot
import com.stellarpath.android.model.CompatibilityReport
import com.stellarpath.android.model.DailyReading
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.ReferenceTopic
import com.stellarpath.android.model.SpecialEvent
import com.stellarpath.android.model.SubscriptionEntitlement
import com.stellarpath.android.model.ThemeMode
import com.stellarpath.android.model.TransitWindow
import java.time.LocalDate

val LocalStellarPathAppState = staticCompositionLocalOf<StellarPathAppState> {
    error("StellarPathAppState not provided")
}

@Stable
class StellarPathAppState {
    private val sample = SampleAstroData

    var selectedProfileId by mutableStateOf(sample.settings.defaultProfileId ?: sample.profiles.first().id)
        private set

    var selectedCompatibilityPrimaryId by mutableStateOf("self")
        private set

    var selectedCompatibilitySecondaryId by mutableStateOf("partner")
        private set

    var selectedTransitDate by mutableStateOf(sample.transitWindow.activeDate)
        private set

    var settings by mutableStateOf(sample.settings)
        private set

    var entitlement by mutableStateOf(sample.entitlement)
        private set

    var profileEditorDraftName by mutableStateOf("")
        private set

    fun selectProfile(profileId: String) {
        selectedProfileId = profileId
    }

    fun selectCompatibilityPrimary(profileId: String) {
        selectedCompatibilityPrimaryId = profileId
    }

    fun selectCompatibilitySecondary(profileId: String) {
        selectedCompatibilitySecondaryId = profileId
    }

    fun selectTransitDate(date: LocalDate) {
        selectedTransitDate = date
    }

    fun updateThemeMode(themeMode: ThemeMode) {
        settings = settings.copy(themeMode = themeMode)
    }

    fun updateDefaultHouseSystem(houseSystem: HouseSystem) {
        settings = settings.copy(defaultHouseSystem = houseSystem)
    }

    fun updateNotificationPreference(
        dailyReadingEnabled: Boolean? = null,
        transitAlertsEnabled: Boolean? = null,
        specialEventAlertsEnabled: Boolean? = null,
        marketingEnabled: Boolean? = null,
    ) {
        val current = settings.notifications
        settings = settings.copy(
            notifications = current.copy(
                dailyReadingEnabled = dailyReadingEnabled ?: current.dailyReadingEnabled,
                transitAlertsEnabled = transitAlertsEnabled ?: current.transitAlertsEnabled,
                specialEventAlertsEnabled = specialEventAlertsEnabled ?: current.specialEventAlertsEnabled,
                marketingEnabled = marketingEnabled ?: current.marketingEnabled,
            ),
        )
    }

    fun updateShowPremiumPrompts(show: Boolean) {
        settings = settings.copy(showPremiumPrompts = show)
    }

    fun beginProfileEditor(profileName: String = "") {
        profileEditorDraftName = profileName
    }

    fun activeProfile(): BirthProfile = sample.profileById(selectedProfileId)

    fun profile(profileId: String): BirthProfile = sample.profileById(profileId)

    fun profiles(): List<BirthProfile> = sample.profiles

    fun chart(profileId: String): ChartSnapshot = sample.chartFor(profileId)

    /** Natal chart overlaid with transit positions for the given date. */
    fun transitChart(profileId: String, date: LocalDate): ChartSnapshot =
        sample.transitChartFor(profileId, date)

    fun reading(profileId: String): DailyReading = sample.readingFor(profileId)

    /** Daily reading adjusted for the selected transit date. */
    fun transitReading(profileId: String, date: LocalDate): DailyReading =
        sample.transitReadingFor(profileId, date)

    fun compatibilityReport(): CompatibilityReport =
        sample.buildCompatibilityReport(selectedCompatibilityPrimaryId, selectedCompatibilitySecondaryId)

    fun transitWindow(): TransitWindow = sample.transitWindow.copy(activeDate = selectedTransitDate)

    fun referenceTopic(topicId: String): ReferenceTopic = sample.referenceTopic(topicId)

    fun referenceTopics(): List<ReferenceTopic> = sample.referenceTopics

    fun specialEvent(eventId: String): SpecialEvent = sample.specialEvent(eventId)

    fun specialEvents(): List<SpecialEvent> = sample.specialEvents

    fun subscription(): SubscriptionEntitlement = entitlement

    fun settings(): AppSettings = settings
}

@Composable
fun rememberStellarPathAppState(): StellarPathAppState = remember {
    StellarPathAppState()
}
