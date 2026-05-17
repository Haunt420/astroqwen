package com.stellarpath.android.data

import com.stellarpath.android.model.AstrologyBody
import com.stellarpath.android.model.Aspect
import com.stellarpath.android.model.AspectType
import com.stellarpath.android.model.BirthLocation
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.BirthTimePrecision
import com.stellarpath.android.model.ChartPlacement
import com.stellarpath.android.model.ChartPrecision
import com.stellarpath.android.model.ChartSnapshot
import com.stellarpath.android.model.ChartSummary
import com.stellarpath.android.model.CompatibilityDimension
import com.stellarpath.android.model.CompatibilityDimensionType
import com.stellarpath.android.model.CompatibilityReport
import com.stellarpath.android.model.HouseCusp
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.InterpretationBlock
import com.stellarpath.android.model.NotificationPreferences
import com.stellarpath.android.model.ProfileType
import com.stellarpath.android.model.ReferenceSection
import com.stellarpath.android.model.ReferenceTopic
import com.stellarpath.android.model.DailyReading
import com.stellarpath.android.model.SpecialEvent
import com.stellarpath.android.model.SpecialEventType
import com.stellarpath.android.model.SubscriptionEntitlement
import com.stellarpath.android.model.SubscriptionTier
import com.stellarpath.android.model.ThemeMode
import com.stellarpath.android.model.TransitMarker
import com.stellarpath.android.model.TransitMarkerType
import com.stellarpath.android.model.TransitWindow
import com.stellarpath.android.model.ZodiacSign
import com.stellarpath.android.model.AppSettings
import com.stellarpath.android.model.EntitlementFeature
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

object SampleAstroData {
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

    val charts: Map<String, ChartSnapshot> = profiles.associate { profile ->
        profile.id to buildChart(profile.id, profile.houseSystem)
    }

    val readings: Map<String, DailyReading> = profiles.associate { profile ->
        profile.id to DailyReading(
            profileId = profile.id,
            date = LocalDate.now(),
            generatedAt = Instant.now(),
            headline = when (profile.id) {
                "self" -> "A productive rhythm returns"
                "partner" -> "The relationship climate feels steady"
                "friend" -> "A direct conversation clears the air"
                "family" -> "Practical support lands well"
                else -> "Curiosity works better than assumption"
            },
            summary = when (profile.id) {
                "self" -> "Focus on the next concrete step instead of trying to solve everything at once."
                "partner" -> "A small adjustment in timing improves the tone of the entire day."
                "friend" -> "Say what you mean, then leave room for the other person to respond."
                "family" -> "Keep the agenda simple and the expectations explicit."
                else -> "A lighter, more open approach gets a better result than forcing certainty."
            },
            blocks = listOf(
                InterpretationBlock(
                    id = "${profile.id}-focus",
                    title = "Primary focus",
                    body = "Your chart suggests a practical lane today. Handle one visible win before expanding the scope.",
                    relatedBodies = listOf(AstrologyBody.Sun, AstrologyBody.Moon),
                ),
                InterpretationBlock(
                    id = "${profile.id}-pace",
                    title = "Best pacing",
                    body = "Keep your tempo measured. If the first version is good enough, ship it and refine later.",
                    relatedBodies = listOf(AstrologyBody.Mercury, AstrologyBody.Saturn),
                ),
                InterpretationBlock(
                    id = "${profile.id}-social",
                    title = "Social tone",
                    body = "Warmth matters more than precision right now. Lead with clarity and let the details follow.",
                    relatedBodies = listOf(AstrologyBody.Venus, AstrologyBody.Mars),
                ),
            ),
        )
    }

    val transitWindow: TransitWindow = TransitWindow(
        profileId = "self",
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusDays(120),
        activeDate = LocalDate.now().plusDays(12),
        markers = listOf(
            TransitMarker(
                id = "mercury-retrograde",
                date = LocalDate.now().plusDays(7),
                title = "Mercury retrograde station",
                type = TransitMarkerType.RetrogradeStation,
                summary = "A review period begins around communication, scheduling, and short-term plans.",
            ),
            TransitMarker(
                id = "equinox",
                date = LocalDate.now().plusDays(23),
                title = "Equinox",
                type = TransitMarkerType.Equinox,
                summary = "A seasonal pivot shifts the tone from maintenance to momentum.",
            ),
            TransitMarker(
                id = "full-moon",
                date = LocalDate.now().plusDays(34),
                title = "Full moon emphasis",
                type = TransitMarkerType.TransitPeak,
                summary = "Expect visibility around a decision, message, or public-facing milestone.",
            ),
            TransitMarker(
                id = "jupiter-birthday",
                date = LocalDate.now().plusDays(51),
                title = "Jupiter birthday",
                type = TransitMarkerType.PlanetBirthday,
                summary = "A growth-oriented marker points to opportunity, expansion, and optimism.",
            ),
        ),
    )

    val compatibilityReport: CompatibilityReport =
        buildCompatibilityReport(primaryId = "self", secondaryId = "partner")

    val specialEvents: List<SpecialEvent> = listOf(
        SpecialEvent(
            id = "eclipse-2026-1",
            title = "Solar eclipse",
            date = LocalDate.now().plusDays(18),
            type = SpecialEventType.Eclipse,
            summary = "A compact reset point with strong emphasis on the profile's public-facing themes.",
        ),
        SpecialEvent(
            id = "solstice-2026",
            title = "Solstice",
            date = LocalDate.now().plusDays(46),
            type = SpecialEventType.Solstice,
            summary = "Seasonal turning point; a natural checkpoint for goals and schedules.",
        ),
        SpecialEvent(
            id = "retrograde-2026",
            title = "Mercury retrograde",
            date = LocalDate.now().plusDays(7),
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
        showPremiumPrompts = true,
    )

    val entitlement = SubscriptionEntitlement(
        tier = SubscriptionTier.Free,
        active = true,
        features = setOf(),
        expiresAt = null,
        autoRenew = false,
        storeProductId = null,
    )

    fun chartFor(profileId: String): ChartSnapshot = charts[profileId] ?: charts.getValue("self")

    fun readingFor(profileId: String): DailyReading = readings[profileId] ?: readings.getValue("self")

    fun profileById(profileId: String): BirthProfile =
        profiles.firstOrNull { it.id == profileId } ?: profiles.first()

    fun referenceTopic(topicId: String): ReferenceTopic =
        referenceTopics.firstOrNull { it.id == topicId } ?: referenceTopics.first()

    fun specialEvent(eventId: String): SpecialEvent =
        specialEvents.firstOrNull { it.id == eventId } ?: specialEvents.first()

    /** Returns a natal chart overlaid with sample transit placements for the given date. */
    fun transitChartFor(profileId: String, date: LocalDate): ChartSnapshot {
        val natal = chartFor(profileId)
        val dayOffset = (date.toEpochDay() % 30).toFloat() / 30f
        val transitPlacements = listOf(
            ChartPlacement(AstrologyBody.Sun, ZodiacSign.entries[((date.monthValue + 2) % 12)], 10.0 + dayOffset * 28, house = null),
            ChartPlacement(AstrologyBody.Moon, ZodiacSign.entries[((date.dayOfMonth) % 12)], 5.0 + dayOffset * 25, house = null),
            ChartPlacement(AstrologyBody.Mercury, ZodiacSign.entries[((date.monthValue + 1) % 12)], 18.0 + dayOffset * 22, house = null),
            ChartPlacement(AstrologyBody.Venus, ZodiacSign.entries[((date.monthValue + 3) % 12)], 8.0 + dayOffset * 15, house = null),
            ChartPlacement(AstrologyBody.Mars, ZodiacSign.entries[((date.monthValue + 5) % 12)], 22.0 + dayOffset * 12, house = null),
            ChartPlacement(AstrologyBody.Jupiter, ZodiacSign.Taurus, 15.0 + dayOffset * 2, house = null),
            ChartPlacement(AstrologyBody.Saturn, ZodiacSign.Pisces, 11.0 + dayOffset * 1, house = null),
        )
        val transitAspects = listOf(
            Aspect(AstrologyBody.Sun, AstrologyBody.Moon, AspectType.Trine, 1.4),
            Aspect(AstrologyBody.Mercury, AstrologyBody.Saturn, AspectType.Conjunction, 0.8),
            Aspect(AstrologyBody.Venus, AstrologyBody.Mars, AspectType.Square, 2.2),
            Aspect(AstrologyBody.Jupiter, AstrologyBody.Sun, AspectType.Sextile, 1.1),
        )
        return natal.copy(
            transitPlacements = transitPlacements,
            transitAspects = transitAspects,
        )
    }

    fun transitReadingFor(profileId: String, date: LocalDate): DailyReading {
        val base = readingFor(profileId)
        return base.copy(
            date = date,
            headline = if (date == LocalDate.now()) base.headline else "Transit reading for $date",
            summary = if (date == LocalDate.now()) base.summary
            else "The planetary positions on this date activate themes around timing, clarity, and forward motion.",
        )
    }

    fun buildCompatibilityReport(primaryId: String, secondaryId: String): CompatibilityReport {
        val primary = profileById(primaryId)
        val secondary = profileById(secondaryId)
        val blend = "${primary.displayName} × ${secondary.displayName}"
        return CompatibilityReport(
            primaryProfileId = primary.id,
            secondaryProfileId = secondary.id,
            generatedAt = Instant.now(),
            elementMatch = CompatibilityDimension(
                type = CompatibilityDimensionType.ElementMatch,
                label = "Matching elements",
                score = 84,
                summary = "$blend shares enough elemental overlap to feel easy and recognizable.",
                details = "The element blend points to a supportive baseline with enough contrast to stay interesting.",
            ),
            harmony = CompatibilityDimension(
                type = CompatibilityDimensionType.Harmony,
                label = "Harmony",
                score = 78,
                summary = "The emotional cadence between both charts is mostly cooperative.",
                details = "Soft aspects and pacing indicators suggest a low-friction rhythm when plans are made clearly.",
            ),
            passion = CompatibilityDimension(
                type = CompatibilityDimensionType.Passion,
                label = "Passion",
                score = 91,
                summary = "There is strong charge and curiosity here.",
                details = "The Venus/Mars axis is energetic, giving the connection a visible spark and strong pull.",
            ),
            overallSummary = "This pair reads as engaging, affectionate, and durable if expectations stay explicit.",
        )
    }

    private fun buildChart(profileId: String, houseSystem: HouseSystem): ChartSnapshot {
        val now = Instant.now()
        val placements = listOf(
            ChartPlacement(AstrologyBody.Sun, ZodiacSign.Aries, 18.2, house = 1),
            ChartPlacement(AstrologyBody.Moon, ZodiacSign.Cancer, 4.7, house = 4),
            ChartPlacement(AstrologyBody.Mercury, ZodiacSign.Pisces, 29.1, house = 12),
            ChartPlacement(AstrologyBody.Venus, ZodiacSign.Taurus, 12.6, house = 2),
            ChartPlacement(AstrologyBody.Mars, ZodiacSign.Gemini, 7.4, house = 3),
            ChartPlacement(AstrologyBody.Jupiter, ZodiacSign.Leo, 23.0, house = 5),
            ChartPlacement(AstrologyBody.Saturn, ZodiacSign.Aquarius, 15.9, house = 11),
            ChartPlacement(AstrologyBody.Uranus, ZodiacSign.Taurus, 2.8, house = 2),
            ChartPlacement(AstrologyBody.Neptune, ZodiacSign.Pisces, 24.3, house = 12),
            ChartPlacement(AstrologyBody.Pluto, ZodiacSign.Capricorn, 29.5, house = 10),
            ChartPlacement(AstrologyBody.Ascendant, ZodiacSign.Libra, 9.0, house = 1),
            ChartPlacement(AstrologyBody.Midheaven, ZodiacSign.Cancer, 11.0, house = 10),
        )
        val aspects = listOf(
            Aspect(AstrologyBody.Sun, AstrologyBody.Moon, AspectType.Sextile, 1.8),
            Aspect(AstrologyBody.Venus, AstrologyBody.Mars, AspectType.Trine, 0.6),
            Aspect(AstrologyBody.Mercury, AstrologyBody.Saturn, AspectType.Square, 2.1),
            Aspect(AstrologyBody.Jupiter, AstrologyBody.Neptune, AspectType.Opposition, 1.2, premiumLocked = true),
            Aspect(AstrologyBody.Saturn, AstrologyBody.Pluto, AspectType.Sextile, 0.9),
        )
        val houses = (1..12).map { house ->
            HouseCusp(
                houseNumber = house,
                sign = ZodiacSign.entries[(house - 1) % ZodiacSign.entries.size],
                degree = (house * 2.5) % 30.0,
            )
        }
        return ChartSnapshot(
            profileId = profileId,
            calculatedAt = now,
            houseSystem = houseSystem,
            precision = ChartPrecision.Exact,
            placements = placements,
            aspects = aspects,
            houses = houses,
            summary = ChartSummary(
                headline = "A chart that favors decisive action and clear timing",
                description = "The profile balances strong initiative with enough emotional sensitivity to keep relationships readable.",
            ),
        )
    }
}
