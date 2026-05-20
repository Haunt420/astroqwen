@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.stellarpath.android.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellarpath.android.data.FamousPeopleDatabase
import com.stellarpath.android.data.SampleAstroData
import com.stellarpath.android.model.AstrologyBody
import com.stellarpath.android.model.Aspect
import com.stellarpath.android.model.AspectType
import com.stellarpath.android.model.BirthProfile
import com.stellarpath.android.model.BirthTimePrecision
import com.stellarpath.android.model.ChartPlacement
import com.stellarpath.android.model.ChartPrecision
import com.stellarpath.android.model.ChartSnapshot
import com.stellarpath.android.model.CompatibilityDimension
import com.stellarpath.android.model.CompatibilityReport
import com.stellarpath.android.model.EntitlementFeature
import com.stellarpath.android.model.HouseSystem
import com.stellarpath.android.model.InterpretationBlock
import com.stellarpath.android.model.PersonCategory
import com.stellarpath.android.model.ProfileType
import com.stellarpath.android.model.ReferenceTopic
import com.stellarpath.android.model.SpecialEvent
import com.stellarpath.android.model.SubscriptionTier
import com.stellarpath.android.model.ThemeMode
import com.stellarpath.android.model.TransitMarker
import com.stellarpath.android.model.ZodiacSign
import com.stellarpath.android.ui.state.LocalStellarPathAppState
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

// ─── Top-level screens ───────────────────────────────────────────────────────

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    onOpenDemo: () -> Unit,
) {
    AppScreenScaffold(title = "StellarPath") { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BrandOrb(modifier = Modifier.size(140.dp))
                Text(
                    text = "Chart-first astrology, tuned for daily use.",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Loading cached profiles, chart state, and premium entitlements.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onContinue) { Text("Continue") }
                    OutlinedButton(onClick = onOpenDemo) { Text("Open demo") }
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onCreateProfile: () -> Unit,
    onUseDemoData: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    AppScreenScaffold(title = "Onboarding") { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Add birth details", subtitle = "Create a natal chart, daily reading, and compatibility graph.") {
                    Text(
                        text = "The app can work with an exact time, an approximate time, or an unknown time profile.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    ProfileChipRow(
                        label = "Start with a sample profile",
                        selectedId = appState.activeProfile().id,
                        profiles = appState.profiles(),
                        onSelect = appState::selectProfile,
                    )
                }
            }
            item {
                SectionCard(title = "What you can do here", subtitle = "Daily reading, transits, compatibility, reports, and reference content.") {
                    FlowList(items = listOf("Natal chart", "Transit timeline", "Compatibility", "PDF report", "Special events", "Reference library"))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onCreateProfile, modifier = Modifier.weight(1f)) { Text("Create profile") }
                    OutlinedButton(onClick = onUseDemoData, modifier = Modifier.weight(1f)) { Text("Use demo data") }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onOpenChart: () -> Unit,
    onOpenTransits: () -> Unit,
    onOpenCompatibility: () -> Unit,
    onOpenProfiles: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.activeProfile()
    val reading = appState.reading(profile.id)
    val chart = appState.chart(profile.id)
    val event = appState.specialEvents().minByOrNull { it.date } ?: appState.specialEvents().first()
    val profiles = appState.profiles()

    AppScreenScaffold(title = "Home") { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Active profile", subtitle = "Switch context without losing chart state.") {
                    ProfileChipRow(label = null, selectedId = profile.id, profiles = profiles, onSelect = appState::selectProfile)
                }
            }
            item {
                SectionCard(title = reading.headline, subtitle = "Today") {
                    Text(text = reading.summary, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    reading.blocks.forEach { block -> ReadingBlockCard(block) }
                }
            }
            item {
                SectionCard(title = "Chart snapshot", subtitle = chart.summary?.headline ?: "Current chart") {
                    ChartWheel(
                        chart = chart,
                        selectedBody = chart.placements.first().body,
                        onPlacementSelected = {},
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(text = chart.summary?.description ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                SectionCard(title = "Next event", subtitle = event.date.toString()) {
                    EventSummaryCard(event = event)
                }
            }
            item {
                SectionCard(title = "Quick actions", subtitle = "Jump to the core flows.") {
                    ActionTileGrid(
                        actions = listOf(
                            ActionTileData("Chart", "Wheel and aspects", onOpenChart),
                            ActionTileData("Transits", "Future scrubber", onOpenTransits),
                            ActionTileData("Compatibility", "Synastry and fit", onOpenCompatibility),
                            ActionTileData("Profiles", "People vault", onOpenProfiles),
                            ActionTileData("Reports", "PDF and share", onOpenReports),
                            ActionTileData("Library", "Elements and houses", onOpenLibrary),
                            ActionTileData("Settings", "Theme and privacy", onOpenSettings),
                            ActionTileData("Paywall", "Premium unlocks", onOpenPaywall),
                        ),
                    )
                }
            }
        }
    }
}

// ─── Chart screen ─────────────────────────────────────────────────────────────

@Composable
fun ChartScreen(
    onBack: () -> Unit,
    onOpenProfileDetail: (String) -> Unit,
    onOpenReportPreview: (String) -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.activeProfile()
    val chart = appState.chart(profile.id)
    var selectedBody by rememberSaveable { mutableStateOf(AstrologyBody.Sun) }
    val selectedPlacement = chart.placements.firstOrNull { it.body == selectedBody } ?: chart.placements.first()

    AppScreenScaffold(title = "Chart", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Profile", subtitle = profile.displayName) {
                    ProfileChipRow(label = null, selectedId = profile.id, profiles = appState.profiles(), onSelect = appState::selectProfile)
                }
            }
            item {
                SectionCard(
                    title = "Natal wheel",
                    subtitle = when (chart.precision) {
                        ChartPrecision.Exact -> "Exact chart"
                        ChartPrecision.Estimated -> "Estimated chart"
                        ChartPrecision.UnknownTime -> "Unknown time"
                    },
                ) {
                    ChartWheel(
                        chart = chart,
                        selectedBody = selectedBody,
                        onPlacementSelected = { selectedBody = it },
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                }
            }
            item {
                SectionCard(title = selectedPlacement.body.name, subtitle = "Selected placement") {
                    PlacementDetailCard(selectedPlacement = selectedPlacement, chart = chart)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { onOpenProfileDetail(profile.id) }, modifier = Modifier.weight(1f)) { Text("Profile") }
                        Button(onClick = { onOpenReportPreview(profile.id) }, modifier = Modifier.weight(1f)) { Text("Report") }
                    }
                }
            }
            item {
                SectionCard(title = "Aspects", subtitle = "Tap a row to inspect the relationship.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        chart.aspects.forEach { aspect -> AspectRow(aspect = aspect) }
                    }
                }
            }
            item {
                SectionCard(title = "Placements", subtitle = "Planet, sign, degree, and house.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        chart.placements.forEach { placement ->
                            PlacementRow(
                                placement = placement,
                                selected = placement.body == selectedBody,
                                onClick = { selectedBody = placement.body },
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Transits screen ──────────────────────────────────────────────────────────

@Composable
fun TransitsScreen(
    onBack: () -> Unit,
    onOpenEventDetail: (String) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.activeProfile()
    val window = appState.transitWindow()
    val activeDate = window.activeDate
    val totalDays = ChronoUnit.DAYS.between(window.startDate, window.endDate).toInt().coerceAtLeast(1)
    val selectedOffset = ChronoUnit.DAYS.between(window.startDate, activeDate).toFloat()
    val premiumLocked = EntitlementFeature.ExtendedTransitRange !in appState.subscription().features

    val transitChart = appState.transitChart(profile.id, activeDate)
    val transitReading = appState.transitReading(profile.id, activeDate)
    var selectedBody by rememberSaveable { mutableStateOf(AstrologyBody.Sun) }
    val transitAspects = transitChart.transitAspects ?: emptyList()
    val majorAspects = transitAspects.filter {
        it.type in listOf(AspectType.Conjunction, AspectType.Opposition, AspectType.Trine, AspectType.Square)
    }

    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    AppScreenScaffold(title = "Transits", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (premiumLocked) {
                item {
                    PremiumBanner(
                        title = "Extended range is premium",
                        description = "Scrub further into the future by unlocking the full transit window.",
                        onOpenPaywall = onOpenPaywall,
                    )
                }
            }

            item {
                ElevatedCard(shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = activeDate.dayOfMonth.toString().padStart(2, '0'),
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "${monthNames[activeDate.monthValue - 1]} ${activeDate.year}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = dayNames[activeDate.dayOfWeek.value - 1],
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                val daysFrom = ChronoUnit.DAYS.between(LocalDate.now(), activeDate)
                                Text(
                                    text = when {
                                        daysFrom == 0L -> "Today"
                                        daysFrom > 0 -> "+${daysFrom}d ahead"
                                        else -> "${-daysFrom}d ago"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${window.startDate}  \u2014  ${window.endDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = window.startDate.toString(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = window.endDate.toString(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Slider(
                                value = selectedOffset,
                                onValueChange = { v ->
                                    val clamped = v.coerceIn(0f, totalDays.toFloat())
                                    appState.selectTransitDate(window.startDate.plusDays(clamped.roundToInt().toLong()))
                                },
                                valueRange = 0f..totalDays.toFloat(),
                            )
                            Text(text = activeDate.toString(), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }

                        if (majorAspects.isNotEmpty()) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                majorAspects.take(3).forEach { aspect ->
                                    val label = when (aspect.type) {
                                        AspectType.Conjunction -> "\u260C"
                                        AspectType.Opposition -> "\u260D"
                                        AspectType.Trine -> "\u25B3"
                                        AspectType.Square -> "\u25A1"
                                        else -> "\u2219"
                                    }
                                    AssistBubble(text = "$label ${aspect.source.name.take(3)}-${aspect.target.name.take(3)}")
                                }
                                if (majorAspects.size > 3) AssistBubble(text = "+${majorAspects.size - 3}")
                            }
                        }
                    }
                }
            }

            item {
                ElevatedCard(shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Transit overlay", style = MaterialTheme.typography.titleLarge)
                            AssistBubble(text = "${transitChart.transitPlacements?.size ?: 0} bodies")
                        }
                        Text(text = "Natal (inner) \u00B7 Transit (outer)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item {
                ChartWheel(
                    chart = transitChart,
                    selectedBody = selectedBody,
                    onPlacementSelected = { selectedBody = it },
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    transitMode = true,
                )
            }

            if (transitAspects.isNotEmpty()) {
                item {
                    ElevatedCard(shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(text = "Transit aspects", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = when {
                                    majorAspects.size >= 5 -> "Heavy transit day \u2014 several bodies activated."
                                    majorAspects.isNotEmpty() -> "${majorAspects.size} major aspect${if (majorAspects.size != 1) "s" else ""} in effect."
                                    else -> "No major aspects today. Background influence is subtle."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            if (window.markers.isNotEmpty()) {
                item {
                    Text(text = "Events", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 4.dp))
                }
                window.markers.forEach { marker ->
                    item {
                        TransitMarkerCard(marker = marker, onClick = { onOpenEventDetail(marker.id) })
                    }
                }
            }
        }
    }
}

// ─── Compatibility screen ─────────────────────────────────────────────────────

@Composable
fun CompatibilityScreen(
    onBack: () -> Unit,
    onOpenProfileDetail: (String) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profiles = appState.profiles()
    val report = appState.compatibilityReport()
    val primary = appState.profile(report.primaryProfileId)
    val secondary = appState.profile(report.secondaryProfileId)

    AppScreenScaffold(title = "Compatibility", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (report.premiumLocked) {
                item {
                    PremiumBanner(
                        title = "Detailed compatibility is premium",
                        description = "The full synastry breakdown is locked in the free tier.",
                        onOpenPaywall = onOpenPaywall,
                    )
                }
            }
            item {
                SectionCard(title = "Choose the pair", subtitle = "Compare any two saved people.") {
                    ProfileSelectionPair(label = "Primary", selectedId = report.primaryProfileId, profiles = profiles, onSelect = appState::selectCompatibilityPrimary)
                    Spacer(Modifier.height(12.dp))
                    ProfileSelectionPair(label = "Secondary", selectedId = report.secondaryProfileId, profiles = profiles, onSelect = appState::selectCompatibilitySecondary)
                }
            }
            item {
                SectionCard(title = "${primary.displayName} × ${secondary.displayName}", subtitle = "Overall compatibility") {
                    Text(text = report.overallSummary, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { onOpenProfileDetail(primary.id) }, modifier = Modifier.weight(1f)) { Text(primary.displayName) }
                        OutlinedButton(onClick = { onOpenProfileDetail(secondary.id) }, modifier = Modifier.weight(1f)) { Text(secondary.displayName) }
                    }
                }
            }
            item { SectionCard(title = "Matching elements", subtitle = "Base temperament") { CompatibilityDimensionCard(report.elementMatch) } }
            item { SectionCard(title = "Harmony", subtitle = "Emotional cadence and alignment") { CompatibilityDimensionCard(report.harmony) } }
            item { SectionCard(title = "Passion", subtitle = "Charge and attraction") { CompatibilityDimensionCard(report.passion) } }
        }
    }
}

// ─── Profiles screen ──────────────────────────────────────────────────────────

@Composable
fun ProfilesScreen(
    onBack: () -> Unit,
    onOpenProfileDetail: (String) -> Unit,
    onCreateProfile: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profiles = appState.profiles()

    AppScreenScaffold(title = "Profiles", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onCreateProfile, modifier = Modifier.weight(1f)) { Text("Create profile") }
                    OutlinedButton(onClick = { appState.selectProfile(appState.activeProfile().id) }, modifier = Modifier.weight(1f)) { Text("Set active") }
                }
            }
            items(profiles, key = { it.id }) { profile ->
                ProfileCard(profile = profile, selected = profile.id == appState.activeProfile().id, onClick = { onOpenProfileDetail(profile.id) })
            }
        }
    }
}

// ─── Reports screen ───────────────────────────────────────────────────────────

@Composable
fun ReportsScreen(
    onBack: () -> Unit,
    onOpenReportPreview: (String) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.activeProfile()
    val reading = appState.reading(profile.id)
    val chart = appState.chart(profile.id)

    AppScreenScaffold(title = "Reports", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "PDF report", subtitle = "Export a permanent chart summary.") {
                    ReportPreviewCard(profile = profile, reading = reading, chart = chart)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { onOpenReportPreview(profile.id) }, modifier = Modifier.weight(1f)) { Text("Preview") }
                        Button(onClick = onOpenPaywall, modifier = Modifier.weight(1f)) { Text("Unlock export") }
                    }
                }
            }
            item {
                SectionCard(title = "Share snapshot", subtitle = "Card-style export for social and messaging apps.") {
                    ShareSnapshotPreview(profile = profile, reading = reading)
                }
            }
        }
    }
}

// ─── Library screen ───────────────────────────────────────────────────────────

@Composable
fun LibraryScreen(
    onBack: () -> Unit,
    onOpenTopic: (String) -> Unit,
) {
    val topics = LocalStellarPathAppState.current.referenceTopics()

    AppScreenScaffold(title = "Library", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Astrology reference", subtitle = "Elements, qualities, dualities, houses, planets, and special events.") {
                    FlowList(items = topics.map { it.title })
                }
            }
            items(topics, key = { it.id }) { topic ->
                ReferenceTopicCard(topic = topic, onClick = { onOpenTopic(topic.id) })
            }
        }
    }
}

// ─── Famous people screen ─────────────────────────────────────────────────────

@Composable
fun FamousPeopleScreen(
    onBack: () -> Unit,
    onOpenProfileDetail: (String) -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<PersonCategory?>(null) }
    val categories = PersonCategory.entries

    val filteredPeople = remember(searchQuery, selectedCategory) {
        when {
            searchQuery.isNotBlank() -> FamousPeopleDatabase.searchByName(searchQuery)
            selectedCategory != null -> FamousPeopleDatabase.searchByCategory(selectedCategory!!)
            else -> FamousPeopleDatabase.famousPeople
        }
    }

    AppScreenScaffold(title = "Famous People", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by name or profession...") },
                    singleLine = true,
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(selected = selectedCategory == null, onClick = { selectedCategory = null }, label = { Text("All") })
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.replaceFirstChar { it.uppercase() }) },
                        )
                    }
                }
            }
            item {
                Text(text = "${filteredPeople.size} people found", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            items(filteredPeople, key = { it.id }) { person ->
                ElevatedCard(onClick = { onOpenProfileDetail(person.id) }, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = person.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            AssistBubble(text = person.category.name)
                        }
                        Text(text = person.profession, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = person.birthDate.toString(), style = MaterialTheme.typography.bodySmall)
                            person.birthLocation?.let { loc ->
                                Text(text = "\u2022 ${loc.city}, ${loc.countryCode}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (person.notes.isNotBlank()) {
                            Text(text = person.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

// ─── Settings screen ──────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val settings = appState.settings()
    val subscription = appState.subscription()

    AppScreenScaffold(title = "Settings", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Appearance", subtitle = "Theme mode and brand presentation.") {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ThemeChoice(label = "System", selected = settings.themeMode == ThemeMode.System, onClick = { appState.updateThemeMode(ThemeMode.System) })
                        ThemeChoice(label = "Light", selected = settings.themeMode == ThemeMode.Light, onClick = { appState.updateThemeMode(ThemeMode.Light) })
                        ThemeChoice(label = "Dark", selected = settings.themeMode == ThemeMode.Dark, onClick = { appState.updateThemeMode(ThemeMode.Dark) })
                    }
                }
            }
            item {
                SectionCard(title = "House system", subtitle = "Default chart interpretation model.") {
                    HouseSystem.entries.forEach { houseSystem ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { appState.updateDefaultHouseSystem(houseSystem) }.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = settings.defaultHouseSystem == houseSystem, onClick = { appState.updateDefaultHouseSystem(houseSystem) })
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(houseSystem.name)
                                Text(text = houseSystemDescription(houseSystem), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
            item {
                SectionCard(title = "Notifications", subtitle = "Daily reading and transit alerts.") {
                    SettingSwitchRow(label = "Daily reading", checked = settings.notifications.dailyReadingEnabled, onCheckedChange = { appState.updateNotificationPreference(dailyReadingEnabled = it) })
                    SettingSwitchRow(label = "Transit alerts", checked = settings.notifications.transitAlertsEnabled, onCheckedChange = { appState.updateNotificationPreference(transitAlertsEnabled = it) })
                    SettingSwitchRow(label = "Special events", checked = settings.notifications.specialEventAlertsEnabled, onCheckedChange = { appState.updateNotificationPreference(specialEventAlertsEnabled = it) })
                    SettingSwitchRow(label = "Marketing", checked = settings.notifications.marketingEnabled, onCheckedChange = { appState.updateNotificationPreference(marketingEnabled = it) })
                }
            }
            item {
                SectionCard(title = "Subscription", subtitle = "Current entitlement state.") {
                    Text(
                        text = when (subscription.tier) {
                            SubscriptionTier.Free -> "Free tier active"
                            SubscriptionTier.PremiumMonthly -> "Premium monthly active"
                            SubscriptionTier.PremiumAnnual -> "Premium annual active"
                            SubscriptionTier.Lifetime -> "Lifetime access active"
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Restore purchases or move to the premium screen to review unlocks.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = onOpenPaywall, modifier = Modifier.weight(1f)) { Text("Open paywall") }
                        Button(onClick = onOpenPaywall, modifier = Modifier.weight(1f)) { Text("Restore") }
                    }
                }
            }
            item {
                SectionCard(title = "Privacy", subtitle = "Data handling and local storage.") {
                    Text(text = "Birth data stays local in this scaffold. Automatic backups are disabled.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ─── Paywall screen ───────────────────────────────────────────────────────────

@Composable
fun PaywallScreen(onBack: () -> Unit) {
    AppScreenScaffold(title = "Premium", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Unlock the full chart experience", subtitle = "Feature gates mirror the observable product shape.") {
                    FlowList(items = listOf("Extended transit range", "Detailed transit interpretations", "Compatibility depth", "Special events", "PDF export", "House systems"))
                }
            }
            item { PlanCard(title = "Monthly", price = "$4.99", description = "Good for trying the premium surface without a long commitment.") }
            item { PlanCard(title = "Annual", price = "$24.99", description = "Better for daily users who want the extended chart and report tools.") }
            item { PlanCard(title = "Lifetime", price = "$59.99", description = "One-time access for users who plan to keep a long-term chart history.") }
        }
    }
}

// ─── Detail screens ───────────────────────────────────────────────────────────

@Composable
fun ProfileDetailScreen(
    profileId: String,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenChart: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.profile(profileId)
    val reading = appState.reading(profileId)
    val chart = appState.chart(profileId)

    AppScreenScaffold(title = profile.displayName, onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Profile", subtitle = profile.profileType.name) {
                    ProfileSummary(profile = profile)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = onEditProfile, modifier = Modifier.weight(1f)) { Text("Edit") }
                        Button(onClick = onOpenChart, modifier = Modifier.weight(1f)) { Text("Chart") }
                    }
                }
            }
            item { SectionCard(title = "Chart summary", subtitle = chart.summary?.headline ?: "Chart") { Text(chart.summary?.description ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
            item {
                SectionCard(title = "Daily reading", subtitle = reading.date.toString()) {
                    Text(reading.headline, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(reading.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ProfileEditorScreen(
    profileId: String,
    onBack: () -> Unit,
    onSaveProfile: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val source = if (profileId == "new") null else appState.profile(profileId)

    var name by rememberSaveable(profileId) { mutableStateOf(source?.displayName ?: "") }
    var city by rememberSaveable(profileId) { mutableStateOf(source?.location?.city ?: "") }
    var region by rememberSaveable(profileId) { mutableStateOf(source?.location?.region ?: "") }
    var country by rememberSaveable(profileId) { mutableStateOf(source?.location?.countryCode ?: "US") }
    var birthDate by rememberSaveable(profileId) { mutableStateOf(source?.birthDate?.toString() ?: "") }
    var birthTime by rememberSaveable(profileId) { mutableStateOf(source?.birthTime?.toString() ?: "") }
    var timezone by rememberSaveable(profileId) { mutableStateOf(source?.location?.timezoneId ?: "") }
    var notes by rememberSaveable(profileId) { mutableStateOf(source?.notes ?: "") }
    var unknownTime by rememberSaveable(profileId) { mutableStateOf(source?.birthTimePrecision == BirthTimePrecision.Unknown) }
    var profileType by rememberSaveable(profileId) { mutableStateOf(source?.profileType ?: ProfileType.Self) }
    var houseSystem by rememberSaveable(profileId) { mutableStateOf(source?.houseSystem ?: appState.settings().defaultHouseSystem) }

    AppScreenScaffold(title = if (source == null) "New profile" else "Edit profile", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionCard(title = "Identity", subtitle = "Person and label.") {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    ProfileTypeRow(selected = profileType, onSelect = { profileType = it })
                }
            }
            item {
                SectionCard(title = "Birth data", subtitle = "Enter date, time, location, and timezone.") {
                    OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Birth date") }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("YYYY-MM-DD") })
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = birthTime, onValueChange = { birthTime = it }, label = { Text("Birth time") }, modifier = Modifier.weight(1f), placeholder = { Text("HH:MM") }, enabled = !unknownTime)
                        OutlinedTextField(value = timezone, onValueChange = { timezone = it }, label = { Text("Timezone") }, modifier = Modifier.weight(1f), placeholder = { Text("Region/City") })
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Unknown time", modifier = Modifier.weight(1f))
                        Switch(checked = unknownTime, onCheckedChange = { unknownTime = it; if (it) birthTime = "" })
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Region") }, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country code") }, modifier = Modifier.fillMaxWidth())
                }
            }
            item {
                SectionCard(title = "Interpretation preferences", subtitle = "House system and notes.") {
                    HouseSystemRow(selected = houseSystem, onSelect = { houseSystem = it })
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                }
            }
            item {
                Button(onClick = onSaveProfile, modifier = Modifier.fillMaxWidth()) { Text("Save profile") }
            }
        }
    }
}

@Composable
fun ReportPreviewScreen(
    profileId: String,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onExportPdf: () -> Unit,
) {
    val appState = LocalStellarPathAppState.current
    val profile = appState.profile(profileId)
    val reading = appState.reading(profileId)
    val chart = appState.chart(profileId)

    AppScreenScaffold(title = "Report preview", onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { SectionCard(title = "Preview", subtitle = "PDF-ready layout") { ReportPreviewCard(profile = profile, reading = reading, chart = chart) } }
            item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = onShare, enabled = false, modifier = Modifier.weight(1f)) { Text("Share") }
                        Button(onClick = onExportPdf, enabled = false, modifier = Modifier.weight(1f)) { Text("Export PDF") }
                    }
            }
        }
    }
}

@Composable
fun LibraryDetailScreen(topicId: String, onBack: () -> Unit) {
    val topic = LocalStellarPathAppState.current.referenceTopic(topicId)
    AppScreenScaffold(title = topic.title, onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { SectionCard(title = topic.title, subtitle = topic.summary) { FlowList(items = topic.sections.map { it.title }) } }
            items(topic.sections, key = { it.id }) { section ->
                SectionCard(title = section.title, subtitle = "Reference") { Text(section.body) }
            }
        }
    }
}

@Composable
fun EventDetailScreen(eventId: String, onBack: () -> Unit) {
    val event = LocalStellarPathAppState.current.specialEvent(eventId)
    AppScreenScaffold(title = event.title, onBack = onBack) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { SectionCard(title = event.title, subtitle = event.date.toString()) { EventSummaryCard(event = event) } }
        }
    }
}

// ─── Scaffold ─────────────────────────────────────────────────────────────────

@Composable
private fun AppScreenScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = if (onBack == null) ({}) else ({
                    TextButton(onClick = onBack) { Text("Back") }
                }),
            )
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}

// ─── Reusable components ──────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ElevatedCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                if (subtitle != null) Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            content()
        }
    }
}

@Composable
private fun ProfileChipRow(label: String?, selectedId: String, profiles: List<BirthProfile>, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (label != null) Text(label, style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            profiles.forEach { profile ->
                FilterChip(selected = profile.id == selectedId, onClick = { onSelect(profile.id) }, label = { Text(profile.displayName) })
            }
        }
    }
}

@Composable
private fun ProfileSelectionPair(label: String, selectedId: String, profiles: List<BirthProfile>, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            profiles.forEach { profile ->
                FilterChip(selected = profile.id == selectedId, onClick = { onSelect(profile.id) }, label = { Text(profile.displayName) })
            }
        }
    }
}

@Composable
private fun ProfileTypeRow(selected: ProfileType, onSelect: (ProfileType) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Profile type", style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileType.entries.forEach { type ->
                FilterChip(selected = selected == type, onClick = { onSelect(type) }, label = { Text(type.name) })
            }
        }
    }
}

@Composable
private fun HouseSystemRow(selected: HouseSystem, onSelect: (HouseSystem) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("House system", style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HouseSystem.entries.forEach { system ->
                FilterChip(selected = selected == system, onClick = { onSelect(system) }, label = { Text(system.name) })
            }
        }
    }
}

@Composable
private fun FlowList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { item -> AssistBubble(text = item, modifier = Modifier.weight(1f)) }
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AssistBubble(text: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(999.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ActionTileGrid(actions: List<ActionTileData>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        actions.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { action -> ActionTile(data = action, modifier = Modifier.weight(1f).heightIn(min = 92.dp)) }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ActionTile(data: ActionTileData, modifier: Modifier = Modifier) {
    ElevatedCard(onClick = data.onClick, modifier = modifier, shape = RoundedCornerShape(22.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = data.title, style = MaterialTheme.typography.titleMedium)
            Text(text = data.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ReadingBlockCard(block: InterpretationBlock) {
    OutlinedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(block.title, style = MaterialTheme.typography.titleMedium)
            Text(block.body, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FlowList(items = block.relatedBodies.map { it.name })
        }
    }
}

@Composable
private fun EventSummaryCard(event: SpecialEvent) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(event.title, style = MaterialTheme.typography.titleMedium)
        Text(text = event.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
        AssistBubble(text = event.type.name)
    }
}

@Composable
private fun PlacementDetailCard(selectedPlacement: ChartPlacement, chart: ChartSnapshot) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "${selectedPlacement.sign.name} ${formatDegree(selectedPlacement.degreeInSign)}", style = MaterialTheme.typography.titleMedium)
        Text(text = "House ${selectedPlacement.house ?: "?"} \u00B7 ${chart.houseSystem.name}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "This placement helps explain the chart's emphasis on ${selectedPlacement.body.name.lowercase(Locale.US)}.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PlacementRow(placement: ChartPlacement, selected: Boolean, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface),
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            BodyBadge(body = placement.body, selected = selected)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = placement.body.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${placement.sign.name} ${formatDegree(placement.degreeInSign)} \u00B7 House ${placement.house ?: "?"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AspectRow(aspect: Aspect) {
    OutlinedCard(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AssistBubble(text = aspect.type.name)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${aspect.source.name} ${aspect.type.name.lowercase(Locale.US)} ${aspect.target.name}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Orb ${formatDegree(aspect.orb)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (aspect.premiumLocked) AssistBubble(text = "Premium")
        }
    }
}

@Composable
private fun CompatibilityDimensionCard(dimension: CompatibilityDimension) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AssistBubble(text = dimension.label)
            if (dimension.score != null) Text(text = "${dimension.score}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        if (dimension.score != null) LinearProgressIndicator(progress = { dimension.score / 100f }, modifier = Modifier.fillMaxWidth())
        Text(dimension.summary)
        Text(text = dimension.details, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileCard(profile: BirthProfile, selected: Boolean, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                BodyBadge(body = AstrologyBody.Sun, selected = selected)
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile.displayName, style = MaterialTheme.typography.titleLarge)
                    Text(text = profile.profileType.name, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (selected) AssistBubble(text = "Active")
            }
            ProfileSummary(profile)
        }
    }
}

@Composable
private fun ProfileSummary(profile: BirthProfile) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = profile.birthDate.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "${profile.location.city}${profile.location.region?.let { ", $it" } ?: ""}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (profile.birthTimePrecision == BirthTimePrecision.Unknown) {
            AssistBubble(text = "Unknown birth time")
        } else {
            Text(text = profile.birthTime?.toString() ?: "Time unavailable", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ReportPreviewCard(profile: BirthProfile, reading: com.stellarpath.android.model.DailyReading, chart: ChartSnapshot) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(profile.displayName, style = MaterialTheme.typography.titleLarge)
            Text(reading.headline, style = MaterialTheme.typography.titleMedium)
            Text(reading.summary)
            Text(text = chart.summary?.headline ?: "Chart summary", color = MaterialTheme.colorScheme.onSurfaceVariant)
            FlowList(items = listOf("Natal chart", "Daily reading", "Compatibility", "Special events"))
        }
    }
}

@Composable
private fun ShareSnapshotPreview(profile: BirthProfile, reading: com.stellarpath.android.model.DailyReading) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("${profile.displayName} today", style = MaterialTheme.typography.titleLarge)
            Text(reading.headline, style = MaterialTheme.typography.titleMedium)
            Text(reading.summary)
        }
    }
}

@Composable
private fun ReferenceTopicCard(topic: ReferenceTopic, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(topic.title, style = MaterialTheme.typography.titleLarge)
            Text(topic.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ThemeChoice(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

@Composable
private fun SettingSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun PlanCard(title: String, price: String, description: String) {
    ElevatedCard(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(price, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = {}, enabled = false) { Text("Choose plan") }
        }
    }
}

@Composable
private fun PremiumBanner(title: String, description: String, onOpenPaywall: () -> Unit) {
    OutlinedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = onOpenPaywall) { Text("Open premium") }
        }
    }
}

@Composable
private fun BodyBadge(body: AstrologyBody, selected: Boolean) {
    val color = if (selected) MaterialTheme.colorScheme.onPrimary else bodyColor(body)
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary else bodyColor(body).copy(alpha = 0.15f),
        contentColor = color,
        modifier = Modifier.size(42.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = bodyAbbreviation(body), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun BrandOrb(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)).border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "SP", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun TransitMarkerCard(marker: TransitMarker, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                AssistBubble(text = marker.type.name)
                Text(marker.date.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(marker.title, style = MaterialTheme.typography.titleMedium)
            Text(marker.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─── Chart wheel ──────────────────────────────────────────────────────────────

@Composable
private fun ChartWheel(
    chart: ChartSnapshot,
    selectedBody: AstrologyBody,
    onPlacementSelected: (AstrologyBody) -> Unit,
    modifier: Modifier = Modifier,
    transitMode: Boolean = false,
) {
    val textMeasurer = rememberTextMeasurer()
    val transitPlacements = if (transitMode) chart.transitPlacements ?: emptyList() else emptyList()
    val transitAspects = if (transitMode) chart.transitAspects ?: emptyList() else emptyList()
    val hasTransit = transitPlacements.isNotEmpty()

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val density = LocalDensity.current
        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.secondary
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
        val surfaceColor = MaterialTheme.colorScheme.surface
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val wheelRadius = (min(widthPx, heightPx) / 2f) * 0.78f
        val natalRadius = wheelRadius * (if (hasTransit) 0.60f else 0.82f)
        val transitRadius = wheelRadius * 0.88f
        val labelRadius = wheelRadius * 0.96f
        val nodeSize = if (hasTransit) 30.dp else 42.dp
        val transitNodeSize = 26.dp
        val nodeSizePx = with(density) { nodeSize.toPx() }
        val transitNodeSizePx = with(density) { transitNodeSize.toPx() }
        val centerX = widthPx / 2f
        val centerY = heightPx / 2f
        val signNames = ZodiacSign.entries.map { it.name.take(3).uppercase() }

        Canvas(
            modifier = Modifier.matchParentSize().pointerInput(chart, selectedBody, transitMode) {
                detectTapGestures { tapOffset ->
                    var closest: AstrologyBody? = null
                    var closestDist = Float.MAX_VALUE
                    val hitTargets = if (hasTransit) transitPlacements else chart.placements
                    val hitR = if (hasTransit) transitNodeSizePx else nodeSizePx
                    val hitRing = if (hasTransit) transitRadius else natalRadius
                    hitTargets.forEach { placement ->
                        val angle = placementAngle(placement)
                        val rad = Math.toRadians((angle - 90.0))
                        val px = centerX + cos(rad).toFloat() * hitRing
                        val py = centerY + sin(rad).toFloat() * hitRing
                        val dist = kotlin.math.sqrt((tapOffset.x - px) * (tapOffset.x - px) + (tapOffset.y - py) * (tapOffset.y - py))
                        if (dist < closestDist && dist < hitR) { closestDist = dist; closest = placement.body }
                    }
                    // Also allow tapping natal ring in transit mode
                    if (hasTransit && closest == null) {
                        chart.placements.forEach { placement ->
                            val angle = placementAngle(placement)
                            val rad = Math.toRadians((angle - 90.0))
                            val px = centerX + cos(rad).toFloat() * natalRadius
                            val py = centerY + sin(rad).toFloat() * natalRadius
                            val dist = kotlin.math.sqrt((tapOffset.x - px) * (tapOffset.x - px) + (tapOffset.y - py) * (tapOffset.y - py))
                            if (dist < closestDist && dist < nodeSizePx) { closestDist = dist; closest = placement.body }
                        }
                    }
                    closest?.let { onPlacementSelected(it) }
                }
            },
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(color = primaryColor.copy(alpha = 0.10f), radius = wheelRadius, center = center, style = Stroke(width = 3.dp.toPx()))
            if (hasTransit) {
                drawCircle(color = secondaryColor.copy(alpha = 0.10f), radius = transitRadius, center = center, style = Stroke(width = 1.5f.dp.toPx()))
                drawCircle(color = tertiaryColor.copy(alpha = 0.08f), radius = natalRadius * 1.05f, center = center, style = Stroke(width = 1.dp.toPx()))
            }

            repeat(12) { index ->
                val angle = Math.toRadians((index * 30.0 - 90.0))
                drawLine(
                    color = onSurfaceVariantColor.copy(alpha = 0.12f),
                    start = Offset(center.x + cos(angle).toFloat() * natalRadius, center.y + sin(angle).toFloat() * natalRadius),
                    end = Offset(center.x + cos(angle).toFloat() * wheelRadius, center.y + sin(angle).toFloat() * wheelRadius),
                    strokeWidth = 1.dp.toPx(),
                )
                val labelAngle = Math.toRadians((index * 30.0))
                val lx = center.x + cos(labelAngle).toFloat() * labelRadius
                val ly = center.y + sin(labelAngle).toFloat() * labelRadius
                val textResult = textMeasurer.measure(text = signNames[index], style = TextStyle(fontSize = 9.sp, color = onSurfaceVariantColor.copy(alpha = 0.5f)))
                drawText(textLayoutResult = textResult, topLeft = Offset(lx - textResult.size.width / 2f, ly - textResult.size.height / 2f))
            }

            // Transit aspects — dashed
            if (hasTransit) {
                transitAspects.forEach { aspect ->
                    val src = transitPlacements.firstOrNull { it.body == aspect.source } ?: return@forEach
                    val tgt = chart.placements.firstOrNull { it.body == aspect.target } ?: return@forEach
                    val srcRad = Math.toRadians((placementAngle(src) - 90.0))
                    val tgtRad = Math.toRadians((placementAngle(tgt) - 90.0))
                    val sx = center.x + cos(srcRad).toFloat() * transitRadius
                    val sy = center.y + sin(srcRad).toFloat() * transitRadius
                    val tx = center.x + cos(tgtRad).toFloat() * natalRadius
                    val ty = center.y + sin(tgtRad).toFloat() * natalRadius
                    val highlighted = aspect.source == selectedBody || aspect.target == selectedBody
                    drawLine(
                        color = if (highlighted) tertiaryColor else tertiaryColor.copy(alpha = 0.20f),
                        start = Offset(sx, sy), end = Offset(tx, ty),
                        strokeWidth = if (highlighted) 2.dp.toPx() else 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f)),
                    )
                }
            }

            // Natal aspects — solid
            chart.aspects.forEach { aspect ->
                val src = chart.placements.firstOrNull { it.body == aspect.source } ?: return@forEach
                val tgt = chart.placements.firstOrNull { it.body == aspect.target } ?: return@forEach
                val srcRad = Math.toRadians((placementAngle(src) - 90.0))
                val tgtRad = Math.toRadians((placementAngle(tgt) - 90.0))
                val sx = center.x + cos(srcRad).toFloat() * natalRadius
                val sy = center.y + sin(srcRad).toFloat() * natalRadius
                val tx = center.x + cos(tgtRad).toFloat() * natalRadius
                val ty = center.y + sin(tgtRad).toFloat() * natalRadius
                val highlighted = aspect.source == selectedBody || aspect.target == selectedBody
                drawLine(
                    color = if (highlighted) tertiaryColor else tertiaryColor.copy(alpha = 0.15f),
                    start = Offset(sx, sy), end = Offset(tx, ty),
                    strokeWidth = if (highlighted) 2.dp.toPx() else 0.8f.dp.toPx(),
                )
            }

            drawCircle(color = surfaceColor, radius = if (hasTransit) natalRadius * 0.30f else wheelRadius * 0.30f, center = center)
        }

        // Natal planet nodes
        chart.placements.forEach { placement ->
            val angle = placementAngle(placement)
            val rad = Math.toRadians((angle - 90.0))
            val x = centerX + cos(rad).toFloat() * natalRadius
            val y = centerY + sin(rad).toFloat() * natalRadius
            val selected = placement.body == selectedBody
            val sz = if (hasTransit) 28.dp else nodeSize
            val szPx = with(density) { sz.toPx() }
            Surface(
                shape = CircleShape,
                color = if (selected) MaterialTheme.colorScheme.primary else bodyColor(placement.body).copy(alpha = 0.96f),
                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                shadowElevation = if (selected) 8.dp else 2.dp,
                modifier = Modifier
                    .offset { IntOffset((x - szPx / 2f).roundToInt(), (y - szPx / 2f).roundToInt()) }
                    .size(sz)
                    .clickable { onPlacementSelected(placement.body) },
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = bodyAbbreviation(placement.body), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }

        // Transit planet nodes (outlined ring style)
        if (hasTransit) {
            transitPlacements.forEach { placement ->
                val angle = placementAngle(placement)
                val rad = Math.toRadians((angle - 90.0))
                val x = centerX + cos(rad).toFloat() * transitRadius
                val y = centerY + sin(rad).toFloat() * transitRadius
                val selected = placement.body == selectedBody
                val col = bodyColor(placement.body)
                val tnsz = transitNodeSize
                val tnszPx = with(density) { tnsz.toPx() }
                Surface(
                    shape = CircleShape,
                    color = if (selected) col.copy(alpha = 0.9f) else Color.Transparent,
                    contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else col,
                    border = if (selected) null else BorderStroke(1.5.dp, col.copy(alpha = 0.7f)),
                    modifier = Modifier
                        .offset { IntOffset((x - tnszPx / 2f).roundToInt(), (y - tnszPx / 2f).roundToInt()) }
                        .size(tnsz)
                        .clickable { onPlacementSelected(placement.body) },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = bodyAbbreviation(placement.body), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 8.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        // Center label
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 3.dp,
            shadowElevation = 2.dp,
            modifier = Modifier.size(if (hasTransit) 72.dp else 104.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (hasTransit) "Now" else "Chart", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = chart.houseSystem.name.take(4), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ─── Pure helpers ─────────────────────────────────────────────────────────────

private data class ActionTileData(val title: String, val subtitle: String, val onClick: () -> Unit)

private fun bodyAbbreviation(body: AstrologyBody): String = when (body) {
    AstrologyBody.Sun -> "Sun"; AstrologyBody.Moon -> "Moon"; AstrologyBody.Mercury -> "Mer"
    AstrologyBody.Venus -> "Ven"; AstrologyBody.Mars -> "Mar"; AstrologyBody.Jupiter -> "Jup"
    AstrologyBody.Saturn -> "Sat"; AstrologyBody.Uranus -> "Ura"; AstrologyBody.Neptune -> "Nep"
    AstrologyBody.Pluto -> "Plu"; AstrologyBody.NorthNode -> "NN"; AstrologyBody.SouthNode -> "SN"
    AstrologyBody.Chiron -> "Chi"; AstrologyBody.Lilith -> "Lil"
    AstrologyBody.Ascendant -> "Asc"; AstrologyBody.Midheaven -> "MC"
}

private fun bodyColor(body: AstrologyBody): Color = when (body) {
    AstrologyBody.Sun -> Color(0xFFF7B500); AstrologyBody.Moon -> Color(0xFF8FC7FF)
    AstrologyBody.Mercury -> Color(0xFF4FC3B3); AstrologyBody.Venus -> Color(0xFFE48FB1)
    AstrologyBody.Mars -> Color(0xFFFF8A65); AstrologyBody.Jupiter -> Color(0xFF7E57C2)
    AstrologyBody.Saturn -> Color(0xFF90A4AE); AstrologyBody.Uranus -> Color(0xFF4DB6AC)
    AstrologyBody.Neptune -> Color(0xFF64B5F6); AstrologyBody.Pluto -> Color(0xFF9575CD)
    AstrologyBody.NorthNode -> Color(0xFF81C784); AstrologyBody.SouthNode -> Color(0xFFEF9A9A)
    AstrologyBody.Chiron -> Color(0xFFA1887F); AstrologyBody.Lilith -> Color(0xFFBA68C8)
    AstrologyBody.Ascendant -> Color(0xFF26A69A); AstrologyBody.Midheaven -> Color(0xFFFFB74D)
}

private fun placementAngle(placement: ChartPlacement): Float =
    (ZodiacSign.entries.indexOf(placement.sign).coerceAtLeast(0) * 30f) + placement.degreeInSign.toFloat()

private fun formatDegree(value: Double): String = String.format(Locale.US, "%.1f°", value)

private fun houseSystemDescription(houseSystem: HouseSystem): String = when (houseSystem) {
    HouseSystem.Placidus -> "Default time-based division used by many readers."
    HouseSystem.WholeSign -> "Each sign becomes a full house."
    HouseSystem.Koch -> "Dynamic time-based segmentation with a different emphasis curve."
    HouseSystem.Equal -> "Twelve equal slices from the ascendant."
    HouseSystem.Campanus -> "Spatial division focused on local horizon geometry."
    HouseSystem.Porphyry -> "Divides the quadrant into three equal parts."
    HouseSystem.Regiomontanus -> "Traditional angular house division."
}
