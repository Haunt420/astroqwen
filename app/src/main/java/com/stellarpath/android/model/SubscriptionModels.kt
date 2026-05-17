package com.stellarpath.android.model

import androidx.compose.runtime.Immutable
import java.time.Instant

enum class SubscriptionTier {
    Free,
    PremiumMonthly,
    PremiumAnnual,
    Lifetime,
}

enum class EntitlementFeature {
    ExtendedTransitRange,
    DetailedCompatibility,
    DetailedTransitInterpretations,
    SpecialEvents,
    PdfExport,
    HouseSystems,
    Widgets,
    WearOs,
}

@Immutable
data class SubscriptionEntitlement(
    val tier: SubscriptionTier,
    val active: Boolean,
    val features: Set<EntitlementFeature> = emptySet(),
    val expiresAt: Instant? = null,
    val autoRenew: Boolean = false,
    val storeProductId: String? = null,
)
