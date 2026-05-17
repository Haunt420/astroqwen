package com.stellarpath.android.data.repository

import com.stellarpath.android.data.local.ProfileDao
import com.stellarpath.android.data.local.toDomain
import com.stellarpath.android.data.local.toEntity
import com.stellarpath.android.model.BirthProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProfileRepository(
    private val profileDao: ProfileDao,
) : ProfileRepository {
    override val profiles: Flow<List<BirthProfile>> =
        profileDao.observeProfiles().map { items -> items.map { it.toDomain() } }

    override suspend fun getProfile(profileId: String): BirthProfile? =
        profileDao.getProfile(profileId)?.toDomain()

    override suspend fun upsertProfile(profile: BirthProfile) {
        profileDao.upsertProfile(profile.toEntity())
    }

    override suspend fun deleteProfile(profileId: String) {
        profileDao.deleteProfile(profileId)
    }

    override suspend fun seedIfEmpty(seedProfiles: List<BirthProfile>) {
        if (profileDao.profileCount() == 0) {
            seedProfiles.forEach { profile ->
                profileDao.upsertProfile(profile.toEntity())
            }
        }
    }
}
