package com.stellarpath.android.model

import java.time.Instant

enum class CompatibilityDimensionType {
    ElementMatch,
    Harmony,
    Passion,
    Communication,
    Longevity,
    Chemistry,
}

data class CompatibilityDimension(
    val type: CompatibilityDimensionType,
    val label: String,
    val score: Int? = null,
    val summary: String,
    val details: String,           // ← AppScreens.CompatibilityDimensionCard references this
)

data class CompatibilityReport(
    val primaryProfileId: String,
    val secondaryProfileId: String,
    val generatedAt: Instant,
    val elementMatch: CompatibilityDimension,
    val harmony: CompatibilityDimension,
    val passion: CompatibilityDimension,
    val overallSummary: String,
    val premiumLocked: Boolean = false,  // ← AppScreens.CompatibilityScreen references this
)
