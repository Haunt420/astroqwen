# Astroqwen Handoff

Last updated: 2026-05-20

This repo is an Android Compose port of Astro Future. Treat `astro.txt` as the product spec, not a log file. It is the strongest statement of desired behavior in the workspace.

## Product intent

The target app is a polished astrology product with:

- Accurate natal, transit, and synastry calculations.
- Multiple birth profiles with persisted preferences.
- Interactive chart rendering and time scrubbing.
- Interpretation lists and drill-down detail screens.
- UX close to the iOS Astro Future app.
- No synthetic chart data in the final product.

Accuracy is non-negotiable. The spec explicitly asks for a real ephemeris backend, not approximations.

## What `astro.txt` says to optimize for

- Real ephemeris-backed chart generation.
- Extended range support, roughly 1900 to 2050.
- Smooth scrubbing, throttled background recalculation, and cached results.
- Tappable chart wheel elements and interpretation lists.
- Profiles, settings, and backup/restore.
- Testing against known data sets for accuracy.

## Current repo shape

- Single-module Android app.
- Jetpack Compose UI with Material3.
- Main entry flow is `MainActivity -> StellarPathApp -> AppNavGraph`.
- DataStore and Room infrastructure exist.
- The app is still sample-backed for most meaningful astrology outputs, but the Swiss Ephemeris bootstrap work has now started.

## Current ephemeris work in progress

The calculation layer is being moved to real Swiss Ephemeris data.

What is already in the workspace:

- `app/libs/jswisseph-core.jar`
- `app/src/main/assets/swisseph/ephem/`

What this means:

- We have a real ephemeris library and the required data files staged locally.
- The remaining work is to wrap that library behind an app-facing engine, extract the assets to internal storage at runtime, and route the chart/transit/synastry logic through that engine.
- Do not reintroduce synthetic chart generation once the engine is wired.
- The engine boundary now exists in code, and `SampleAstroData` has been turned into an adapter to that engine.

## What has already been intentionally removed

These should stay out unless explicitly requested later:

- Widget support.
- Paywall/premium UI and premium route surfaces.
- Active share and PDF export UI.

There is still dormant support code for sharing, but it is not wired into the product surface. That is intentional and low-cost to keep as a future hook.

## Important current implementation facts

- `StellarPathAppState` still reads from `SampleAstroData`.
- `SampleAstroData` still generates the chart/transit/compatibility outputs with hardcoded values and simple date math.
- `StellarPathApplication` seeds Room from `SampleAstroData`.
- Room and DataStore exist, but they are not yet the source of truth for the UI.
- Most screens in `AppScreens.kt` are wired to the sample-backed app state.
- The transit timeline is still sample-driven and will need a separate real-data pass after the core ephemeris layer lands.

## Files to know first

- `app/src/main/java/com/stellarpath/android/StellarPathApp.kt`
- `app/src/main/java/com/stellarpath/android/StellarPathApplication.kt`
- `app/src/main/java/com/stellarpath/android/ui/state/StellarPathAppState.kt`
- `app/src/main/java/com/stellarpath/android/data/SampleAstroData.kt`
- `app/src/main/java/com/stellarpath/android/navigation/AppNavGraph.kt`
- `app/src/main/java/com/stellarpath/android/navigation/Routes.kt`
- `app/src/main/java/com/stellarpath/android/ui/screens/AppScreens.kt`
- `app/src/main/java/com/stellarpath/android/data/repository/RoomProfileRepository.kt`
- `app/src/main/java/com/stellarpath/android/data/repository/DataStoreSettingsRepository.kt`
- `app/src/main/java/com/stellarpath/android/data/local/StellarPathDatabase.kt`
- `app/src/main/java/com/stellarpath/android/util/ShareUtils.kt`
- `app/src/main/java/com/stellarpath/android/worker/DailyHoroscopeWorker.kt`
- `app/src/main/java/com/stellarpath/android/model/*`
- `.github/workflows/compile-check.yml`
- `.github/workflows/android-build.yml`

## Current cleanup status

Already completed in this adoption pass:

- Fixed compile blockers from the build log:
  - Added `androidx.compose.ui:ui-text`.
  - Imported `drawText`.
  - Replaced the bad `matchParentSize` usage in the chart canvas.
- Removed premium/paywall UI and routing.
- Removed widget files and widget dependencies.
- Removed share/export buttons and copy from report preview screens.
- Removed the subscription model layer and premium flags from the core data classes.
- Left the share helper and daily horoscope worker as dormant support code, not product surface.
- Staged Swiss Ephemeris sources and asset data locally so the engine can be wired without another dependency hunt.
- Added a local-only `local.properties` with `sdk.dir=/opt/android-sdk` so Gradle can resolve the Android SDK in this workspace.

## Remaining high-priority work

1. Make Room and DataStore the source of truth for UI state.
2. Persist profile CRUD and default profile selection.
3. Replace the remaining sample transit-window markers with real timing events or a narrower, explicit placeholder.
4. Add tests for calculation accuracy and core flows.
5. Decide whether any dormant sharing/export hooks should stay in the tree or be removed entirely.

## Build and verification environment

The local container is usable, but it is not a complete Android build host by default.

What I installed locally:

- JDK 17 at `/usr/lib/jvm/java-17-openjdk-arm64`
- Android SDK at `/opt/android-sdk`
- `platform-tools`
- `platforms;android-35`
- `build-tools;35.0.0`
- `build-tools;36.0.0`
- Android cmdline tools at `/opt/android-sdk/cmdline-tools/latest`

Local build command that was used:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
export ANDROID_SDK_ROOT=/opt/android-sdk
export PATH=/opt/android-sdk/platform-tools:/opt/android-sdk/cmdline-tools/latest/bin:$PATH
bash ./gradlew assembleDebug --no-daemon --stacktrace
```

Important caveat:

- This container is `aarch64`.
- The Android AAPT2 binary Gradle uses here is `x86-64`.
- The build therefore fails in `processDebugResources` on this host, even after the code compiles.
- CI or an amd64 host is the correct final verification target.
- If you need a reproducible local build, use an amd64 Linux container or VM with the same JDK 17 and Android SDK pieces. This host is fine for Kotlin edits and logic work, but it is not a full Android resource-build environment.
- Current local verification status:
  - `bash ./gradlew :app:compileDebugKotlin --no-daemon --stacktrace` passes.
  - `bash ./gradlew :app:assembleDebug --no-daemon --stacktrace` still fails in `processDebugResources` because AAPT2 cannot start on this arm64 host.
  - The failure is the same class of host/toolchain mismatch already noted above, not an application compile error.

## Build failure pattern to recognize

If you see errors like:

- `AAPT2 ... Daemon startup failed`
- `No such file or directory` for the `aapt2` binary

that is an environment mismatch, not an app logic bug.

## Git and workspace notes

- `git` may complain about dubious ownership for `/sdcard/codex/astroqwen`.
- If needed, run:

```bash
git config --global --add safe.directory /sdcard/codex/astroqwen
```

- In this workspace, `bash ./gradlew` was more reliable than relying on the wrapper being executable directly.

## Do not reintroduce

- Premium or paywall gates.
- Widgets.
- Fake share/export behavior.
- Synthetic astro calculations as a final product path.

## Suggested next implementation slice

Start with the calculation layer, not the UI:

1. Define a real ephemeris engine interface.
2. Move synthetic sample calculations behind a temporary adapter.
3. Replace chart/transit/synastry generation with ephemeris-backed results.
4. Only then wire the state layer to real persistence and flows.

Current slice status:

- Engine interface and implementation are now present.
- `SampleAstroData` is the adapter boundary for chart, transit, reading, and compatibility generation.
- Keep the remaining transit-window work separate unless there is time to make it real in the same pass.

## Source files that describe the target product

- `astro.txt` is the product brief.
- `clawloggy.txt` is the recent build failure log that led to the compile fixes.
