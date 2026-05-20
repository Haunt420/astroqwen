package com.stellarpath.android.data.repository

import android.content.Context
import com.stellarpath.android.data.ephemeris.AstroCalculationEngine
import com.stellarpath.android.data.ephemeris.SwissEphemerisAstroCalculationEngine
import com.stellarpath.android.data.local.StellarPathDatabase

class StellarPathAppContainer(
    context: Context,
) {
    private val appContext = context.applicationContext

    private val database: StellarPathDatabase by lazy {
        StellarPathDatabase.build(appContext)
    }

    val profileRepository: ProfileRepository by lazy {
        RoomProfileRepository(database.profileDao())
    }

    val settingsRepository: SettingsRepository by lazy {
        DataStoreSettingsRepository(appContext)
    }

    val astroEngine: AstroCalculationEngine by lazy {
        SwissEphemerisAstroCalculationEngine(appContext)
    }
}
