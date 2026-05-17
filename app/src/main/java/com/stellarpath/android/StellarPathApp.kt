package com.stellarpath.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.stellarpath.android.navigation.AppNavGraph
import com.stellarpath.android.ui.theme.StellarPathTheme
import com.stellarpath.android.ui.state.LocalStellarPathAppState
import com.stellarpath.android.ui.state.rememberStellarPathAppState
import com.stellarpath.android.model.ThemeMode

@Composable
fun StellarPathApp() {
    val appState = rememberStellarPathAppState()
    val darkTheme = when (appState.settings.themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    StellarPathTheme(
        darkTheme = darkTheme,
        dynamicColor = false,
    ) {
        CompositionLocalProvider(LocalStellarPathAppState provides appState) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                AppNavGraph()
            }
        }
    }
}
