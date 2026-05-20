package com.stellarpath.android.model

import androidx.compose.runtime.Immutable

@Immutable
data class ReferenceSection(
    val id: String,
    val title: String,
    val body: String,
    val relatedBodies: List<AstrologyBody> = emptyList(),
    val relatedAspects: List<AspectType> = emptyList(),
)

@Immutable
data class ReferenceTopic(
    val id: String,
    val title: String,
    val summary: String,
    val sections: List<ReferenceSection> = emptyList(),
)
