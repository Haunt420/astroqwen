~}# Before responding, rewrite the following prompt to eliminate ambiguity and surface any implicit assumptions\. Then, with that refined version as your foundation, consider: second\-order implications, cross\-domain connections, and latent nuances that separate a surface answer from a genuinely thorough one\. Then respond with that depth, structured for clarity\.
Prompt: "Create as perfect a copy of the iOS app 'AstroFuture' as you possibly can, in form and function\."

- Exported: 2026-05-10T01:30:11.652Z
- Thread ID: 019e0f3f-8239-7ef3-a377-384ef6d63008

---

## USER

Make it for android, not iOS

## ASSISTANT

Use this Android-specific version:

> Create an original Android app that matches the reference apps user value, interaction quality, and overall polish as closely as possible, using AstroFuture only as a behavioral benchmark. Do not reuse proprietary code, brand assets, copy, screenshots, or exact UI compositions. Produce a feature-parity map, screen inventory, interaction/state model, design system, and implementation plan for Android. Explicitly define scope, non-goals, target form factors, onboarding inputs, personalization logic, notifications, subscriptions, edge cases, accessibility, localization, privacy, and Google Play constraints. Assume a modern Android stack unless otherwise specified, with Kotlin, Jetpack Compose, Material 3, adaptive layouts, and Android-native navigation patterns. Where the reference behavior is not directly observable, state the assumption and choose the most defensible Android-native solution.

If you want, I can take this one step further and turn it into a full Android build spec for:
1. a feature list
2. a screen-by-screen UX spec
3. a Kotlin/Compose implementation plan

## USER

3

## ASSISTANT

Im turning the Android version into a concrete Kotlin/Compose execution plan now. Im assuming this is an astrology-style personalized future-reading app, and Ill keep the scope original rather than a literal clone.

## USER

No, as close to AstroFuture in function and in form as is legally permissable for you.

## ASSISTANT

The closest safe Android equivalent is a chart instrument with editorial overlays, not a generic horoscope feed. Based on AstroFutures public App Store and site pages, the build should preserve the observable interaction grammar and feature breadth, while changing the identity layer enough to avoid copying branding, artwork, exact layouts, or text.

**Implementation Plan**

**1. Fidelity Targets**
- Mirror the product shape: natal charts, tappable aspects, list-and-chart dual reading modes, transits, synastry, love compatibility, special events, widgets, PDF reports, and custom house systems.
- Mirror the interaction rhythm: tap-to-reveal detail, future scrubbing, multi-profile support, and concise explanatory copy.
- Diverge on protected identity: new app name, icon, color system, typography pairings, illustration style, and all prose.

**2. Stack**
- Use `Kotlin`, `Jetpack Compose`, `Material 3`, `Navigation Compose`, `Hilt`, `Room`, `DataStore`, `WorkManager`, and `Play Billing`.
- Add `Glance` for home-screen widgets and `Wear OS` only if you want the nearest analogue to Apple Watch support.
- Keep calculations behind a deterministic domain layer; the UI should render from chart state, not compute chart state.
- Avoid mandatory login. Use local profiles by default and optional sync later if you need it.

**3. Module Layout**
- `app`
- `core-domain`
- `core-data`
- `core-ui`
- `feature-onboarding`
- `feature-home`
- `feature-chart`
- `feature-transits`
- `feature-compatibility`
- `feature-reports`
- `feature-library`
- `feature-settings`
- `feature-widget`
- `feature-wear`

**4. Screen Inventory**
- Onboarding: collect birth date, exact time, place, nickname, and an explicit unknown-time fallback.
- Home: show todays reading, a chart preview, next event, and quick access to saved profiles.
- Natal Chart: make the wheel the centerpiece, with tap-to-explain aspects and a switch to list mode.
- Transits: show a future timeline, a zoomable solar-system/orbit view, and event markers for retrogrades, eclipses, solstices, and equinoxes.
- Compatibility: support two-profile synastry, plus the three-part breakdown that maps cleanly to element match, harmony, and passion.
- Reports: generate and share PDFs and snapshot cards.
- Library: include elements, qualities, dualities, aspects, house systems, and special events.
- Settings: house system, notifications, privacy, theme, subscription, export, and data reset.
- Saved Profiles: manage family, friends, and crushes as first-class entities.

**5. Compose Implementation Notes**
- Render the chart wheel in a dedicated `Canvas` composable with cached geometry and hit-testing.
- Use a `ModalBottomSheet` for aspect explanations and detailed readings.
- Keep chart and list modes in sync so selecting one highlights the same item in the other.
- Use `LazyColumn` for the reading list and `Slider` or timeline chips for transit scrubbing.
- Reuse the same domain model for screen rendering, share-card rendering, and PDF generation.
- Keep state in the ViewModel and drive the UI with immutable `UiState`.

**6. Data and Logic**
- Model the core entities as `BirthProfile`, `ChartSnapshot`, `Aspect`, `TransitWindow`, `CompatibilityReport`, `InterpretationBlock`, and `SubscriptionEntitlement`.
- Make chart math and event detection pure and testable.
- Handle timezone/DST and unknown birth time explicitly.
- Store local data encrypted and keep raw birth details out of logs and analytics.
- Version interpretation content separately from code so text can change without an app update.
- Keep the app usable offline after the first sync.

**7. Monetization**
- Free tier: basic natal chart, today view, limited transit horizon, and a lightweight compatibility preview.
- Premium: extended interpretations, unlimited compatibility, time extension, special events, custom house systems, and richer PDF/report export.
- Use `Play Billing` with server-side entitlement verification and purchase restore.
- Make cancellation and renewal behavior explicit in settings and paywall copy.

**8. Platform Surfaces**
- Ship widgets early. They are a major part of parity, not a nice-to-have.
- Add a Wear OS companion if you want a watch-equivalent surface for todays event or daily reading.
- Use adaptive layouts: single-pane on phones, two-pane on tablets and foldables, persistent chart plus details in landscape.
- Keep notifications user-controlled: daily horoscope reminders, transit alerts, and special-event alerts.

**9. Quality Gates**
- Use plainspoken, non-trendy copy. The tone should feel clear and explanatory.
- Make every aspect and chart element accessible with screen reader labels.
- Test unknown time, missing location, offline mode, subscription restore, and timezone edge cases.
- Add screenshot/golden tests for chart states, compatibility states, and report layouts.
- Keep chart interactions smooth on midrange devices; the chart should feel responsive, not decorative.
- Show the entertainment-only disclaimer without making onboarding feel bureaucratic.

**10. Build Order**
- Phase 0: build a parity matrix from public references and define every observable screen and action.
- Phase 1: scaffold app shell, profile storage, navigation, and theme.
- Phase 2: implement natal chart rendering, aspect hit-testing, and the list/chart dual view.
- Phase 3: add transits, solar-system animation, compatibility, and reports.
- Phase 4: add widgets, Wear OS, notifications, and adaptive layouts.
- Phase 5: wire premium, analytics/crash reporting, accessibility hardening, and Play release polish.

**What Id Preserve Closely**
- The chart-first hierarchy.
- The tap-to-reveal aspect behavior.
- The list-vs-visual reading pattern.
- The ability to manage many people/profiles.
- The compatibility breakdown into distinct explanatory dimensions.
- The clear, explanatory tone.

**What Id Change Deliberately**
- The name, icon, and brand system.
- Exact spacing, typography, and color values.
- Exact copy and labels.
- Any illustrative art or chart styling that could create confusion with the original.

Public references used for this plan:
- [Astro Future App Store listing](https://apps.apple.com/us/app/astro-future-daily-horoscope/id1119123378)
- [Astro Future official site](https://www.astro-future.com/)
- [Love Compatibility page](https://www.astro-future.com/love-compatibility)
- [Elements, Qualities & Dualities](https://www.astro-future.com/elements-qualities-dualities)
- [Subscription Terms](https://www.astro-future.com/subscription-terms)
- [Privacy Policy](https://www.astro-future.com/privacy-policy)

If you want, I can turn this into:
1. a screen-by-screen PRD with acceptance criteria
2. a Kotlin/Compose package tree and starter class scaffold

## USER

1

## ASSISTANT

Im going to turn the product into a concrete PRD now: first Ill inspect the workspace so the spec matches the actual Android app structure, then Ill write a screen-by-screen document with acceptance criteria and clear non-goals.

## ASSISTANT

The workspace isnt a Git repository, so Im treating this as a plain file tree and checking whats already there before I add the PRD. That will keep the document aligned with the actual project shape instead of inventing structure.

## ASSISTANT

I have enough source detail to draft the PRD. Im writing it as a practical Android build spec with screen-level acceptance criteria, plus explicit parity boundaries so it stays as close as possible without copying protected identity.

## SYSTEM

## ASSISTANT

The PRD is in place. Im doing one pass over the document now to catch structural issues and make sure the screen coverage and acceptance criteria are tight enough to hand to implementation without a second translation layer.

## ASSISTANT

Created the PRD at [ASTROFUTURE_ANDROID_PRD.md](/sdcard/git/androidfuture/ASTROFUTURE_ANDROID_PRD.md).

It includes:
- a screen-by-screen Android spec
- acceptance criteria for each screen
- product constraints and non-goals
- parity mapping to the public Astro Future feature set
- non-functional requirements, stack, and delivery phases

If you want the next step, I can turn this into:
1. a Jetpack Compose package/class scaffold
2. a data model and navigation graph
3. an implementation checklist with milestones

## USER

1 and 2

## ASSISTANT

Im going to lay down the Android skeleton now: a minimal Compose project structure, the core domain models, and a navigation graph that matches the PRD screens. Since the workspace is empty, Ill create the project files from scratch rather than pretending an existing app structure is there.

## ASSISTANT

Ive pinned the Android toolchain versions from official docs, so the scaffold can use current stable Compose and AGP versions instead of placeholders. Next Im creating the project files, then Ill see whether the local environment can generate a Gradle wrapper cleanly.

## ASSISTANT

Im adding the project skeleton first: Gradle setup, manifest, and the minimal resource/theme shell so the Compose code has a place to live. After that Ill add the Kotlin packages for models, navigation, and placeholder screens.

## SYSTEM

## ASSISTANT

Im writing the Kotlin side now: the app entrypoint, a clean domain model split, the route map, and placeholder screens wired through the nav host. The goal is for the structure to read like a real app, even though the feature bodies are still scaffolds.

## SYSTEM

## ASSISTANT

The scaffold is in place. Im doing a quick pass on the new files now to catch any obvious route or import mistakes before I hand it back, since empty-project setup is where small mismatches usually hide.

## ASSISTANT

Ive got the structure in place. Im checking the build and theme files for any version or syntax mismatches now, since those are the only things likely to block a clean import in Android Studio.

## ASSISTANT

I caught one small ambiguity in the manifest resolution path. Im tightening that so the activity and application class names resolve cleanly regardless of how the project is imported.

## SYSTEM

## ASSISTANT

Im filling the two obvious model gaps now: reference-library content and user settings. That keeps the scaffold aligned with the screens we already added, instead of leaving those areas as loose placeholders.

## SYSTEM

## ASSISTANT

Im tightening one privacy default before I hand this off: the scaffold is meant to treat birth data as sensitive, so Im disabling automatic Android backups by default.

## SYSTEM

## ASSISTANT

Added the Android scaffold and the core domain/nav skeleton under `StellarPath`.

**Whats in place**
- Project setup: [settings.gradle.kts](/sdcard/git/androidfuture/settings.gradle.kts), [build.gradle.kts](/sdcard/git/androidfuture/build.gradle.kts), [gradle.properties](/sdcard/git/androidfuture/gradle.properties), [app/build.gradle.kts](/sdcard/git/androidfuture/app/build.gradle.kts)
- App shell: [app/src/main/AndroidManifest.xml](/sdcard/git/androidfuture/app/src/main/AndroidManifest.xml), [app/src/main/java/com/stellarpath/android/StellarPathApplication.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/StellarPathApplication.kt), [app/src/main/java/com/stellarpath/android/MainActivity.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/MainActivity.kt), [app/src/main/java/com/stellarpath/android/StellarPathApp.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/StellarPathApp.kt)
- Navigation: [app/src/main/java/com/stellarpath/android/navigation/Routes.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/navigation/Routes.kt), [app/src/main/java/com/stellarpath/android/navigation/AppNavGraph.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/navigation/AppNavGraph.kt)
- Data models: [app/src/main/java/com/stellarpath/android/model/ProfileModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/ProfileModels.kt), [app/src/main/java/com/stellarpath/android/model/ChartModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/ChartModels.kt), [app/src/main/java/com/stellarpath/android/model/ReadingModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/ReadingModels.kt), [app/src/main/java/com/stellarpath/android/model/CompatibilityModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/CompatibilityModels.kt), [app/src/main/java/com/stellarpath/android/model/ReferenceModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/ReferenceModels.kt), [app/src/main/java/com/stellarpath/android/model/SettingsModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/SettingsModels.kt), [app/src/main/java/com/stellarpath/android/model/SubscriptionModels.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/model/SubscriptionModels.kt)
- Placeholder UI and theme: [app/src/main/java/com/stellarpath/android/ui/screens/PlaceholderScreens.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/ui/screens/PlaceholderScreens.kt), [app/src/main/java/com/stellarpath/android/ui/theme/Color.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/ui/theme/Color.kt), [app/src/main/java/com/stellarpath/android/ui/theme/Type.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/ui/theme/Type.kt), [app/src/main/java/com/stellarpath/android/ui/theme/Theme.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/ui/theme/Theme.kt)

**What the scaffold covers**
- Top-level routes for splash, onboarding, home, chart, transits, compatibility, profiles, reports, library, settings, and paywall
- Detail routes for profile detail/editor, report preview, library detail, and event detail
- Domain coverage for birth data, chart anatomy, readings, transits, compatibility, reference topics, settings, and subscriptions
- Privacy-aware manifest defaults, including `android:allowBackup="false"`

**Notes**
- I did not run a Gradle build here because `gradle` is not installed in this workspace.
- I used current official Android/Kotlin docs to pin the versions in the scaffold.

**Sources used**
- [Jetpack Compose setup and dependencies](https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler)
- [Navigation with Compose](https://developer.android.com/develop/ui/compose/navigation)
- [Android Gradle Plugin 9.1.1 release notes](https://developer.android.com/build/releases/agp-9-1-0-release-notes)
- [Kotlin release process](https://kotlinlang.org/docs/releases.html)
- [Kotlin command-line compiler](https://kotlinlang.org/docs/command-line.html)

**Next**
1. Replace the placeholder screens with real Compose implementations, starting with `ChartScreen` and `TransitsScreen`.
2. Add persistence and state management (`Room` and `DataStore`) plus sample chart generation.

## USER

Continue

## ASSISTANT

Im moving past the scaffold now: first Ill verify the dependency versions so the build file isnt carrying placeholder numbers, then Ill flesh out the app shell into something runnable with real state and navigation behavior.

## ASSISTANT

Ive got the next slice scoped: Im replacing the placeholders with real Compose screens and shared app state, plus correcting the dependency versions to the current stable AndroidX/Kotlin line. That should turn the project from structure only into a believable app shell.

## SYSTEM

## ASSISTANT

Im adding the shared sample catalog and app state first so the screens can read realistic chart, reading, transit, and compatibility data instead of hardcoded placeholders. That keeps the UI changes cohesive instead of one-off.

## SYSTEM

## SYSTEM

## SYSTEM

## ASSISTANT

I caught a wiring issue while connecting app state to the theme: the theme decision needs to flow from the settings, otherwise the new state is unused. Im fixing that as part of the next patch.

## SYSTEM

## ASSISTANT

Im filling one missing state accessor before I wire the library screens: the reference section list needs to be reachable from the shared app state, not only by individual topic lookup.

## SYSTEM

## ASSISTANT

Im replacing the placeholder screen file with real content now. This is the biggest slice so far: shared profile state, an actual natal chart wheel, transit scrubbing, compatibility breakdowns, profile management, settings, and reference content.

## SYSTEM

## SYSTEM

## ASSISTANT

Ive got the real screens in, and Im doing a compile-minded pass now. The risky part is the chart wheel math and a few state/geometry references, so Im checking those before I move on.

## ASSISTANT

I found the two real compile risks in the new UI layer: the chart canvas was reading composable theme values from inside a draw lambda, and one overlay position used an undefined `size` value. Im fixing both now, plus tightening the premium gating check.

## ASSISTANT

The previous patch missed the current import order, so Im re-reading the top of the file and then applying a tighter patch. I only need to fix the actual compile blockers, not churn the rest.

## SYSTEM

## SYSTEM

## SYSTEM

## ASSISTANT

The app code is now materially closer to a runnable Android shell. Im doing one last hygiene pass on the app-state wiring and top-level files, and then Ill summarize what changed and what still needs a build toolchain.

## ASSISTANT

Im checking whether a usable Gradle wrapper already exists somewhere on disk before I spend time recreating it. If there isnt one, Ill at least pin the wrapper properties so the project points at the right Gradle line.

## ASSISTANT

Im checking the remaining compile-risk areas now: screen code, dependency versions, and whether a usable Gradle wrapper exists. After that Ill patch anything blocking a clean build and report the exact state.

## ASSISTANT

Ive got the main screen file loaded. Next Im checking the lower half for any compile traps in the chart/composable helpers, then Ill align the build versions with the latest official Android/Kotlin releases.

## ASSISTANT

I found at least one likely compile break already: the screen file uses `Surface` heavily but the import is missing. Im validating the rest of the file and then Ill patch the minimal set needed to get the project into a buildable state.

## ASSISTANT

Ive confirmed a couple of concrete gaps in the screen file: `Surface` isnt imported, and `Modifier.weight(...)` is used multiple times without the `weight` layout import. Im patching those, then Ill align the build versions and add a wrapper if the environment can support it.

## ASSISTANT

The dependency versions are straightforward to update and the official docs confirm the newer releases Im targeting. Im now checking whether I can bootstrap a Gradle wrapper locally; if not, Ill leave the code build-ready and call out that the environment lacks a build tool.

## ASSISTANT

I checked the subscription model. Theres already a `DetailedCompatibility` entitlement, so I can make the compatibility screen use the paywall callback in a way that matches the feature model instead of leaving it unused.

## ASSISTANT

The code changes are clear now. Im applying the import and compatibility-screen fixes first, then Ill try to bootstrap a wrapper only if the environment already has the pieces I need.

## SYSTEM

## SYSTEM

## SYSTEM

## ASSISTANT

The code-level blockers are patched. Im checking whether a Gradle wrapper can be sourced locally so I can actually validate the project instead of only reasoning about it.

## ASSISTANT

I found a local Gradle wrapper source in another workspace and the current AGP release expects Gradle 9.3.1. Im copying a working wrapper bootstrap into this repo and pointing it at the compatible distribution, which should let the project actually run builds here.

## SYSTEM

## USER

Continue plz

## ASSISTANT

Im continuing from the current scaffold by validating it end-to-end with Gradle now that the wrapper is in place, then Ill fix any concrete build errors that show up.

## ASSISTANT

The wrapper script exists but isnt executable in this workspace; Im fixing permissions and rerunning the toolchain check.

## ASSISTANT

This storage mount wont honor execute bits, so Im invoking the wrapper through `bash` directly to continue validation.

## ASSISTANT

Gradle is now reachable, but the environment has no Java runtime configured. Im locating any installed JDK on this machine and wiring `JAVA_HOME` so we can run builds.

## ASSISTANT

I cant execute Android builds yet because this environment has no JDK, so Im continuing by advancing implementation in code: Ill add a concrete local data layer (`Room` + `DataStore` scaffolding) and wire app state through a repository boundary.

## ASSISTANT

Im now adding concrete persistence scaffolding files and Gradle deps for `Room` and `DataStore`; this will keep the current demo UI intact while preparing a real storage-backed path.

## SYSTEM

## ASSISTANT

Im applying a focused set of edits now: new `Room` entities/DAO/database, `DataStore` settings repository, repository interfaces, and application-level initialization with sample-profile seeding.

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## SYSTEM

## ASSISTANT

Im wiring the new repositories into `Application` now and seeding initial profiles asynchronously so the persisted store has data on first launch.

## SYSTEM

## ASSISTANT

Ive finished the persistence scaffolding and app initialization wiring; next Im doing a quick consistency pass over imports/dependencies and then Ill summarize exactly whats ready versus what still needs a JDK build pass.

## ASSISTANT

Continued and moved the project from UI-only scaffold toward real local persistence.

**What I changed**
- Added `Room + DataStore + Lifecycle` dependencies and `kapt` plugin in [app/build.gradle.kts](/sdcard/git/androidfuture/app/build.gradle.kts).
- Updated top-level Kotlin plugin versions to `2.3.21` in [build.gradle.kts](/sdcard/git/androidfuture/build.gradle.kts).
- Added Gradle wrapper files and pinned distribution to `9.3.1` in [gradle/wrapper/gradle-wrapper.properties](/sdcard/git/androidfuture/gradle/wrapper/gradle-wrapper.properties), plus `gradlew`/`gradlew.bat`.
- Fixed screen compile blockers and wired premium CTA in compatibility flow in [AppScreens.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/ui/screens/AppScreens.kt):
  - added missing `Surface` and `weight` imports
  - used `onOpenPaywall` via a premium banner when compatibility is locked
- Added Room local layer:
  - [ProfileEntity.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/local/ProfileEntity.kt)
  - [ProfileDao.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/local/ProfileDao.kt)
  - [RoomTypeConverters.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/local/RoomTypeConverters.kt)
  - [StellarPathDatabase.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/local/StellarPathDatabase.kt)
- Added repository layer:
  - [ProfileRepository.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/repository/ProfileRepository.kt)
  - [RoomProfileRepository.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/repository/RoomProfileRepository.kt)
  - [SettingsRepository.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/repository/SettingsRepository.kt)
  - [DataStoreSettingsRepository.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/repository/DataStoreSettingsRepository.kt)
  - [StellarPathAppContainer.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/data/repository/StellarPathAppContainer.kt)
- Wired app startup seeding through `Application` in [StellarPathApplication.kt](/sdcard/git/androidfuture/app/src/main/java/com/stellarpath/android/StellarPathApplication.kt).

**Validation status**
- I could not run Gradle tasks because this environment has no Java runtime (`JAVA_HOME` unset and no `java` in `PATH`).
- Wrapper invocation works via `bash gradlew ...` on this filesystem, but still requires Java.

**Sources used for versions**
- [Room releases](https://developer.android.com/jetpack/androidx/releases/room?hl=en)
- [DataStore releases](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Lifecycle releases](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [RoomDatabase.Builder API](https://developer.android.com/reference/kotlin/androidx/room/RoomDatabase.Builder)

1. If you want, I can next wire `StellarPathAppState` to these repositories so UI state is persistence-backed instead of sample-data-backed.
2. If you install/configure JDK 17, I can immediately run a full compile pass and fix any remaining build issues.

