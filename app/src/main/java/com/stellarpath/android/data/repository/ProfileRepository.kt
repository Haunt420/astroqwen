package com.stellarpath.android.data.repository

import com.stellarpath.android.model.BirthProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profiles: Flow<List<BirthProfile>>

    suspend fun getProfile(profileId: String): BirthProfile?

    suspend fun upsertProfile(profile: BirthProfile)

    suspend fun deleteProfile(profileId: String)

    suspend fun seedIfEmpty(seedProfiles: List<BirthProfile>)
}
