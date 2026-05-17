package com.stellarpath.android.model

import java.time.LocalDate

// BirthLocation is defined in ProfileModels.kt — do NOT redeclare it here.

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
    Other,
}
