package com.stellarpath.android.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Midnight = Color(0xFF0B1020)
private val DeepSpace = Color(0xFF16223A)
private val Celestial = Color(0xFF234A7C)
private val Gold = Color(0xFFF2C14E)
private val Teal = Color(0xFF4FC3B3)
private val Rose = Color(0xFFE48FB1)
private val Cream = Color(0xFFF6F1E8)

val StellarDarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Midnight,
    secondary = Teal,
    onSecondary = Midnight,
    tertiary = Rose,
    onTertiary = Midnight,
    background = Midnight,
    onBackground = Cream,
    surface = DeepSpace,
    onSurface = Cream,
    surfaceVariant = Celestial,
    onSurfaceVariant = Cream,
)

val StellarLightColorScheme = lightColorScheme(
    primary = Celestial,
    onPrimary = Color.White,
    secondary = Teal,
    onSecondary = Midnight,
    tertiary = Rose,
    onTertiary = Midnight,
    background = Color(0xFFF8F6F2),
    onBackground = Midnight,
    surface = Color.White,
    onSurface = Midnight,
    surfaceVariant = Color(0xFFE4ECF7),
    onSurfaceVariant = Midnight,
)
