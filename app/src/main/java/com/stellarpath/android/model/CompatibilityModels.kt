package com.stellarpath.android.model

import androidx.compose.runtime.Immutable
import java.time.Instant

enum class CompatibilityDimensionType {
    ElementMatch,
    Harmony,
    Passion,
}

@Immutable
data class CompatibilityDimension(
    val type: CompatibilityDimensionType,
    val label: String,
    val score: Int? = null,
    val summary: String,
    val details: String,
)

@Immutable
data class CompatibilityReport(
    val primaryProfileId: String,
    val secondaryProfileId: String,
    val generatedAt: Instant,
    val elementMatch: CompatibilityDimension,
    val harmony: CompatibilityDimension,
    val passion: CompatibilityDimension,
    val overallSummary: String,
    val premiumLocked: Boolean = false,
)
