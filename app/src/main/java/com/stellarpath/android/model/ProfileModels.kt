package com.stellarpath.android.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.LocalTime

@Immutable
data class BirthLocation(
    val city: String,
    val region: String? = null,
    val countryCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezoneId: String? = null,
)

enum class ProfileType {
    Self,
    Partner,
    Family,
    Friend,
    Crush,
    Other,
}

enum class BirthTimePrecision {
    Exact,
    Approximate,
    Unknown,
}

enum class HouseSystem {
    Placidus,
    WholeSign,
    Koch,
    Equal,
    Campanus,
    Porphyry,
    Regiomontanus,
}

@Immutable
data class BirthProfile(
    val id: String,
    val displayName: String,
    val profileType: ProfileType,
    val birthDate: LocalDate,
    val birthTime: LocalTime? = null,
    val birthTimePrecision: BirthTimePrecision = BirthTimePrecision.Unknown,
    val location: BirthLocation,
    val houseSystem: HouseSystem = HouseSystem.Placidus,
    val notes: String? = null,
)
