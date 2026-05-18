package com.stellarpath.android.model

import java.time.Instant

enum class SubscriptionTier {
    Free,
    PremiumMonthly,
    PremiumAnnual,
    Lifetime,
}

enum class EntitlementFeature {
    ExtendedTransitRange,           // ← TransitsScreen gates on this
    DetailedTransitInterpretations,
    CompatibilityDepth,
    SpecialEvents,
    PdfExport,
    HouseSystems,
    UnlimitedProfiles,
}

data class SubscriptionEntitlement(
    val tier: SubscriptionTier,
    val active: Boolean,
    val features: Set<EntitlementFeature>,
    val expiresAt: Instant?,
    val autoRenew: Boolean,
    val storeProductId: String?,
)
