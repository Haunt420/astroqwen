package com.stellarpath.android.data.ephemeris

import android.content.Context
import com.stellarpath.android.model.Aspect
import com.stellarpath.android.model.AspectType
import com.stellarpath.android.model.AstrologyBody
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.ChartPlacement
import com.stellarpath.android.model.ChartPrecision
import com.stellarpath.android.model.ChartSnapshot
import com.stellarpath.android.model.ChartSummary
import com.stellarpath.android.model.CompatibilityDimension
import com.stellarpath.android.model.CompatibilityDimensionType
import com.stellarpath.android.model.CompatibilityReport
import com.stellarpath.android.model.DailyReading
import com.stellarpath.android.model.HouseCusp
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.InterpretationBlock
import com.stellarpath.android.model.ZodiacSign
import java.io.File
import java.io.FileOutputStream
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt
import swisseph.SwissEph
import swisseph.data.SweConst

class SwissEphemerisAstroCalculationEngine(
    context: Context,
    private val clock: Clock = Clock.systemUTC(),
) : AstroCalculationEngine {
    private val appContext = context.applicationContext
    private val engineLock = Any()

    private val natalCache = ConcurrentHashMap<String, ChartSnapshot>()
    private val transitCache = ConcurrentHashMap<String, ChartSnapshot>()
    private val readingCache = ConcurrentHashMap<String, DailyReading>()
    private val compatibilityCache = ConcurrentHashMap<String, CompatibilityReport>()

    private val swissEph: SwissEph by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val ephPath = SwissEphemerisAssetInstaller(appContext).install()
        SwissEph(ephPath.absolutePath)
    }

    override fun warmUp() {
        ensureReady()
    }

    override fun natalChart(profile: BirthProfile): ChartSnapshot =
        natalCache.computeIfAbsent(profile.cacheKey()) { calculateNatalChart(profile) }

    override fun transitChart(profile: BirthProfile, date: LocalDate): ChartSnapshot =
        transitCache.computeIfAbsent(transitCacheKey(profile, date)) { calculateTransitChart(profile, date) }

    override fun dailyReading(profile: BirthProfile, date: LocalDate): DailyReading =
        readingCache.computeIfAbsent(readingCacheKey(profile, date)) { calculateDailyReading(profile, date) }

    override fun compatibilityReport(primary: BirthProfile, secondary: BirthProfile): CompatibilityReport =
        compatibilityCache.computeIfAbsent(compatibilityCacheKey(primary, secondary)) {
            calculateCompatibilityReport(primary, secondary)
        }

    private fun calculateNatalChart(profile: BirthProfile): ChartSnapshot {
        val birthInstant = profileBirthInstant(profile)
        val birthJulianDay = julianDay(birthInstant)
        val chartPrecision = profileChartPrecision(profile)
        val includeHouses = chartPrecision != ChartPrecision.UnknownTime &&
            profile.location.latitude != null &&
            profile.location.longitude != null

        val positions = natalBodyPositions(birthJulianDay)
        val houseData = if (includeHouses) calculateHouseData(profile, birthJulianDay) else null

        val placements = buildNatalPlacements(
            positions = positions,
            houseData = houseData,
            includeAngles = includeHouses,
        )
        val aspects = calculateAspects(
            sourcePlacements = placements.filterNot { it.body.isAngle() },
            targetPlacements = placements.filterNot { it.body.isAngle() },
            maxCount = 10,
        )

        val summary = buildNatalSummary(profile, placements, aspects, chartPrecision)

        return ChartSnapshot(
            profileId = profile.id,
            calculatedAt = birthInstant,
            houseSystem = profile.houseSystem,
            precision = chartPrecision,
            placements = placements,
            aspects = aspects,
            houses = houseData?.cusps?.mapIndexed { index, cusp ->
                HouseCusp(
                    houseNumber = index + 1,
                    sign = zodiacSignFor(cusp),
                    degree = degreeInSign(cusp),
                )
            }.orEmpty(),
            summary = summary,
        )
    }

    private fun calculateTransitChart(profile: BirthProfile, date: LocalDate): ChartSnapshot {
        val natal = natalChart(profile)
        val transitInstant = profileDateInstant(profile, date, LocalTime.NOON)
        val transitJulianDay = julianDay(transitInstant)

        val transitPositions = transitBodyPositions(transitJulianDay)
        val transitPlacements = buildTransitPlacements(transitPositions)
        val transitAspects = calculateAspects(
            sourcePlacements = transitPlacements,
            targetPlacements = natal.placements.filterNot { it.body.isAngle() },
            maxCount = 12,
        )

        val summary = buildTransitSummary(profile, date, transitPlacements, transitAspects)

        return natal.copy(
            calculatedAt = transitInstant,
            summary = summary,
            transitPlacements = transitPlacements,
            transitAspects = transitAspects,
        )
    }

    private fun calculateDailyReading(profile: BirthProfile, date: LocalDate): DailyReading {
        val natal = natalChart(profile)
        val transit = transitChart(profile, date)
        val transitPlacements = transit.transitPlacements.orEmpty()
        val transitAspects = transit.transitAspects.orEmpty()
        val basePlacements = natal.placements.filterNot { it.body.isAngle() }

        val headlineAspect = transitAspects.firstOrNull()
        val headline = headlineAspect?.let {
            "${it.source.name} ${it.type.name.lowercase(Locale.US)} ${it.target.name}"
        } ?: dominantElement(basePlacements).let { element ->
            "${element.name} rhythm leads the day"
        }

        val sun = natalPlacement(natal.placements, AstrologyBody.Sun)
        val moon = natalPlacement(natal.placements, AstrologyBody.Moon)
        val mercury = natalPlacement(natal.placements, AstrologyBody.Mercury)
        val venus = natalPlacement(natal.placements, AstrologyBody.Venus)
        val mars = natalPlacement(natal.placements, AstrologyBody.Mars)
        val saturn = natalPlacement(natal.placements, AstrologyBody.Saturn)

        val dominantElement = dominantElement(basePlacements)
        val retrogradeBody = transitPlacements
            .firstOrNull { it.body in retrogradeSensitiveBodies() && transitBodySpeed(it.body, transit).let { speed -> speed < 0.0 } }
            ?.body

        val summary = buildString {
            append("The chart leans ${dominantElement.name.lowercase(Locale.US)} today.")
            append(" ")
            append("${sun.sign.name} Sun and ${moon.sign.name} Moon keep the focus readable.")
            headlineAspect?.let {
                append(" ")
                append("${it.source.name} ${it.type.name.lowercase(Locale.US)} ${it.target.name} is the strongest active transit.")
            }
            retrogradeBody?.let {
                append(" ")
                append("${it.name} is retrograde, so verification matters more than speed.")
            }
        }

        val blocks = listOf(
            InterpretationBlock(
                id = "${profile.id}-focus",
                title = "Primary focus",
                body = "Lean into the ${dominantElement.name.lowercase(Locale.US)} part of the chart. ${sun.sign.name} Sun and ${moon.sign.name} Moon describe the day’s baseline tone.",
                relatedBodies = listOf(AstrologyBody.Sun, AstrologyBody.Moon),
            ),
            InterpretationBlock(
                id = "${profile.id}-pace",
                title = "Best pacing",
                body = if (transitAspects.any { it.source == AstrologyBody.Mercury || it.target == AstrologyBody.Mercury || it.source == AstrologyBody.Saturn || it.target == AstrologyBody.Saturn }) {
                    "Mercury and Saturn are active in the transit layer, so slower sequencing and explicit checkpoints will work better than improvisation."
                } else {
                    "Mercury in ${mercury.sign.name} and Saturn in ${saturn.sign.name} suggest steady pacing and a single-threaded plan."
                },
                relatedBodies = listOf(AstrologyBody.Mercury, AstrologyBody.Saturn),
                relatedAspects = listOf(AspectType.Conjunction, AspectType.Square, AspectType.Trine, AspectType.Sextile),
            ),
            InterpretationBlock(
                id = "${profile.id}-social",
                title = "Social tone",
                body = "Venus in ${venus.sign.name} and Mars in ${mars.sign.name} describe how the chart handles warmth, attraction, and momentum.",
                relatedBodies = listOf(AstrologyBody.Venus, AstrologyBody.Mars),
            ),
        )

        return DailyReading(
            profileId = profile.id,
            date = date,
            generatedAt = Instant.now(clock),
            headline = headline,
            summary = summary,
            blocks = blocks,
        )
    }

    private fun calculateCompatibilityReport(primary: BirthProfile, secondary: BirthProfile): CompatibilityReport {
        val primaryChart = natalChart(primary)
        val secondaryChart = natalChart(secondary)
        val primaryPlacements = primaryChart.placements.filterNot { it.body.isAngle() }
        val secondaryPlacements = secondaryChart.placements.filterNot { it.body.isAngle() }
        val synastry = calculateAspects(
            sourcePlacements = primaryPlacements,
            targetPlacements = secondaryPlacements,
            maxCount = 40,
        )

        val primaryElements = elementDistribution(primaryPlacements)
        val secondaryElements = elementDistribution(secondaryPlacements)
        val elementScore = scoreElementAffinity(primaryElements, secondaryElements)

        val supportive = synastry.filter { it.type in supportiveAspectTypes() }
        val challenging = synastry.filter { it.type in challengingAspectTypes() }
        val harmonyScore = scoreHarmony(supportive, challenging)
        val passionScore = scorePassion(primaryPlacements, secondaryPlacements, synastry)

        val primaryDominant = dominantElement(primaryPlacements)
        val secondaryDominant = dominantElement(secondaryPlacements)
        val strongestSynastry = synastry.firstOrNull()
        val strongestVenusMars = synastry.firstOrNull {
            (it.source == AstrologyBody.Venus || it.source == AstrologyBody.Mars ||
                it.target == AstrologyBody.Venus || it.target == AstrologyBody.Mars)
        }

        val elementSummary = CompatibilityDimension(
            type = CompatibilityDimensionType.ElementMatch,
            label = "Matching elements",
            score = elementScore,
            summary = "${primary.displayName} leans ${primaryDominant.name.lowercase(Locale.US)} while ${secondary.displayName} leans ${secondaryDominant.name.lowercase(Locale.US)}.",
            details = buildString {
                append("Element balance across the charts points to ")
                append(elementCompatibilityDescription(primaryDominant, secondaryDominant))
                append(". ")
                append("The blend is scored from the actual planetary spread, not a canned lookup.")
            },
        )

        val harmonyDimension = CompatibilityDimension(
            type = CompatibilityDimensionType.Harmony,
            label = "Harmony",
            score = harmonyScore,
            summary = if (supportive.isNotEmpty()) {
                val best = supportive.first()
                "The strongest supportive line is ${best.source.name} ${best.type.name.lowercase(Locale.US)} ${best.target.name}."
            } else {
                "The charts have more tension than easy flow, so clear expectations matter."
            },
            details = buildString {
                append("Supportive contacts: ${supportive.size}. ")
                append("Challenging contacts: ${challenging.size}. ")
                strongestSynastry?.let {
                    append("The tightest synastry aspect is ${synastryAspectLabel(it)}.")
                } ?: append("No tight synastry aspects were found within the default orb limits.")
            },
        )

        val passionDimension = CompatibilityDimension(
            type = CompatibilityDimensionType.Passion,
            label = "Passion",
            score = passionScore,
            summary = strongestVenusMars?.let {
                "Venus and Mars are wired through ${it.type.name.lowercase(Locale.US)}."
            } ?: "The attraction layer is steadier than explosive, but still active.",
            details = buildString {
                append("The passion score comes from Venus/Mars contacts, fire-element overlap, and the overall temperature of the charts. ")
                strongestVenusMars?.let {
                    append("The clearest attraction marker is ${synastryAspectLabel(it)}.")
                } ?: append("No standout Venus/Mars synastry contact crossed the orb threshold.")
            },
        )

        val overallSummary = buildString {
            append(primary.displayName)
            append(" and ")
            append(secondary.displayName)
            append(" show ")
            append(if (harmonyScore >= 70) "good flow" else "mixed flow")
            append(", with ")
            append(if (passionScore >= 70) "strong attraction" else "moderate attraction")
            append(" and an element match score of ")
            append(elementScore)
            append("%.")
        }

        return CompatibilityReport(
            primaryProfileId = primary.id,
            secondaryProfileId = secondary.id,
            generatedAt = Instant.now(clock),
            elementMatch = elementSummary,
            harmony = harmonyDimension,
            passion = passionDimension,
            overallSummary = overallSummary,
        )
    }

    private fun calculateAspects(
        sourcePlacements: List<ChartPlacement>,
        targetPlacements: List<ChartPlacement>,
        maxCount: Int,
    ): List<Aspect> {
        val samePool = sourcePlacements === targetPlacements
        val candidates = mutableListOf<AspectCandidate>()

        sourcePlacements.forEachIndexed { sourceIndex, source ->
            val targetStart = if (samePool) sourceIndex + 1 else 0
            for (targetIndex in targetStart until targetPlacements.size) {
                val target = targetPlacements[targetIndex]
                if (source.body.isAngle() || target.body.isAngle()) continue

                val aspect = matchAspect(source, target) ?: continue
                candidates += AspectCandidate(
                    aspect = aspect,
                    priority = aspectPriority(aspect.type),
                    sourceOrdinal = source.body.ordinal,
                    targetOrdinal = target.body.ordinal,
                )
            }
        }

        return candidates
            .sortedWith(
                compareBy<AspectCandidate> { it.priority }
                    .thenBy { it.aspect.orb }
                    .thenBy { it.sourceOrdinal }
                    .thenBy { it.targetOrdinal }
            )
            .take(maxCount)
            .map { it.aspect }
    }

    private fun calculateHouseData(profile: BirthProfile, julianDay: Double): HouseData? {
        val latitude = profile.location.latitude ?: return null
        val longitude = profile.location.longitude ?: return null
        val houseSystem = houseSystemCode(profile.houseSystem)

        val cusp = DoubleArray(13)
        val ascmc = DoubleArray(10)

        val result = withSwissEph {
            swe_houses(
                julianDay,
                0,
                latitude,
                longitude,
                houseSystem,
                cusp,
                ascmc,
            )
        }

        if (result < 0) {
            throw IllegalStateException("Swiss Ephemeris house calculation failed for ${profile.displayName}")
        }

        return HouseData(
            cusps = (1..12).map { cusp[it] },
            ascendant = ascmc[SweConst.SE_ASC],
            midheaven = ascmc[SweConst.SE_MC],
        )
    }

    private fun buildNatalPlacements(
        positions: Map<AstrologyBody, Double>,
        houseData: HouseData?,
        includeAngles: Boolean,
    ): List<ChartPlacement> {
        val placements = mutableListOf<ChartPlacement>()
        natalBodiesInOrder().forEach { body ->
            val longitude = positions[body] ?: return@forEach
            placements += ChartPlacement(
                body = body,
                sign = zodiacSignFor(longitude),
                degreeInSign = degreeInSign(longitude),
                house = houseData?.let { houseForLongitude(longitude, it.cusps) },
            )
        }

        if (includeAngles && houseData != null) {
            placements += ChartPlacement(
                body = AstrologyBody.Ascendant,
                sign = zodiacSignFor(houseData.ascendant),
                degreeInSign = degreeInSign(houseData.ascendant),
                house = 1,
            )
            placements += ChartPlacement(
                body = AstrologyBody.Midheaven,
                sign = zodiacSignFor(houseData.midheaven),
                degreeInSign = degreeInSign(houseData.midheaven),
                house = 10,
            )
        }

        return placements
    }

    private fun buildTransitPlacements(positions: Map<AstrologyBody, Double>): List<ChartPlacement> =
        transitBodiesInOrder().mapNotNull { body ->
            positions[body]?.let { longitude ->
                ChartPlacement(
                    body = body,
                    sign = zodiacSignFor(longitude),
                    degreeInSign = degreeInSign(longitude),
                )
            }
        }

    private fun buildNatalSummary(
        profile: BirthProfile,
        placements: List<ChartPlacement>,
        aspects: List<Aspect>,
        precision: ChartPrecision,
    ): ChartSummary {
        val dominantElement = dominantElement(placements.filterNot { it.body.isAngle() })
        val sun = natalPlacement(placements, AstrologyBody.Sun)
        val moon = natalPlacement(placements, AstrologyBody.Moon)
        val strongestAspect = aspects.firstOrNull()

        val headline = when (dominantElement) {
            Element.Fire -> "Fire drives this chart"
            Element.Earth -> "Earth grounds this chart"
            Element.Air -> "Air keeps this chart moving"
            Element.Water -> "Water colors this chart"
        }

        val description = buildString {
            append(profile.displayName)
            append(" leans ")
            append(dominantElement.name.lowercase(Locale.US))
            append(", with ")
            append(sun.sign.name)
            append(" Sun and ")
            append(moon.sign.name)
            append(" Moon setting the tone.")
            strongestAspect?.let {
                append(" ")
                append("The clearest aspect is ")
                append(synastryAspectLabel(it))
                append(".")
            }
            if (precision == ChartPrecision.UnknownTime) {
                append(" Birth time is unavailable, so house placement and angle emphasis stay approximate.")
            }
        }

        return ChartSummary(headline = headline, description = description)
    }

    private fun buildTransitSummary(
        profile: BirthProfile,
        date: LocalDate,
        placements: List<ChartPlacement>,
        aspects: List<Aspect>,
    ): ChartSummary {
        val dominantElement = dominantElement(placements)
        val strongestAspect = aspects.firstOrNull()
        val headline = strongestAspect?.let {
            "${it.source.name} ${it.type.name.lowercase(Locale.US)} ${it.target.name}"
        } ?: "${dominantElement.name} transit"

        val description = buildString {
            append("Transit weather for ")
            append(profile.displayName)
            append(" on ")
            append(date)
            append(" leans ")
            append(dominantElement.name.lowercase(Locale.US))
            append(".")
            strongestAspect?.let {
                append(" ")
                append("The strongest active transit is ")
                append(synastryAspectLabel(it))
                append(".")
            }
        }

        return ChartSummary(headline = headline, description = description)
    }

    private fun profileChartPrecision(profile: BirthProfile): ChartPrecision = when {
        profile.birthTime == null || profile.birthTimePrecision == com.stellarpath.android.model.BirthTimePrecision.Unknown ->
            ChartPrecision.UnknownTime
        profile.birthTimePrecision == com.stellarpath.android.model.BirthTimePrecision.Approximate ->
            ChartPrecision.Estimated
        else -> ChartPrecision.Exact
    }

    private fun profileBirthInstant(profile: BirthProfile): Instant {
        val birthTime = when (profile.birthTimePrecision) {
            com.stellarpath.android.model.BirthTimePrecision.Exact,
            com.stellarpath.android.model.BirthTimePrecision.Approximate,
            -> profile.birthTime ?: LocalTime.NOON

            com.stellarpath.android.model.BirthTimePrecision.Unknown -> LocalTime.NOON
        }
        return profileDateInstant(profile, profile.birthDate, birthTime)
    }

    private fun julianDay(instant: Instant): Double =
        2440587.5 + instant.epochSecond.toDouble() / 86400.0 + instant.nano.toDouble() / 86_400_000_000_000.0

    private fun natalBodyPositions(julianDay: Double): Map<AstrologyBody, Double> {
        val result = mutableMapOf<AstrologyBody, Double>()
        coreBodyMappings().forEach { (body, planet) ->
            result[body] = calcLongitude(julianDay, planet)
        }
        result[AstrologyBody.SouthNode] = normalizeLongitude((result[AstrologyBody.NorthNode] ?: 0.0) + 180.0)
        return result
    }

    private fun transitBodyPositions(julianDay: Double): Map<AstrologyBody, Double> = natalBodyPositions(julianDay)

    private fun calcLongitude(julianDay: Double, planetId: Int): Double {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val result = withSwissEph {
            swe_calc_ut(
                julianDay,
                planetId,
                SweConst.SEFLG_SWIEPH or SweConst.SEFLG_SPEED,
                xx,
                serr,
            )
        }
        if (result < 0) {
            throw IllegalStateException(serr.toString().ifBlank { "Swiss Ephemeris calculation failed." })
        }
        return normalizeLongitude(xx[0])
    }

    private fun houseForLongitude(longitude: Double, cusps: List<Double>): Int? {
        if (cusps.size != 12) return null
        val normalized = normalizeLongitude(longitude)
        cusps.forEachIndexed { index, current ->
            val next = cusps[(index + 1) % cusps.size]
            val inHouse = if (current <= next) {
                normalized >= current && normalized < next
            } else {
                normalized >= current || normalized < next
            }
            if (inHouse) {
                return index + 1
            }
        }
        return null
    }

    private fun zodiacSignFor(longitude: Double): ZodiacSign {
        val normalized = normalizeLongitude(longitude)
        val index = floor(normalized / 30.0).toInt().coerceIn(0, ZodiacSign.entries.lastIndex)
        return ZodiacSign.entries[index]
    }

    private fun degreeInSign(longitude: Double): Double {
        val normalized = normalizeLongitude(longitude)
        return normalized % 30.0
    }

    private fun dominantElement(placements: List<ChartPlacement>): Element {
        val counts = elementDistribution(placements)
        return counts.maxByOrNull { it.value }?.key ?: Element.Fire
    }

    private fun elementDistribution(placements: List<ChartPlacement>): Map<Element, Double> {
        val counts = mutableMapOf<Element, Double>()
        placements.forEach { placement ->
            if (placement.body.isAngle()) return@forEach
            val weight = placementWeight(placement.body)
            val element = elementFor(placement.sign)
            counts[element] = counts.getOrDefault(element, 0.0) + weight
        }
        return counts
    }

    private fun scoreElementAffinity(
        primary: Map<Element, Double>,
        secondary: Map<Element, Double>,
    ): Int {
        val primaryTotal = primary.values.sum().takeIf { it > 0.0 } ?: return 50
        val secondaryTotal = secondary.values.sum().takeIf { it > 0.0 } ?: return 50
        var affinity = 0.0

        primary.forEach { (primaryElement, primaryWeight) ->
            secondary.forEach { (secondaryElement, secondaryWeight) ->
                affinity += primaryWeight * secondaryWeight * elementCompatibility(primaryElement, secondaryElement)
            }
        }

        val normalized = affinity / (primaryTotal * secondaryTotal)
        return (normalized * 100.0).roundToInt().coerceIn(0, 100)
    }

    private fun scoreHarmony(supportive: List<Aspect>, challenging: List<Aspect>): Int {
        var score = 50.0
        supportive.forEach {
            score += aspectHarmonyWeight(it.type) * aspectCloseness(it.orb)
        }
        challenging.forEach {
            score -= aspectChallengeWeight(it.type) * aspectCloseness(it.orb)
        }
        return score.roundToInt().coerceIn(0, 100)
    }

    private fun scorePassion(
        primaryPlacements: List<ChartPlacement>,
        secondaryPlacements: List<ChartPlacement>,
        synastry: List<Aspect>,
    ): Int {
        var score = 40.0
        val firePrimary = elementDistribution(primaryPlacements)[Element.Fire] ?: 0.0
        val fireSecondary = elementDistribution(secondaryPlacements)[Element.Fire] ?: 0.0
        score += min(15.0, (firePrimary + fireSecondary) * 2.0)

        synastry.forEach {
            if (it.source in passionBodies() || it.target in passionBodies()) {
                score += when (it.type) {
                    AspectType.Conjunction -> 12.0 * aspectCloseness(it.orb)
                    AspectType.Trine -> 10.0 * aspectCloseness(it.orb)
                    AspectType.Sextile -> 8.0 * aspectCloseness(it.orb)
                    AspectType.Square -> 7.0 * aspectCloseness(it.orb)
                    AspectType.Opposition -> 6.0 * aspectCloseness(it.orb)
                    else -> 4.0 * aspectCloseness(it.orb)
                }
            }
        }

        return score.roundToInt().coerceIn(0, 100)
    }

    private fun aspectCloseness(orb: Double): Double = (1.0 - (orb / 8.0)).coerceIn(0.0, 1.0)

    private fun aspectHarmonyWeight(type: AspectType): Double = when (type) {
        AspectType.Conjunction -> 1.1
        AspectType.Trine -> 1.0
        AspectType.Sextile -> 0.8
        AspectType.Quintile -> 0.5
        AspectType.Quincunx -> 0.35
        AspectType.Semisextile -> 0.25
        AspectType.Opposition -> 0.15
        AspectType.Square -> 0.05
    }

    private fun aspectChallengeWeight(type: AspectType): Double = when (type) {
        AspectType.Square -> 1.0
        AspectType.Opposition -> 0.9
        AspectType.Quincunx -> 0.55
        AspectType.Semisextile -> 0.25
        AspectType.Quintile -> 0.15
        AspectType.Conjunction -> 0.20
        AspectType.Trine -> 0.10
        AspectType.Sextile -> 0.05
    }

    private fun elementCompatibility(first: Element, second: Element): Double = when {
        first == second -> 1.0
        (first == Element.Fire && second == Element.Air) || (first == Element.Air && second == Element.Fire) -> 0.85
        (first == Element.Earth && second == Element.Water) || (first == Element.Water && second == Element.Earth) -> 0.85
        (first == Element.Fire && second == Element.Water) || (first == Element.Water && second == Element.Fire) -> 0.45
        (first == Element.Earth && second == Element.Air) || (first == Element.Air && second == Element.Earth) -> 0.45
        else -> 0.60
    }

    private fun elementCompatibilityDescription(first: Element, second: Element): String = when {
        first == second -> "${first.name.lowercase(Locale.US)} resonance"
        (first == Element.Fire && second == Element.Air) || (first == Element.Air && second == Element.Fire) ->
            "a fire-and-air pairing that keeps things active and responsive"
        (first == Element.Earth && second == Element.Water) || (first == Element.Water && second == Element.Earth) ->
            "an earth-and-water pairing that tends to be steady and emotionally fluent"
        else -> "a mixed-element blend that needs more explicit coordination"
    }

    private fun matchAspect(source: ChartPlacement, target: ChartPlacement): Aspect? {
        val distance = angularDistance(source.longitude(), target.longitude())
        val best = aspectPatterns()
            .map { pattern ->
                val orb = abs(distance - pattern.angle)
                AspectMatchCandidate(pattern.type, orb, pattern.orbLimit)
            }
            .filter { it.orb <= it.orbLimit }
            .minByOrNull { it.orb }
            ?: return null

        return Aspect(
            source = source.body,
            target = target.body,
            type = best.type,
            orb = best.orb,
            exact = best.orb <= 0.5,
        )
    }

    private fun synastryAspectLabel(aspect: Aspect): String =
        "${aspect.source.name} ${aspect.type.name.lowercase(Locale.US)} ${aspect.target.name} (${formatOrb(aspect.orb)})"

    private fun formatOrb(value: Double): String = String.format(Locale.US, "%.1f°", value)

    private fun angularDistance(first: Double, second: Double): Double {
        val delta = abs(normalizeLongitude(first - second))
        return min(delta, 360.0 - delta)
    }

    private fun normalizeLongitude(value: Double): Double {
        val normalized = value % 360.0
        return if (normalized < 0.0) normalized + 360.0 else normalized
    }

    private fun coreBodyMappings(): Map<AstrologyBody, Int> = mapOf(
        AstrologyBody.Sun to SweConst.SE_SUN,
        AstrologyBody.Moon to SweConst.SE_MOON,
        AstrologyBody.Mercury to SweConst.SE_MERCURY,
        AstrologyBody.Venus to SweConst.SE_VENUS,
        AstrologyBody.Mars to SweConst.SE_MARS,
        AstrologyBody.Jupiter to SweConst.SE_JUPITER,
        AstrologyBody.Saturn to SweConst.SE_SATURN,
        AstrologyBody.Uranus to SweConst.SE_URANUS,
        AstrologyBody.Neptune to SweConst.SE_NEPTUNE,
        AstrologyBody.Pluto to SweConst.SE_PLUTO,
        AstrologyBody.NorthNode to SweConst.SE_TRUE_NODE,
        AstrologyBody.Chiron to SweConst.SE_CHIRON,
        AstrologyBody.Lilith to SweConst.SE_MEAN_APOG,
    )

    private fun natalBodiesInOrder(): List<AstrologyBody> = listOf(
        AstrologyBody.Sun,
        AstrologyBody.Moon,
        AstrologyBody.Mercury,
        AstrologyBody.Venus,
        AstrologyBody.Mars,
        AstrologyBody.Jupiter,
        AstrologyBody.Saturn,
        AstrologyBody.Uranus,
        AstrologyBody.Neptune,
        AstrologyBody.Pluto,
        AstrologyBody.NorthNode,
        AstrologyBody.SouthNode,
        AstrologyBody.Chiron,
        AstrologyBody.Lilith,
    )

    private fun transitBodiesInOrder(): List<AstrologyBody> = listOf(
        AstrologyBody.Sun,
        AstrologyBody.Moon,
        AstrologyBody.Mercury,
        AstrologyBody.Venus,
        AstrologyBody.Mars,
        AstrologyBody.Jupiter,
        AstrologyBody.Saturn,
        AstrologyBody.Uranus,
        AstrologyBody.Neptune,
        AstrologyBody.Pluto,
        AstrologyBody.NorthNode,
        AstrologyBody.SouthNode,
        AstrologyBody.Chiron,
        AstrologyBody.Lilith,
    )

    private fun retrogradeSensitiveBodies(): Set<AstrologyBody> = setOf(
        AstrologyBody.Mercury,
        AstrologyBody.Venus,
        AstrologyBody.Mars,
        AstrologyBody.Jupiter,
        AstrologyBody.Saturn,
        AstrologyBody.Uranus,
        AstrologyBody.Neptune,
        AstrologyBody.Pluto,
    )

    private fun passionBodies(): Set<AstrologyBody> = setOf(
        AstrologyBody.Venus,
        AstrologyBody.Mars,
        AstrologyBody.Moon,
        AstrologyBody.Sun,
    )

    private fun supportiveAspectTypes(): Set<AspectType> = setOf(
        AspectType.Conjunction,
        AspectType.Trine,
        AspectType.Sextile,
        AspectType.Quintile,
    )

    private fun challengingAspectTypes(): Set<AspectType> = setOf(
        AspectType.Square,
        AspectType.Opposition,
        AspectType.Quincunx,
    )

    private fun placementWeight(body: AstrologyBody): Double = when (body) {
        AstrologyBody.Sun, AstrologyBody.Moon -> 2.0
        AstrologyBody.Mercury, AstrologyBody.Venus, AstrologyBody.Mars -> 1.3
        AstrologyBody.Jupiter, AstrologyBody.Saturn -> 1.1
        AstrologyBody.Uranus, AstrologyBody.Neptune, AstrologyBody.Pluto -> 0.9
        AstrologyBody.NorthNode, AstrologyBody.SouthNode, AstrologyBody.Chiron, AstrologyBody.Lilith -> 0.7
        AstrologyBody.Ascendant, AstrologyBody.Midheaven -> 0.8
    }

    private fun elementFor(sign: ZodiacSign): Element = when (sign) {
        ZodiacSign.Aries, ZodiacSign.Leo, ZodiacSign.Sagittarius -> Element.Fire
        ZodiacSign.Taurus, ZodiacSign.Virgo, ZodiacSign.Capricorn -> Element.Earth
        ZodiacSign.Gemini, ZodiacSign.Libra, ZodiacSign.Aquarius -> Element.Air
        ZodiacSign.Cancer, ZodiacSign.Scorpio, ZodiacSign.Pisces -> Element.Water
    }

    private fun houseSystemCode(system: HouseSystem): Int = when (system) {
        HouseSystem.Placidus -> SweConst.SE_HSYS_PLACIDUS
        HouseSystem.WholeSign -> SweConst.SE_HSYS_WHOLE_SIGN
        HouseSystem.Koch -> SweConst.SE_HSYS_KOCH
        HouseSystem.Equal -> SweConst.SE_HSYS_EQUAL
        HouseSystem.Campanus -> SweConst.SE_HSYS_CAMPANUS
        HouseSystem.Porphyry -> SweConst.SE_HSYS_PORPHYRIUS
        HouseSystem.Regiomontanus -> SweConst.SE_HSYS_REGIOMONTANUS
    }

    private fun natalPlacement(placements: List<ChartPlacement>, body: AstrologyBody): ChartPlacement =
        placements.firstOrNull { it.body == body } ?: placements.first()

    private fun withSwissEph(block: SwissEph.() -> Int): Int = synchronized(engineLock) {
        swissEph.block()
    }

    private fun ensureReady() {
        swissEph
    }

    private fun profileZoneId(profile: BirthProfile): ZoneId =
        profile.location.timezoneId?.let { runCatching { ZoneId.of(it) }.getOrNull() } ?: ZoneId.of("UTC")

    private fun profileDateInstant(profile: BirthProfile, date: LocalDate, time: LocalTime): Instant =
        ZonedDateTime.of(date, time, profileZoneId(profile)).toInstant()

    private fun transitBodySpeed(body: AstrologyBody, chart: ChartSnapshot): Double {
        val sourceInstant = chart.calculatedAt
        val julianDay = julianDay(sourceInstant)
        val planetId = coreBodyMappings()[body] ?: return 0.0
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val result = withSwissEph {
            swe_calc_ut(
                julianDay,
                planetId,
                SweConst.SEFLG_SWIEPH or SweConst.SEFLG_SPEED,
                xx,
                serr,
            )
        }
        return if (result < 0) 0.0 else xx[3]
    }

    private fun BirthProfile.cacheKey(): String = buildString {
        val profile = this@cacheKey
        append(profile.id)
        append('|')
        append(profile.displayName)
        append('|')
        append(profile.birthDate)
        append('|')
        append(profile.birthTime?.toString() ?: "no-time")
        append('|')
        append(profile.birthTimePrecision.name)
        append('|')
        append(profile.location.city)
        append('|')
        append(profile.location.region ?: "")
        append('|')
        append(profile.location.countryCode ?: "")
        append('|')
        append(profile.location.latitude ?: "")
        append('|')
        append(profile.location.longitude ?: "")
        append('|')
        append(profile.location.timezoneId ?: "")
        append('|')
        append(profile.houseSystem.name)
    }

    private fun transitCacheKey(profile: BirthProfile, date: LocalDate): String =
        "${profile.cacheKey()}|transit|$date"

    private fun readingCacheKey(profile: BirthProfile, date: LocalDate): String =
        "${profile.cacheKey()}|reading|$date"

    private fun compatibilityCacheKey(primary: BirthProfile, secondary: BirthProfile): String =
        "${primary.cacheKey()}|vs|${secondary.cacheKey()}"

    private enum class Element {
        Fire,
        Earth,
        Air,
        Water,
    }

    private data class HouseData(
        val cusps: List<Double>,
        val ascendant: Double,
        val midheaven: Double,
    )

    private data class AspectCandidate(
        val aspect: Aspect,
        val priority: Int,
        val sourceOrdinal: Int,
        val targetOrdinal: Int,
    )

    private data class AspectMatchCandidate(
        val type: AspectType,
        val orb: Double,
        val orbLimit: Double,
    )

    private data class AspectPattern(
        val type: AspectType,
        val angle: Double,
        val orbLimit: Double,
    )

    private fun aspectPatterns(): List<AspectPattern> = listOf(
        AspectPattern(AspectType.Conjunction, 0.0, 8.0),
        AspectPattern(AspectType.Opposition, 180.0, 8.0),
        AspectPattern(AspectType.Trine, 120.0, 6.0),
        AspectPattern(AspectType.Square, 90.0, 6.0),
        AspectPattern(AspectType.Sextile, 60.0, 5.0),
        AspectPattern(AspectType.Quincunx, 150.0, 3.5),
        AspectPattern(AspectType.Semisextile, 30.0, 3.0),
        AspectPattern(AspectType.Quintile, 72.0, 2.5),
    )

    private fun aspectPriority(type: AspectType): Int = when (type) {
        AspectType.Conjunction, AspectType.Opposition, AspectType.Trine, AspectType.Square, AspectType.Sextile -> 0
        AspectType.Quincunx, AspectType.Semisextile, AspectType.Quintile -> 1
    }

    private fun ChartPlacement.longitude(): Double =
        (ZodiacSign.entries.indexOf(sign).coerceAtLeast(0) * 30.0) + degreeInSign

    private fun AstrologyBody.isAngle(): Boolean =
        this == AstrologyBody.Ascendant || this == AstrologyBody.Midheaven
}

private class SwissEphemerisAssetInstaller(
    private val context: Context,
) {
    fun install(): File {
        val targetRoot = File(context.filesDir, "swisseph/ephem")
        val readyMarker = File(targetRoot, ".ready")
        val sentinelFiles = listOf(
            File(targetRoot, "sepl_00.se1"),
            File(targetRoot, "semo_00.se1"),
            File(targetRoot, "swe_deltat.txt"),
        )

        if (readyMarker.exists() && sentinelFiles.all { it.exists() }) {
            return targetRoot
        }

        if (targetRoot.exists()) {
            targetRoot.deleteRecursively()
        }
        targetRoot.mkdirs()
        copyAssetTree("swisseph/ephem", targetRoot)
        readyMarker.writeText("ok")
        return targetRoot
    }

    private fun copyAssetTree(assetPath: String, destination: File) {
        val children = context.assets.list(assetPath).orEmpty()
        if (children.isEmpty()) {
            destination.parentFile?.mkdirs()
            context.assets.open(assetPath).use { input ->
                FileOutputStream(destination).use { output -> input.copyTo(output) }
            }
            return
        }

        destination.mkdirs()
        children.forEach { child ->
            copyAssetTree("$assetPath/$child", File(destination, child))
        }
    }
}
