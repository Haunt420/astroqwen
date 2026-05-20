package com.stellarpath.android.data

import com.stellarpath.android.data.ephemeris.AstroCalculationEngine
import com.stellarpath.android.model.AstrologyBody
import com.stellarpath.android.model.BirthLocation
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.BirthTimePrecision
import com.stellarpath.android.model.AppSettings
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.NotificationPreferences
import com.stellarpath.android.model.ProfileType
import com.stellarpath.android.model.ReferenceSection
import com.stellarpath.android.model.ReferenceTopic
import com.stellarpath.android.model.SpecialEvent
import com.stellarpath.android.model.SpecialEventType
import com.stellarpath.android.model.ThemeMode
import com.stellarpath.android.model.TransitMarker
import com.stellarpath.android.model.TransitMarkerType
import com.stellarpath.android.model.TransitWindow
import java.time.LocalDate
import java.time.LocalTime

object SampleAstroData {
    @Volatile
    private var calculationEngine: AstroCalculationEngine? = null

    fun configure(engine: AstroCalculationEngine) {
        calculationEngine = engine
    }

    val profiles: List<BirthProfile> = listOf(
        BirthProfile(
            id = "self",
            displayName = "Aria",
            profileType = ProfileType.Self,
            birthDate = LocalDate.of(1994, 4, 18),
            birthTime = LocalTime.of(7, 42),
            birthTimePrecision = BirthTimePrecision.Exact,
            location = BirthLocation(
                city = "Portland",
                region = "OR",
                countryCode = "US",
                latitude = 45.5152,
                longitude = -122.6784,
                timezoneId = "America/Los_Angeles",
            ),
            houseSystem = HouseSystem.Placidus,
            notes = "Primary profile",
        ),
        BirthProfile(
            id = "partner",
            displayName = "Miles",
            profileType = ProfileType.Partner,
            birthDate = LocalDate.of(1992, 11, 2),
            birthTime = LocalTime.of(16, 15),
            birthTimePrecision = BirthTimePrecision.Exact,
            location = BirthLocation(
                city = "Seattle",
                region = "WA",
                countryCode = "US",
                latitude = 47.6062,
                longitude = -122.3321,
                timezoneId = "America/Los_Angeles",
            ),
            houseSystem = HouseSystem.Placidus,
        ),
        BirthProfile(
            id = "friend",
            displayName = "Jun",
            profileType = ProfileType.Friend,
            birthDate = LocalDate.of(1995, 8, 23),
            birthTimePrecision = BirthTimePrecision.Unknown,
            location = BirthLocation(
                city = "Austin",
                region = "TX",
                countryCode = "US",
                latitude = 30.2672,
                longitude = -97.7431,
                timezoneId = "America/Chicago",
            ),
            houseSystem = HouseSystem.WholeSign,
            notes = "Birth time unavailable",
        ),
        BirthProfile(
            id = "family",
            displayName = "Iris",
            profileType = ProfileType.Family,
            birthDate = LocalDate.of(1968, 2, 11),
            birthTime = LocalTime.of(9, 5),
            birthTimePrecision = BirthTimePrecision.Approximate,
            location = BirthLocation(
                city = "San Diego",
                region = "CA",
                countryCode = "US",
                latitude = 32.7157,
                longitude = -117.1611,
                timezoneId = "America/Los_Angeles",
            ),
            houseSystem = HouseSystem.Equal,
        ),
        BirthProfile(
            id = "crush",
            displayName = "Noah",
            profileType = ProfileType.Crush,
            birthDate = LocalDate.of(1996, 12, 29),
            birthTime = LocalTime.of(1, 22),
            birthTimePrecision = BirthTimePrecision.Exact,
            location = BirthLocation(
                city = "Denver",
                region = "CO",
                countryCode = "US",
                latitude = 39.7392,
                longitude = -104.9903,
                timezoneId = "America/Denver",
            ),
            houseSystem = HouseSystem.Placidus,
        ),
    )

    val transitWindow: TransitWindow = TransitWindow(
        profileId = "self",
        startDate = LocalDate.of(2026, 5, 1),
        endDate = LocalDate.of(2026, 8, 31),
        activeDate = LocalDate.of(2026, 6, 12),
        markers = listOf(
            TransitMarker(
                id = "mercury-retrograde",
                date = LocalDate.of(2026, 6, 8),
                title = "Mercury retrograde station",
                type = TransitMarkerType.RetrogradeStation,
                summary = "A review period begins around communication, scheduling, and short-term plans.",
            ),
            TransitMarker(
                id = "equinox",
                date = LocalDate.of(2026, 6, 21),
                title = "Equinox",
                type = TransitMarkerType.Equinox,
                summary = "A seasonal pivot shifts the tone from maintenance to momentum.",
            ),
            TransitMarker(
                id = "full-moon",
                date = LocalDate.of(2026, 7, 4),
                title = "Full moon emphasis",
                type = TransitMarkerType.TransitPeak,
                summary = "Expect visibility around a decision, message, or public-facing milestone.",
            ),
            TransitMarker(
                id = "jupiter-birthday",
                date = LocalDate.of(2026, 7, 21),
                title = "Jupiter birthday",
                type = TransitMarkerType.PlanetBirthday,
                summary = "A growth-oriented marker points to opportunity, expansion, and optimism.",
            ),
        ),
    )

    val specialEvents: List<SpecialEvent> = listOf(
        SpecialEvent(
            id = "eclipse-2026-1",
            title = "Solar eclipse",
            date = LocalDate.of(2026, 6, 18),
            type = SpecialEventType.Eclipse,
            summary = "A compact reset point with strong emphasis on the profile's public-facing themes.",
        ),
        SpecialEvent(
            id = "solstice-2026",
            title = "Solstice",
            date = LocalDate.of(2026, 6, 21),
            type = SpecialEventType.Solstice,
            summary = "Seasonal turning point; a natural checkpoint for goals and schedules.",
        ),
        SpecialEvent(
            id = "retrograde-2026",
            title = "Mercury retrograde",
            date = LocalDate.of(2026, 6, 8),
            type = SpecialEventType.Retrograde,
            summary = "Review, revise, and verify before committing to irreversible plans.",
        ),
    )

    val referenceTopics: List<ReferenceTopic> = listOf(
        ReferenceTopic(
            id = "elements",
            title = "Elements",
            summary = "Fire, Earth, Air, and Water describe the chart's base temperament.",
            sections = listOf(
                ReferenceSection(id = "fire", title = "Fire", body = "Direct, expressive, fast to start, and motivated by momentum."),
                ReferenceSection(id = "earth", title = "Earth", body = "Concrete, patient, stabilizing, and tuned to results that last."),
            ),
        ),
        ReferenceTopic(
            id = "qualities",
            title = "Qualities",
            summary = "Cardinal, Fixed, and Mutable describe how energy moves.",
            sections = listOf(
                ReferenceSection(id = "cardinal", title = "Cardinal", body = "Initiates and pushes things into motion."),
                ReferenceSection(id = "mutable", title = "Mutable", body = "Adapts, reframes, and changes shape to fit context."),
            ),
        ),
        ReferenceTopic(
            id = "dualities",
            title = "Dualities",
            summary = "Positive/negative, wet/dry, and other paired lenses help explain chart balance.",
            sections = listOf(
                ReferenceSection(id = "yang", title = "Yang and Yin", body = "Outgoing and receptive describe how energy tends to express itself."),
            ),
        ),
        ReferenceTopic(
            id = "houses",
            title = "House systems",
            summary = "House systems shift how life areas are segmented and interpreted.",
            sections = listOf(
                ReferenceSection(id = "placidus", title = "Placidus", body = "A popular time-based system that many chart readers use by default."),
                ReferenceSection(id = "whole-sign", title = "Whole sign", body = "Each sign becomes an entire house, creating a clean one-sign-one-house structure."),
            ),
        ),
        ReferenceTopic(
            id = "special-events",
            title = "Special events",
            summary = "Eclipses, retrogrades, solstices, equinoxes, and planetary birthdays carry timing significance.",
            sections = listOf(
                ReferenceSection(id = "retrograde", title = "Retrograde", body = "A review cycle that asks for revision, patience, and close reading of details."),
            ),
        ),
        ReferenceTopic(
            id = "planets",
            title = "Planet meanings",
            summary = "The bodies in the chart describe different functions, drives, and response patterns.",
            sections = listOf(
                ReferenceSection(id = "sun", title = "Sun", body = "Identity, vitality, and the central organizing principle.", relatedBodies = listOf(AstrologyBody.Sun)),
                ReferenceSection(id = "moon", title = "Moon", body = "Mood, instinct, comfort, and immediate emotional response.", relatedBodies = listOf(AstrologyBody.Moon)),
            ),
        ),
    )

    val settings = AppSettings(
        themeMode = ThemeMode.System,
        notifications = NotificationPreferences(
            dailyReadingEnabled = true,
            transitAlertsEnabled = true,
            specialEventAlertsEnabled = true,
            marketingEnabled = false,
        ),
        defaultProfileId = "self",
        defaultHouseSystem = HouseSystem.Placidus,
    )

    fun chartFor(profileId: String) = engine().natalChart(profileById(profileId))

    fun readingFor(profileId: String) = engine().dailyReading(profileById(profileId), LocalDate.now())

    fun transitChartFor(profileId: String, date: LocalDate) = engine().transitChart(profileById(profileId), date)

    fun transitReadingFor(profileId: String, date: LocalDate) = engine().dailyReading(profileById(profileId), date)

    fun profileById(profileId: String): BirthProfile =
        profiles.firstOrNull { it.id == profileId } ?: profiles.first()

    fun referenceTopic(topicId: String): ReferenceTopic =
        referenceTopics.firstOrNull { it.id == topicId } ?: referenceTopics.first()

    fun specialEvent(eventId: String): SpecialEvent =
        specialEvents.firstOrNull { it.id == eventId } ?: specialEvents.first()

    fun buildCompatibilityReport(primaryId: String, secondaryId: String) =
        engine().compatibilityReport(profileById(primaryId), profileById(secondaryId))

    private fun engine(): AstroCalculationEngine =
        calculationEngine ?: error("SampleAstroData calculation engine has not been configured")
}
