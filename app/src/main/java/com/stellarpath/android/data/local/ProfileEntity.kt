package com.stellarpath.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stellarpath.android.model.BirthLocation
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.BirthTimePrecision
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.ProfileType
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val profileType: String,
    val birthDate: String,
    val birthTime: String?,
    val birthTimePrecision: String,
    val city: String,
    val region: String?,
    val countryCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timezoneId: String?,
    val houseSystem: String,
    val notes: String?,
)

fun ProfileEntity.toDomain(): BirthProfile = BirthProfile(
    id = id,
    displayName = displayName,
    profileType = enumValueOrDefault(profileType, ProfileType.Other),
    birthDate = LocalDate.parse(birthDate),
    birthTime = birthTime?.let(LocalTime::parse),
    birthTimePrecision = enumValueOrDefault(birthTimePrecision, BirthTimePrecision.Unknown),
    location = BirthLocation(
        city = city,
        region = region,
        countryCode = countryCode,
        latitude = latitude,
        longitude = longitude,
        timezoneId = timezoneId,
    ),
    houseSystem = enumValueOrDefault(houseSystem, HouseSystem.Placidus),
    notes = notes,
)

fun BirthProfile.toEntity(): ProfileEntity = ProfileEntity(
    id = id,
    displayName = displayName,
    profileType = profileType.name,
    birthDate = birthDate.toString(),
    birthTime = birthTime?.toString(),
    birthTimePrecision = birthTimePrecision.name,
    city = location.city,
    region = location.region,
    countryCode = location.countryCode,
    latitude = location.latitude,
    longitude = location.longitude,
    timezoneId = location.timezoneId,
    houseSystem = houseSystem.name,
    notes = notes,
)

private inline fun <reified T : Enum<T>> enumValueOrDefault(value: String, fallback: T): T =
    runCatching { enumValueOf<T>(value) }.getOrDefault(fallback)
