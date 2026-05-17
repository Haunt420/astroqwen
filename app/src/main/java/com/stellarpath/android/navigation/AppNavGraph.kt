package com.stellarpath.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stellarpath.android.ui.screens.ChartScreen
import com.stellarpath.android.ui.screens.CompatibilityScreen
import com.stellarpath.android.ui.screens.EventDetailScreen
import com.stellarpath.android.ui.screens.FamousPeopleScreen
import com.stellarpath.android.ui.screens.HomeScreen
import com.stellarpath.android.ui.screens.LibraryDetailScreen
import com.stellarpath.android.ui.screens.LibraryScreen
import com.stellarpath.android.ui.screens.OnboardingScreen
import com.stellarpath.android.ui.screens.PaywallScreen
import com.stellarpath.android.ui.screens.ProfileDetailScreen
import com.stellarpath.android.ui.screens.ProfileEditorScreen
import com.stellarpath.android.ui.screens.ProfilesScreen
import com.stellarpath.android.ui.screens.ReportPreviewScreen
import com.stellarpath.android.ui.screens.ReportsScreen
import com.stellarpath.android.ui.screens.SettingsScreen
import com.stellarpath.android.ui.screens.SplashScreen
import com.stellarpath.android.ui.screens.TransitsScreen

@Composable
fun AppNavGraph(
    startDestination: String = Routes.Splash,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Routes.Splash) {
            SplashScreen(
                onContinue = {
                    navController.navigate(Routes.Onboarding) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                },
                onOpenDemo = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.Onboarding) {
            OnboardingScreen(
                onCreateProfile = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                },
                onUseDemoData = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                onOpenChart = { navController.navigate(Routes.Chart) },
                onOpenTransits = { navController.navigate(Routes.Transits) },
                onOpenCompatibility = { navController.navigate(Routes.Compatibility) },
                onOpenProfiles = { navController.navigate(Routes.Profiles) },
                onOpenReports = { navController.navigate(Routes.Reports) },
                onOpenLibrary = { navController.navigate(Routes.Library) },
                onOpenSettings = { navController.navigate(Routes.Settings) },
                onOpenPaywall = { navController.navigate(Routes.Paywall) },
            )
        }

        composable(Routes.Chart) {
            ChartScreen(
                onBack = { navController.popBackStack() },
                onOpenProfileDetail = { profileId ->
                    navController.navigate(Routes.profileDetail(profileId))
                },
                onOpenReportPreview = { profileId ->
                    navController.navigate(Routes.reportPreview(profileId))
                },
            )
        }

        composable(Routes.Transits) {
            TransitsScreen(
                onBack = { navController.popBackStack() },
                onOpenEventDetail = { eventId ->
                    navController.navigate(Routes.eventDetail(eventId))
                },
                onOpenPaywall = { navController.navigate(Routes.Paywall) },
            )
        }

        composable(Routes.Compatibility) {
            CompatibilityScreen(
                onBack = { navController.popBackStack() },
                onOpenProfileDetail = { profileId ->
                    navController.navigate(Routes.profileDetail(profileId))
                },
                onOpenPaywall = { navController.navigate(Routes.Paywall) },
            )
        }

        composable(Routes.Profiles) {
            ProfilesScreen(
                onBack = { navController.popBackStack() },
                onOpenProfileDetail = { profileId ->
                    navController.navigate(Routes.profileDetail(profileId))
                },
                onCreateProfile = {
                    navController.navigate(Routes.profileEditor())
                },
            )
        }

        composable(Routes.Reports) {
            ReportsScreen(
                onBack = { navController.popBackStack() },
                onOpenReportPreview = { profileId ->
                    navController.navigate(Routes.reportPreview(profileId))
                },
                onOpenPaywall = { navController.navigate(Routes.Paywall) },
            )
        }

        composable(Routes.Library) {
            LibraryScreen(
                onBack = { navController.popBackStack() },
                onOpenTopic = { topicId ->
                    navController.navigate(Routes.libraryDetail(topicId))
                },
            )
        }

        composable(Routes.FamousPeople) {
            FamousPeopleScreen(
                onBack = { navController.popBackStack() },
                onOpenProfileDetail = { personId ->
                    navController.navigate(Routes.profileDetail(personId))
                },
            )
        }

        composable(Routes.Settings) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onOpenPaywall = { navController.navigate(Routes.Paywall) },
            )
        }

        composable(Routes.Paywall) {
            PaywallScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.ProfileDetail,
            arguments = listOf(navArgument(Routes.ARG_PROFILE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString(Routes.ARG_PROFILE_ID) ?: "self"
            ProfileDetailScreen(
                profileId = profileId,
                onBack = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Routes.profileEditor(profileId)) },
                onOpenChart = { navController.navigate(Routes.Chart) },
            )
        }

        composable(
            route = Routes.ProfileEditor,
            arguments = listOf(navArgument(Routes.ARG_PROFILE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString(Routes.ARG_PROFILE_ID) ?: "new"
            ProfileEditorScreen(
                profileId = profileId,
                onBack = { navController.popBackStack() },
                onSaveProfile = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.ReportPreview,
            arguments = listOf(navArgument(Routes.ARG_PROFILE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString(Routes.ARG_PROFILE_ID) ?: "self"
            ReportPreviewScreen(
                profileId = profileId,
                onBack = { navController.popBackStack() },
                onShare = { },
                onExportPdf = { },
            )
        }

        composable(
            route = Routes.LibraryDetail,
            arguments = listOf(navArgument(Routes.ARG_TOPIC_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString(Routes.ARG_TOPIC_ID) ?: "elements"
            LibraryDetailScreen(
                topicId = topicId,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.EventDetail,
            arguments = listOf(navArgument(Routes.ARG_EVENT_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString(Routes.ARG_EVENT_ID) ?: "event"
            EventDetailScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
