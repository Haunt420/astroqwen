package com.stellarpath.android.navigation

enum class TopLevelDestination(val route: String, val label: String) {
    Home(Routes.Home, "Home"),
    Chart(Routes.Chart, "Chart"),
    Transits(Routes.Transits, "Transits"),
    Compatibility(Routes.Compatibility, "Compatibility"),
    Profiles(Routes.Profiles, "Profiles"),
    Reports(Routes.Reports, "Reports"),
    Library(Routes.Library, "Library"),
    Famous(Routes.FamousPeople, "Famous"),
    Settings(Routes.Settings, "Settings"),
}

object Routes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Chart = "chart"
    const val Transits = "transits"
    const val Compatibility = "compatibility"
    const val Profiles = "profiles"
    const val Reports = "reports"
    const val Library = "library"
    const val FamousPeople = "famous-people"
    const val Settings = "settings"
    const val Paywall = "paywall"

    const val ARG_PROFILE_ID = "profileId"
    const val ARG_TOPIC_ID = "topicId"
    const val ARG_EVENT_ID = "eventId"

    const val ProfileDetail = "profile/{$ARG_PROFILE_ID}"
    const val ProfileEditor = "profile-editor/{$ARG_PROFILE_ID}"
    const val ReportPreview = "report-preview/{$ARG_PROFILE_ID}"
    const val LibraryDetail = "library/{$ARG_TOPIC_ID}"
    const val EventDetail = "event/{$ARG_EVENT_ID}"

    fun profileDetail(profileId: String) = "profile/$profileId"

    fun profileEditor(profileId: String = "new") = "profile-editor/$profileId"

    fun reportPreview(profileId: String) = "report-preview/$profileId"

    fun libraryDetail(topicId: String) = "library/$topicId"

    fun eventDetail(eventId: String) = "event/$eventId"
}
