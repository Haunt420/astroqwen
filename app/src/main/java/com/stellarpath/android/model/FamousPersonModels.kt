package com.stellarpath.android.model

import java.time.LocalDate

data class FamousPerson(
    val id: String,
    val name: String,
    val birthDate: LocalDate,
    val birthTime: String? = null,
    val birthLocation: BirthLocation? = null,
    val profession: String,
    val category: PersonCategory,
    val notes: String = "",
)

enum class PersonCategory {
    Philosopher,
    Warrior,
    Musician,
    Artist,
    Author,
    Actor,
    Scientist,
    PoliticalLeader,
    ReligiousLeader,
    Athlete,
    BusinessLeader,
    Explorer,
    Other
}

data class BirthLocation(
    val city: String,
    val region: String? = null,
    val countryCode: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezoneId: String? = null,
)