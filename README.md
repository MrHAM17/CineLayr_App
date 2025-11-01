# CineLayr ![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-orange) ![Android Gradle Plugin](https://img.shields.io/badge/AGP-8.12.0-blue)
## Demo:
<table style="width:100%;"> 
  <tr> 
    <td align="center" style="width:50%;"> 
      <strong>App Tour
        <br>
        <em>1 min 48 sec
        </em>
      </strong>
      <br> 
      <img src="https://github.com/MrHAM17/CineLayr_App/blob/main/Rough%20Work%20%26%20Data/Output%20Data/Screen%20Recordings/v1.0.0%20GIFs/1%5D%20App%20Tour.gif" height="510" width="240"> 
  </td> 
    <td align="center" style="width:50%;"> 
      <strong>Theme-switch & Widget
        <br>
        <em>1 min 45 sec
        </em>
      </strong>
      <br> 
      <img src="https://github.com/MrHAM17/CineLayr_App/blob/main/Rough%20Work%20%26%20Data/Output%20Data/Screen%20Recordings/v1.0.0%20GIFs/2%5D%20Theme-switch%20%26%20Widget.gif" height="510" width="240"> </td> </tr> </table>

## Screenshots:
**Light Theme:**

![](https://github.com/MrHAM17/CineLayr_App/blob/main/Rough%20Work%20%26%20Data/Output%20Data/Screenshots/v1.0.0%20Slides/Slide%2001.png)
**Dark Theme:**

![](https://github.com/MrHAM17/CineLayr_App/blob/main/Rough%20Work%20%26%20Data/Output%20Data/Screenshots/v1.0.0%20Slides/Slide%2002.png)
**Notification & Widget Support:**

![](https://github.com/MrHAM17/CineLayr_App/blob/main/Rough%20Work%20%26%20Data/Output%20Data/Screenshots/v1.0.0%20Slides/Slide%2003.png)

## Table of contents

1. [Project Summary](#1-project-summary)
2. [Tech Stack](#2-tech-stack)
3. [Quick Start](#3-quick-start)
4. [Tests & Reports](#4-tests--reports)
5. [Macrobenchmark & Baseline Profile](#5-macrobenchmark--baseline-profile)
6. [Baseline Profile Results](#6-baseline-profile-results)
7. [CI Workflow](#7-ci-workflow)

---

## 1. Project Summary

CineLayr is a modular Android movie application built with Kotlin, Jetpack Compose & the Clean architecture.
It serves as a learning + portfolio project, demonstrating production-grade practices—Koin dependency injection, Room persistence, Ktor networking, WorkManager background sync, widget support, & performance optimization through macrobenchmarks & Baseline Profiles.

**Key Features:**

* **Modular:** With 11 modules — app, benchamrk, core:database, core:designsystem, core:navigation, core:network, data, domain, feature-trending, feature-details, feature-watchlist.
* **Trending:** Displays a paginated list of trending movies fetched via network + cached in Room DB.
* **Details:** Dedicated movie details screen with safe argument handling (movieId validation) & deep-link support.
* **Watchlist:** Displays user-saved movies using local Room persistence & ViewModel state management.
* **Theming:** Supports Light / Dark theme toggle managed through a ThemeViewModel.
* **App Widget:** Glance-based home-screen widget shows trending movie info & deep-links into details; updates daily via WorkManager.
* **Background Work:** WidgetUpdateWorker refreshes widget data periodically (every 24h) using Koin-injected repository.
* **Caching & Offline Support:** Room-backed cache & local Room persistence — ensures online-offline browsing.
* **Dependency Injection:** Async Koin initialization for app startup & Sync initialization for widgets, workers to avoid race conditions.
* **Build & Signing Config:** Benchmark build type inherits from release, signed with a real .jks keystore, includes baseline profile generation; ProGuard keeps reflection-based classes to avoid runtime crashes.
* **Performance Coverage:** Unit tests · Instrumented tests · Macrobenchmarks · Baseline Profile

---

## 2. Tech Stack


* **Language:** Kotlin + Jetpack Compose
* **Database:** Room DB
* **Architecture:** Clean Architecture + Ktor + Room + DI + Multi-Module + Compose Navigation
* **Dependency Injection:** Koin · Async init for app · Sync init for workers, widgets, deep-links
* **External API:** Movies from [TMDB.Org](https://www.themoviedb.org/settings/api).
* **Networking:** Ktor client  · With serialization & interceptors (injects API key from local.properties) + Logging + Caching enabled
* **Images:** Coil for Compose image · Memory + Disk caching
* **Background:** WorkManager for widget updates (startup + 24 hr sync)
* **Performance & Testing:** Unit tests · Instrumented tests · Macrobenchmarks tests + Baseline Profile
* **Continuous Integration:** GitHub Actions

---

## 3. Quick Start

**3.1 Clone the repo:**

```bash
git clone https://github.com/MrHAM17/CineLayr_App
cd cinelayr
```
Or download the ZIP file and extract it manually.

#
**3.2 API Key Integration:**

  * The app uses the **TMDB Movie API** for fetching movie data. It reads your API key from `local.properties` and injects it into requests via interceptor.
  * Get your key from 👉 [TMDB.Org](https://www.themoviedb.org/settings/api).
  * Add it to your local properties file (see example below under signing setup).
  * This key is automatically applied to every request at runtime — no manual setup needed.

#
**3.3 Signing & Release Notes (Keystore) — IMPORTANT**

* Release & benchmark builds must be signed correctly to work as intended.

* **Signing Setup:**
  In `app/build.gradle`, both release and benchmark build types reference a release signing config.
  If the keystore file is missing, these build tasks **will fail**.

* **Options for Local Runs:**
  When running locally (for release, benchmark, or baseline profile generation), **you have two choices:**

#
**3.3.1️⃣ Recommended — Create & Use a Local Release Keystore**

For accurate, production-like results:

1. **Generate a Release Keystore**
   Run in terminal:

   ```bash
   keytool -genkeypair -v -keystore release-keystore.jks \
     -alias myappkey -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Add Credentials to `local.properties`**
   Populate the file at project root (never commit this file).
   Example:

   ```
   # Movie API credentials (get at https://www.themoviedb.org/settings/api)
   #TMDB_API_KEY=123456789                           # Replace with your real API key (can use for networking)
   TMDB_API_KEY=aaa.bbb.ccc.ddd                      # Replace with your real API Read Access Token (USED for networking) & not API Key

   # Release keystore (example values)
   RELEASE_STORE_FILE=keystore/release-keystore.jks  # Keep as is
   RELEASE_STORE_PASSWORD=123456789                  # Your keystore password
   RELEASE_KEY_ALIAS=myappkey                        # Keep as is
   RELEASE_KEY_PASSWORD=123456789                    # Your key password
   ```

3. **Place the Keystore File**

   * Create a directory named `keystore/` in the **project root**.
   * Place the generated `release-keystore.jks` file from project-root folder to inside that directory.
   * Keep it **private**, **do not commit**, and **store a backup copy** (preferably encrypted for CI/CD injection).

#
**3.3.2️⃣ Temporary (Dev Only) — Use Debug Signing**

If you don’t have a keystore yet and just need to run locally:

1. **Comment out** these below lines in both release & benchmark build types in `app/build.gradle`:

  ```gradle
  signingConfigs { ... }                                // For both release & benchmark build types: Inside android block
  signingConfig = signingConfigs.getByName("release")   // For both release & benchmark build types: Inside android/buildTypes block
  ```
2. **Gradle will** sign with the default debug key instead.
  (Use only for local testing — never for Play Store or CI/CD releases.)

#
**3.4 Open Project in Android Studio**

Use the Android Gradle Plugin (AGP) version expected by the project,
then **build and run** the app on your device or emulator.

---

## 4. Tests & Reports

**4.1 Unit Tests**

Run JVM-based (non-device) unit tests:

```bash
./gradlew testDebugUnitTest
./gradlew testReleaseUnitTest

   OR

./gradlew test
```

**Results & Reports:**

* **Android Studio:** View via *Build > Test Results* or the **Run panel**.
* **HTML report:** Open this file in your browser to inspect class-wise results and stack traces.

  ```
  respective_module/build/reports/tests/testDebugUnitTest/index.html
  ```

#
**4.2 Instrumented Tests**

Run Android instrumented tests on a connected device or emulator & keep screen visible while running — you can observe real-time interactions:

```bash
./gradlew app:connectedDebugAndroidTest

OR

./gradlew connectedAndroidTest
```

**Results & Reports:**

* **Android Studio:** View results in *Run > Tests in ‘connectedDebugAndroidTest’*.
* **HTML report:**

  ```
  respective_module/build/reports/androidTests/connected/index.html
  ```
* **Logcat Output:** Detailed logs visible under the *Logcat* tab.

#
**4.3 Macrobenchmark Tests**

Handled via the **benchmark** module.

```bash
# Install benchmark build first
./gradlew :app:installBenchmark

# Run all connected benchmark tests
./gradlew :benchmark:connectedAndroidTest  OR   ./gradlew :benchmark:connectedCheck   OR   ./gradlew :benchmark:cC
```

**Results & Reports:**

* **Benchmark summary (JSON):**

  ```
  benchmark/build/outputs/connected_android_test_additional_output/
  ```
* **Per-run timing details:** Accessible under the same directory for each device and flavor.
* **Android Studio Profiler:** You can also analyze performance traces if configured with `androidx.benchmark` trace support.

---

## 5. Macrobenchmark & Baseline Profile

**5.1 Minimal Recommended Flow:**

1. **Prepare Generator Test:**
   Implement a macrobenchmark baseline generator (e.g., `BaselineProfileGenerator`) inside the **benchmark** module.

2. **Connect a Device:**
   Use a physical device (API ≥ 33 recommended) for consistent performance metrics.

3. **Generate Baseline Profile:**

   ```bash
   # Install target benchmark build
   ./gradlew :app:installBenchmark

   # Run baseline generator instrumentation test
   ./gradlew :benchmark:connectedAndroidTest \
     "-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile"
   ```

4. **Locate the Generated File:**

   ```
   benchmark/build/outputs/managed_device_android_test_additional_output/flavors/<flavor>/<deviceName>/BaselineProfileGenerator_generate-baseline-prof.txt
   ```

5. **Copy the Generated File:**
   This project expects the packaged baseline profile (Folder & file name must match exactly) at:

   ```
   app/src/main/baselineProfiles/baseline-prof.txt
   ```

6. **Rebuild & Compare:**
   Build a **non-debuggable** variant (release or benchmark) so AGP packages the profile, then **re-run benchmarks** (check section 4.3) to compare before/after results.

#
**5.2 Packaging Notes:**

If your AGP version doesn’t auto-merge baseline profiles, manually set:

```gradle
release {
    baselineProfileFiles = files("app/src/main/baselineProfiles/baseline-prof.txt")
    ...
}
create("benchmark") {
    baselineProfileFiles = files("app/src/main/baselineProfiles/baseline-prof.txt")
    ...
}
```

#
**5.3 Verify Packaging by Checking:**

* `app/build/intermediates/merged_art_profile/<variant>/baseline-prof.txt` (check increased size & line count)
* Inside built APK/AAB under `assets/` for `dexopt / baseline.prof / baseline.profm`(check increased size).

#
**5.4 Verification & Debugging Commands:**

For accurate performance benchmarking, the target app must always be non-debuggable.

1. **Prepare Generator Test:**   
   While running benchmarks (both before & after applying the baseline profile), Logcat may display the warning: `Targeted app is debuggable`.
   This indicates the app is built in debug mode, which can produce inaccurate benchmark results.

3. **Check Installed Packages & Build Type:**

```bash
adb shell pm list packages | grep cinelayr
adb shell dumpsys package com.example.cinelayr | grep debuggable
adb shell dumpsys package com.example.cinelayr.benchmark | grep debuggable
```

* `debuggable=true` → Debuggable build (not suitable for benchmarking)
* Blank/no output → Not debuggable build (release or benchmark variant, suitable for benchmarking)

---

## 6. Baseline Profile Results

> **Note**: negative % = improvement (lower times / lower variability), positive % = regression (higher times / more variability).

**Summary Table (Key Metrics)**

| **Benchmark**                                       | **Metric**                       | **Before BP** | **After BP** | **Change**                         |
| --------------------------------------------------- | -------------------------------- | ------------- | ------------ | ---------------------------------- |
| **Cold Startup — Trending** (`coldStartupTrending`) | frameCount (median)              | 254.00        | 212.00       | **-16.54% (Improved consistency)** |
|                                                     | frameCount (CoV)                 | 0.0977        | 0.1326       | **+35.76% (Less consistent)**      |
|                                                     | Total run time (s)               | 195.47 s      | 199.35 s     | **+1.99%**                         |
|                                                     | FrameDurationCpuMs P50 (ms)      | 15.61         | 17.39        | **+11.40% (Regression)**           |
|                                                     | FrameDurationCpuMs P90 (ms)      | 27.81         | 30.50        | **+09.68%**                        |
|                                                     | FrameDurationCpuMs P95 (ms)      | 41.51         | 53.22        | **+28.21% (Regression)**           |
|                                                     | FrameDurationCpuMs P99 (ms)      | 502.32        | 509.19       | **+1.37%**                         |
|                                                     | timeToInitialDisplay (min ms)    | 1239.18       | 1243.39      | **+0.34%**                         |
|                                                     | timeToInitialDisplay (median ms) | 1267.20       | 1269.92      | **+0.21%**                         |
|                                                     | timeToInitialDisplay (max ms)    | 9418.52       | 10544.54     | **+11.96% (Regression)**           |
|                                                     | CoV                              | 0.9865        | 1.0232       | **+3.72%**                         |
| **Warm Startup — Trending** (`warmStartupTrending`) | frameCount (median)              | 197.00        | 197.00       | **0.00%**                          |
|                                                     | frameCount (CoV)                 | 0.0980        | 0.0158       | **-83.84% (Big improvement)**      |
|                                                     | Total run time (s)               | 161.97 s      | 160.17 s     | **-1.12%**                         |
|                                                     | FrameDurationCpuMs P50 (ms)      | 14.41         | 14.40        | **-0.08%**                         |
|                                                     | FrameDurationCpuMs P90 (ms)      | 24.44         | 29.18        | **+19.44% (Regression)**           |
|                                                     | FrameDurationCpuMs P95 (ms)      | 36.60         | 36.27        | **-0.92%**                         |
|                                                     | FrameDurationCpuMs P99 (ms)      | 177.28        | 169.77       | **-4.24%**                         |
|                                                     | timeToInitialDisplay (min ms)    | 299.56        | 291.41       | **-2.73%**                         |
|                                                     | timeToInitialDisplay (median ms) | 314.88        | 327.51       | **+4.01%**                         |
|                                                     | timeToInitialDisplay (max ms)    | 327.41        | 334.96       | **+2.31%**                         |
|                                                     | CoV                              | 0.0650        | 0.0741       | **+14.02% (Regression)**           |
| **Hot Startup — Trending** (`hotStartupTrending`)   | frameCount (median)              | 165.00        | 166.00       | **+0.61%**                         |
|                                                     | frameCount (CoV)                 | 0.0960        | 0.0572       | **-40.41% (Big improvement)**      |
|                                                     | Total run time (s)               | 159.97 s      | 155.99 s     | **-3.11%**                         |
|                                                     | FrameDurationCpuMs P50 (ms)      | 16.60         | 14.06        | **-15.36% (Improved consistency)** |
|                                                     | FrameDurationCpuMs P90 (ms)      | 30.49         | 29.68        | **-2.65%**                         |
|                                                     | FrameDurationCpuMs P95 (ms)      | 33.69         | 31.00        | **-7.98%**                         |
|                                                     | FrameDurationCpuMs P99 (ms)      | 55.28         | 55.84        | **+1.01%**                         |
|                                                     | timeToInitialDisplay (min ms)    | –             | –            | –                                  |
|                                                     | timeToInitialDisplay (median ms) | –             | –            | –                                  |
|                                                     | timeToInitialDisplay (max ms)    | –             | –            | –                                  |
|                                                     | CoV                              | –             | –            | –                                  |

**Interpretation:**

* **Cold Startup — Trending (Time & Frame Metrics):** Baseline Profiles gave a mixed impact. Frame count improved (-16.5%) but frame-time stability regressed (P95 +28%, CoV +3.7%). Startup times stayed nearly the same, with only the max time worsening (+11.9%). Cold launches remain inconsistent but slightly smoother overall.

* **Warm Startup — Trending (Time & Frame Metrics):** Notable consistency gains — frame CoV dropped -83.8%, and total runtime slightly improved. Minor regressions in P90 and startup medians are negligible. Warm starts are now more stable and predictable.

* **Hot Startup — Trending (for & Frame Metrics):** Strong improvement. Frame CoV fell -40%, P50 improved -15%, and runtime decreased -3%, showing smoother and faster re-launches.

* **Overall:** Baseline Profiles significantly improved warm and hot startup consistency, reducing stutter and frame variability. Cold startup saw minor frame and time regressions but still benefits slightly in smoothness.

---

## 7. CI Workflow
* The current CI configuration `.github/workflows/android-ci.yml` runs only unit tests by default.
* Instrumented UI tests are included but commented out to keep builds lightweight.
* You can uncomment those sections anytime to enable full instrumentation testing as well on the CI runner.

* If you want CI (GitHub Actions) to run instrumented tests, benchmarks, or assemble signed release/benchmark APKs, add these **five** secrets to your GitHub repo (Settings → Secrets & variables → Actions):

1. `TMDB_API_KEY` — your `TMDB_API_KEY` (e.g. `aaa.bbb.ccc.ddd`) used to call [TMDB.Org](https://www.themoviedb.org/settings/api).
2. `RELEASE_KEYSTORE_BASE64` — base64-encoded `release-keystore.jks` (decode in workflow to a file).
3. `RELEASE_STORE_PASSWORD` — keystore password (e.g. `123456789`).
4. `RELEASE_KEY_ALIAS` — key alias (e.g. `myappkey`).
5. `RELEASE_KEY_PASSWORD` — key password (can match store password if desired).

---
