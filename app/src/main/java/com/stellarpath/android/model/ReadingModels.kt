package com.stellarpath.android.model

import androidx.compose.runtime.Immutable
import java.time.Instant
import java.time.LocalDate

enum class TransitMarkerType {
    RetrogradeStation,
    RetrogradeEnd,
    Eclipse,
    Solstice,
    Equinox,
    PlanetBirthday,
    TransitPeak,
}

@Immutable
data class TransitMarker(
    val id: String,
    val date: LocalDate,
    val title: String,
    val type: TransitMarkerType,
    val summary: String,
)

@Immutable
data class TransitWindow(
    val profileId: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val activeDate: LocalDate,
    val markers: List<TransitMarker> = emptyList(),
)

@Immutable
data class InterpretationBlock(
    val id: String,
    val title: String,
    val body: String,
    val relatedBodies: List<AstrologyBody> = emptyList(),
    val relatedAspects: List<AspectType> = emptyList(),
)

@Immutable
data class DailyReading(
    val profileId: String,
    val date: LocalDate,
    val generatedAt: Instant,
    val headline: String,
    val summary: String,
    val blocks: List<InterpretationBlock> = emptyList(),
)

enum class SpecialEventType {
    Eclipse,
    Solstice,
    Equinox,
    Retrograde,
    PlanetBirthday,
    LunarPhase,
}

@Immutable
data class SpecialEvent(
    val id: String,
    val title: String,
    val date: LocalDate,
    val type: SpecialEventType,
    val summary: String,
)
