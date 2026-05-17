package com.stellarpath.android.model

import androidx.compose.runtime.Immutable
import java.time.Instant

enum class ZodiacSign {
    Aries, Taurus, Gemini, Cancer, Leo, Virgo,
    Libra, Scorpio, Sagittarius, Capricorn, Aquarius, Pisces,
}

enum class AstrologyBody {
    Sun, Moon, Mercury, Venus, Mars, Jupiter, Saturn,
    Uranus, Neptune, Pluto, NorthNode, SouthNode,
    Chiron, Lilith, Ascendant, Midheaven,
}

enum class AspectType {
    Conjunction, Opposition, Trine, Square,
    Sextile, Quincunx, Semisextile, Quintile,
}

enum class ChartPrecision {
    Exact, Estimated, UnknownTime,
}

@Immutable
data class ChartPlacement(
    val body: AstrologyBody,
    val sign: ZodiacSign,
    val degreeInSign: Double,
    val house: Int? = null,
)

@Immutable
data class Aspect(
    val source: AstrologyBody,
    val target: AstrologyBody,
    val type: AspectType,
    val orb: Double,
    val exact: Boolean = false,
    val premiumLocked: Boolean = false,
)

@Immutable
data class HouseCusp(
    val houseNumber: Int,
    val sign: ZodiacSign,
    val degree: Double,
)

@Immutable
data class ChartSummary(
    val headline: String,
    val description: String,
)

@Immutable
data class ChartSnapshot(
    val profileId: String,
    val calculatedAt: Instant,
    val houseSystem: HouseSystem,
    val precision: ChartPrecision,
    val placements: List<ChartPlacement> = emptyList(),
    val aspects: List<Aspect> = emptyList(),
    val houses: List<HouseCusp> = emptyList(),
    val summary: ChartSummary? = null,
    // Transit overlay fields — null when this is a pure natal chart
    val transitPlacements: List<ChartPlacement>? = null,
    val transitAspects: List<Aspect>? = null,
)
