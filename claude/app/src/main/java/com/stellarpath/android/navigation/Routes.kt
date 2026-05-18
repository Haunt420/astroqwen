package com.stellarpath.android.navigation

object Routes {

    // ── Top-level destinations ────────────────────────────────────────────────
    const val Splash        = "splash"
    const val Onboarding    = "onboarding"
    const val Home          = "home"
    const val Chart         = "chart"
    const val Transits      = "transits"
    const val Compatibility = "compatibility"
    const val Profiles      = "profiles"
    const val Reports       = "reports"
    const val Library       = "library"
    const val FamousPeople  = "famous_people"   // ← was missing; AppNavGraph references this
    const val Settings      = "settings"
    const val Paywall       = "paywall"

    // ── Parameterised route templates ─────────────────────────────────────────
    const val ARG_PROFILE_ID = "profileId"
    const val ARG_TOPIC_ID   = "topicId"
    const val ARG_EVENT_ID   = "eventId"

    const val ProfileDetail = "profile/{$ARG_PROFILE_ID}"
    const val ProfileEditor = "profile_editor/{$ARG_PROFILE_ID}"
    const val ReportPreview = "report_preview/{$ARG_PROFILE_ID}"
    const val LibraryDetail = "library/{$ARG_TOPIC_ID}"
    const val EventDetail   = "event/{$ARG_EVENT_ID}"

    // ── Builder helpers ───────────────────────────────────────────────────────
    fun profileDetail(profileId: String)         = "profile/$profileId"
    fun profileEditor(profileId: String = "new") = "profile_editor/$profileId"
    fun reportPreview(profileId: String)         = "report_preview/$profileId"
    fun libraryDetail(topicId: String)           = "library/$topicId"
    fun eventDetail(eventId: String)             = "event/$eventId"
}
