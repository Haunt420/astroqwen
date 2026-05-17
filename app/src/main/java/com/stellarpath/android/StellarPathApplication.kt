package com.stellarpath.android

import android.app.Application
import com.stellarpath.android.data.SampleAstroData
import com.stellarpath.android.data.repository.StellarPathAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StellarPathApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var appContainer: StellarPathAppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = StellarPathAppContainer(applicationContext)
        applicationScope.launch {
            appContainer.profileRepository.seedIfEmpty(SampleAstroData.profiles)
        }
    }
}
