package com.stellarpath.android.data.ephemeris

import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.ChartSnapshot
import com.stellarpath.android.model.CompatibilityReport
import com.stellarpath.android.model.DailyReading
import java.time.LocalDate

interface AstroCalculationEngine {
    fun warmUp()

    fun natalChart(profile: BirthProfile): ChartSnapshot

    fun transitChart(profile: BirthProfile, date: LocalDate): ChartSnapshot

    fun dailyReading(profile: BirthProfile, date: LocalDate): DailyReading

    fun compatibilityReport(primary: BirthProfile, secondary: BirthProfile): CompatibilityReport
}
