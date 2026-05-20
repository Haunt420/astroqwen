#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# scripts/compile.sh
# Local compile helper — mirrors the GitHub Actions "Compile check" workflow.
#
# Usage:
#   ./scripts/compile.sh              # compile only (default)
#   ./scripts/compile.sh --lint       # compile + lint
#   ./scripts/compile.sh --tests      # compile + unit tests
#   ./scripts/compile.sh --all        # compile + lint + tests
#   ./scripts/compile.sh --clean      # clean build dir first, then compile
#
# Requirements:
#   • JDK 17 on PATH  (java -version should report 17.x)
#   • ANDROID_HOME set, or Android Studio SDK installed at default location
#   • Run from the repo root: cd /sdcard/codex/astroqwen && ./scripts/compile.sh
# ─────────────────────────────────────────────────────────────────────────────

set -euo pipefail

# ── Colours ──────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'

info()    { echo -e "${CYAN}${BOLD}[INFO]${RESET}  $*"; }
success() { echo -e "${GREEN}${BOLD}[PASS]${RESET}  $*"; }
warn()    { echo -e "${YELLOW}${BOLD}[WARN]${RESET}  $*"; }
fail()    { echo -e "${RED}${BOLD}[FAIL]${RESET}  $*"; exit 1; }

# ── Parse flags ──────────────────────────────────────────────────────────────
RUN_LINT=false
RUN_TESTS=false
RUN_CLEAN=false

for arg in "$@"; do
  case "$arg" in
    --lint)   RUN_LINT=true ;;
    --tests)  RUN_TESTS=true ;;
    --all)    RUN_LINT=true; RUN_TESTS=true ;;
    --clean)  RUN_CLEAN=true ;;
    --help|-h)
      echo "Usage: $0 [--lint] [--tests] [--all] [--clean]"
      exit 0
      ;;
    *) warn "Unknown flag: $arg (ignored)" ;;
  esac
done

# ── Locate repo root ─────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT"
info "Repo root: $REPO_ROOT"

# ── Check JDK ────────────────────────────────────────────────────────────────
if ! command -v java &>/dev/null; then
  fail "java not found on PATH. Install JDK 17 and re-run."
fi

JAVA_VER=$(java -version 2>&1 | head -1 | grep -oP '(?<=version ")([^"]+)')
JAVA_MAJOR=$(echo "$JAVA_VER" | cut -d. -f1)
if [[ "$JAVA_MAJOR" != "17" ]]; then
  warn "JDK 17 expected, found $JAVA_VER. Build may still work but AGP 9.x prefers 17."
fi
info "Java: $JAVA_VER"

# ── Check ANDROID_HOME ───────────────────────────────────────────────────────
if [[ -z "${ANDROID_HOME:-}" ]]; then
  for candidate in \
      "$HOME/Android/Sdk" \
      "$HOME/Library/Android/sdk" \
      "/opt/android-sdk" \
      "/usr/local/lib/android/sdk"; do
    if [[ -d "$candidate" ]]; then
      export ANDROID_HOME="$candidate"
      break
    fi
  done
fi

if [[ -z "${ANDROID_HOME:-}" ]]; then
  fail "ANDROID_HOME is not set and no SDK found at default paths.\nSet it with: export ANDROID_HOME=/path/to/sdk"
fi
info "Android SDK: $ANDROID_HOME"

# ── Check gradlew ────────────────────────────────────────────────────────────
if [[ ! -f gradlew ]]; then
  fail "gradlew not found. Run from the repo root: cd $REPO_ROOT"
fi
chmod +x gradlew

# ── Regenerate gradle-wrapper.jar if absent ───────────────────────────────────
if [[ ! -f gradle/wrapper/gradle-wrapper.jar ]]; then
  warn "gradle-wrapper.jar is missing."
  if command -v gradle &>/dev/null; then
    info "Regenerating via system gradle..."
    gradle wrapper --gradle-version 9.3.1 --distribution-type bin
    success "gradle-wrapper.jar regenerated."
  else
    fail "gradle-wrapper.jar missing and no system 'gradle' found to regenerate it.\n\
Download it from:\n\
  https://github.com/gradle/gradle/raw/v9.3.1/gradle/wrapper/gradle-wrapper.jar\n\
Place it at: gradle/wrapper/gradle-wrapper.jar"
  fi
fi

# ── Gradle JVM flags ─────────────────────────────────────────────────────────
export GRADLE_OPTS="${GRADLE_OPTS:-} -Dorg.gradle.jvmargs=-Xmx4g"

# ── Optional clean ────────────────────────────────────────────────────────────
if [[ "$RUN_CLEAN" == true ]]; then
  info "Cleaning build directories..."
  ./gradlew clean --no-daemon --quiet
  success "Clean complete."
fi

# ── Compile ───────────────────────────────────────────────────────────────────
echo ""
info "▶  Compiling debug APK..."
echo ""

START_TIME=$(date +%s)

if ./gradlew assembleDebug \
    --no-daemon \
    --stacktrace \
    --warning-mode all; then
  END_TIME=$(date +%s)
  ELAPSED=$((END_TIME - START_TIME))
  echo ""
  success "assembleDebug passed in ${ELAPSED}s"
else
  echo ""
  fail "assembleDebug failed. See output above for errors."
fi

# ── APK location ──────────────────────────────────────────────────────────────
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [[ -f "$APK_PATH" ]]; then
  APK_SIZE=$(du -sh "$APK_PATH" | cut -f1)
  success "APK: $REPO_ROOT/$APK_PATH  ($APK_SIZE)"
fi

# ── Optional lint ─────────────────────────────────────────────────────────────
if [[ "$RUN_LINT" == true ]]; then
  echo ""
  info "▶  Running lint..."
  echo ""
  if ./gradlew lintDebug --no-daemon --continue; then
    success "Lint passed."
  else
    warn "Lint reported issues. See app/build/reports/lint-results-debug.html"
  fi
fi

# ── Optional unit tests ───────────────────────────────────────────────────────
if [[ "$RUN_TESTS" == true ]]; then
  echo ""
  info "▶  Running unit tests..."
  echo ""
  if ./gradlew testDebugUnitTest --no-daemon --continue; then
    success "Unit tests passed."
  else
    warn "Unit tests failed. See app/build/reports/tests/testDebugUnitTest/"
  fi
fi

# ── Summary ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}────────────────────────────────────────${RESET}"
echo -e "${BOLD}  StellarPath local compile summary${RESET}"
echo -e "${BOLD}────────────────────────────────────────${RESET}"
echo -e "  Compile   ${GREEN}✅ passed${RESET}"
[[ "$RUN_LINT"  == true ]] && echo -e "  Lint      ${YELLOW}see report${RESET}"
[[ "$RUN_TESTS" == true ]] && echo -e "  Tests     ${YELLOW}see report${RESET}"
echo ""
