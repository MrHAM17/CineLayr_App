import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    ///
//    id("kotlin-kapt")
    alias(libs.plugins.ksp)
//    id("org.jetbrains.kotlin.plugin.compose")


//    id("com.android.tools.build.baselineprofile") // The Baseline Profile plugim
//    alias(libs.plugins.baselineprofile) // Use alias for consistency
    id("androidx.baselineprofile") // Applying the AndroidX plugin

}

android {
    namespace = "com.example.cinelayr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cinelayr"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ///
        vectorDrawables {
            useSupportLibrary = true
        }

        /// If used Gradle.Properties
//        buildConfigField("String", "TMDB_API_KEY", "\"${project.properties["TMDB_API_KEY"] ?: ""}\"")
        /// If used Local.Properties (with app build gradle)
        buildConfigField( "String","TMDB_API_KEY","\"${rootProject.extra["TMDB_API_KEY"]}\"" )
        // print("🔑 API_KEY", "Key = ${rootProject.extra["TMDB_API_KEY"]}")
    }

    signingConfigs {
        create("release") {
            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) localProperties.load(localPropertiesFile.inputStream())

            // Path to the keystore file
            storeFile = rootProject.file(localProperties.getProperty("RELEASE_STORE_FILE") ?: "keystore/release-keystore.jks")
            // Password for the keystore itself
            storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD") ?: ""
            // The alias of the key inside the keystore
            keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS") ?: ""
            // Password for that key
            keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {

        // ----------------------
        // DEBUG BUILD → for dev, UI tests, and iterative checks
        // Keep class/method names readable (un-obfuscated )  → easier debugging & testing
        // isDebuggable = true → allows profiling and debugging tools
        // ----------------------
        debug {
            isDebuggable = true
            isJniDebuggable = true
            isProfileable = false
            isMinifyEnabled = false
            isShrinkResources = false //  Enable resource shrinking

            // Add these for better debug performance
//            applicationIdSuffix = ".debug"
//            versionNameSuffix = "-DEBUG"
        }

        // ----------------------
        // RELEASE BUILD → shipped to users, measure production app, see performance in real obfuscated app.
        // Fully optimized for production + Minified + obfuscated + NOT profileable (cannot be inspected)
        // Don't keep isMinifyEnabled = false (the proguard file is ignored anyway,)
        // When you run
        //      ./gradlew :app:assembleRelease →  You’ll get in studio a fully optimized release APK (obfuscated, not profileable) (com.example.newsroom).
        //      ./gradlew :app:installRelease →  You’ll get in running device a fully optimized release APK (obfuscated, not profileable) (com.example.newsroom).
        //
        // 🔑 IMP NOTE -   BEFORE ASSEMBLE RELEASE IF BASELINE PROFILE IS GENERATED && PLACED AT "app/src/main/baselineProfiles/baseline-prof.txt"
        //                  --> IT WILL BE INCLUDED IN THE RELEASE APK AUTOMATICALLY (IF YOUR AGP VERSION SUPPORTS).
        //                  --> IT WILL NOT BE INCLUDED IN THE RELEASE APK AUTOMATICALLY (IF YOUR AGP VERSION NOT SUPPORTS).
        //                 AND IF FILE IS GENERATED BUT NOT PLACED THERE
        //                 --> GRADLE WILL SKIP IT & INCUDES DEFAULT ONE BY ANDROID ITSELF
        //                 AND IF FILE NOT GENERATED (BASELINE PROFILE IS NOT USED)
        //                 --> APK WILL NOT BE OPTIMIZED.
        // 🔑 NOTE - TO VERIFY IS SHIPPED BASELINE PROFILE INCLUDED IN RELEASE BUILD
        //           -→ Confirm via app/build/intermediates/merged_art_profile/release/baseline-prof.txt Its size and number of lines increase noticeably (as compared to before shipping file).
        //              &
        //              app/build/outputs/apk/release/app-release.apk/assets/dexopt/(baseline.prof, baseline.profm) Its sizes increase (as compared to before shipping file) when applied.
        //           ⚠️ The Gradle terminal logs (even with --info) may only mention
        //              “src/release/baselineProfiles not found” or skipped paths.
        //              That is normal — AGP automatically merges from src/main/baselineProfiles/ at fallback internally without explicitly logging it.
        release {
            signingConfig = signingConfigs.getByName("release") // 🔑 important!
            isDebuggable = false
            isJniDebuggable = false
            isProfileable = false
            isMinifyEnabled = true // Shrink + obfuscate for production release APK
            isShrinkResources = true //  Enable resource shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_HTTP_LOGGING", "false")
        }

        // ---------------------
        // BENCHMARK BUILD (assembleBenchmark)
        // ---------------------
        // PROFESSIONAL: Create benchmark build type IN APP MODULE
        //    - Based on release, but with overrides
        //    - Profileable = true, non-minified
        //    - Ideal for Macrobenchmark & Baseline Profile collection
        // create("benchmark") { initWith(getByName("release")) } →
        //      Gradle clones all settings from release, then applies only the overrides you specify inside benchmark { ... }.
        // When you run
        //      ./gradlew :app:assembleBenchmark →  You’ll get in studio a special build that’s profileable, non-minified, ideal for macrobenchmarks (com.example.cinelayr.benchmark) (mannually suffix added in benchmark build type).
        //      ./gradlew :app:installBenchmark →  You’ll get in running device a special build that’s profileable, non-minified, ideal for macrobenchmarks (com.example.cinelayr.benchmark) (mannually suffix added in benchmark build type).
        //      ./gradlew :benchmark:connectedCheck →  Benchmarks tests will start in connected running device and will measure the performance & will give results, reports
        // Gradle generates an HTML test report --> benchmark/build/reports/androidTests/connected/debug/index.html
        // JSON Metrics Output --> benchmark/build/outputs/connected_android_test_additional_output/debugAndroidTest/connected/device-[...]/<test-name>.json
        //
        // NOTE IMP - WHILE BENCHMARKING SETUP
        //             1️⃣ APKs involved
        //               - com.example.benchmark          → Benchmark shell/orchestrator (invisible on device, install only once, auto uninstall after all tests performed, runs Perfetto, collects results)
        //               - com.example.cinelayr.benchmark → Target app for measurements (buildType: benchmark, based on release varient, applicationIdSuffix=".benchmark")
        //               - com.example.cinelayr           → Normal app (debug/release), referenced by Gradle/ADB
        //             2️⃣ Automatic install/uninstall
        //               - Gradle installs the shell and target APK automatically before tests.
        //               - Each iteration reinstall a fresh APK to ensure clean state.
        //               - After tests, temporary APKs are removed; manual installs persist.
        //             3️⃣ Key takeaways
        //               - After cC command target apk "com.example.cinelayr.benchmark" is installed on device along with another apk "com.example.cinelayr".
        //               - But Gradle/ADB temporarily reinstalls the benchmark APK "com.example.cinelayr.benchmark" as "com.example.cinelayr" for runtime visibility.
        //               - After shell launch, the target APK "com.example.cinelayr.benchmark" appear under the original package name "com.example.cinelayr" for a short time.
        //               - This aliasing ensures UiAutomator selectors and Perfetto tracing work consistently.
        //               - Seeing both names simultaneously or in logs is normal; one is the build-time package, one is runtime alias.
        //               - Android Studio & Gradle treat these as two completely separate APK variants, so there’s no clash.
        //
        // ---------------------
        // Benchmark Performance Measurements - issues & solution --> Real Results
        // ---------------------
        //    1. Debug builds → automatically signed by Android Studio debug keystore, no manual config needed.
        //    2. Release builds → require a signingConfig to produce release (installable) APKs.
        //    3. Using debug keystore for release is okay for local testing, benchmark but not for production.
        //    4. Without signingConfig in release → Gradle skip generating actual Release APK and produce debug/unsigned APK for release variant.
        //    5. Benchmark builds → can use debug key or unsigned; must be profileable (isProfileable=true).
        //    6. No separate "debug sign" needed for benchmark.
        //
        // 🔑 BUT VV IMP NOTE:
        //                     [ (  IF RELEASE BUILD TYPE POINTS DEBUG KEYSTORE VIA RELEASE SIGNIN CONFIG
        //                          ||
        //                          IF BENCHMARK TESTS CODES POINT TO WRONG APP_PACKAGE ("com.example.cinelayr" or "com.example.benchmark") (PS: CORRECT SHOULD BE "com.example.cinelayr.benchmark")
        //                       ) &&
        //                       THIS RELEASE BUILD TYPE IS USED FOR BENCHMARK BUILD TYPE
        //                     ] THEN BENCHMARK PROCEEDS
        //                       --> __BUT__ EARLIER OR IN BETWEEN IN SETUP IT MIGHT BE STOPED/CRASHED/FAILED. SO NOTHING GET WILL START.
        //                       --> __BUT__ WILL GET THE WARNING "TARGETED APP IS DEBUGGABLE" SO RESULTS ARE NOT ACCURATE/USELESS.
        //                       --> __BUT__ THE PERFORMANCE MEASURED IS OF DEBUG or UNSIGNED APK ONLY & IS NOT OF ACTUAL RELEASE APK (WHICH IS THE ONLY INTENDED TO BE..)
        //    SOLUTION: WE'VE TO SET REAL GENERATED RELEASE SIGN IN CONFIG TO GET REAL ACCURATE RESULTS (So Steps given just below for - Release Sign in Config Setup)
        //    7. Recommended workflow:
        //       4️⃣ Build types & signing
        //           - Debug → signed automatically by Android Studio debug keystore.
        //           - Release → requires signingConfig to produce installable APK (use proper release signing config for production); use same config for accurate benchmark.
        //           - Benchmark → profileable (isProfileable=true); can use debug or release signing (but should use release for actual real results) but must be debuggable.
        //
        // -------------------------
        // RELEASE SIGNING CONFIG SETUP
        // -------------------------
        // 1. Generate a release keystore using keytool (Run below command here in terminal as it is) & in project folder create a Directory "keystore" & paste the generated "release-keystore.jks" file from prject folder to inside that directory -->
        //      keytool -genkey -v -keystore release-keystore.jks -alias myappkey -keyalg RSA -keysize 2048 -validity 10000
        // 2. Entered the keystore password during generation (used same for key password).
        // 3️. Added the following 4 variables to local.properties:
        //      RELEASE_STORE_FILE=keystore/release-keystore.jks
        //      RELEASE_STORE_PASSWORD=<keystore_password>
        //      RELEASE_KEY_ALIAS=myappkey   // keep this value as it is
        //      RELEASE_KEY_PASSWORD=<keystore_password>
        // 4️. Create signingConfigs{...} block in app/build.gradle ().
        // 5️. Apply "signingConfig = signingConfigs.getByName("release")" in the app build gradle release buildType.
        // 6️. Benchmark buildType is based on release, but profileable and non-minified. So add "initWith(getByName("release"))" in the app build gradle benchmark buildType.
        // 7. This setup allows:
        //      • assembleRelease → signed, minified release APK
        //      • assembleBenchmark → signed release apk which will be target by benchmark APK for benchmark "improved final optimized" performance tests.
        //
        // ---------------------
        // BASELINE PROFILE GENERATION
        // ---------------------
        // 1. Prepare baseline profile benchmark code under "benchmark/src/main/java/com.example.benchmark/BaselineProfileGenerator.kt"
        // 2. keep running device (real or emulator) connected - BUT SHOULD BE OF - MINIMUM API LEVEL 33 (otherwise baseline profile generation will failed to generate .txt or .prof file which we want)
        // 3. Run this once benchmarks are set up to generate baseline-prof.txt -->
        //      ./gradlew :app:installBenchmark
        //      ./gradlew :benchmark:connectedAndroidTest "-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile"
        //
        // → This executes your Macrobenchmark baseline profile tests code with the "BaselineProfile" rule and saves the generated profile into -
        //       benchmark/build/outputs/connected_android_test_additional_output/debug/connected/<device>/BaselineProfileGenerator_generateBaselineProfile-baseline-prof.txt
        //
        //        .../logcat-com.example.benchmark.baselineprofile.BaselineProfileGenerator-generateBaselineProfile.txt	✅ Logcat dump of what happened during generation. Useful for debugging.
        //        .../additionaltestoutput.benchmark.message_...txt	Benchmark test messages. ✅ Mostly informational.
        //        .../BaselineProfileGenerator_generateBaselineProfile-baseline-prof.txt ✅ This is the actual baseline profile file.
        //        .../BaselineProfileGenerator_generateBaselineProfile-baseline-prof-2025-10-02-22-23-49.txt ✅ Backup version with timestamp. Also contains the same profile.
        //
        // After generation, copy that file into your app module (usually app/src/main/baselineProfiles/baseline-prof.txt) and register it inside build types manually.
        // 🔑 NOTE - IF YOUR AGP VERSION AUTOMATICALLY SEARCHES & HANDLES THE GENERATED BASELINE PROFILE THEN IN THIS APP GRADLE DO NOTHING IN RELEASE AND BENCHMARK BUILD TYPES
        //           OTHERWISE MENTION MANUALLY LIKE BELOW IN THIS APP GRADLE:
        //                  release {    baselineProfileFiles = files("app/src/main/baselineProfiles/baseline-prof.txt")  }
        //                  create("benchmark")  {    baselineProfileFiles = files("app/src/main/baselineProfiles/baseline-prof.txt")  }
        //         - AFTER BASELINE PROFILE GENERATED (.txt or .prof file) KEEP THE BASELINE PROFILE CODE ("benchmark/src/main/java/com.example.benchmark/BaselineProfileGenerator.kt") COMMENTED OUT.
        //
        // ---------------------
        // RE-RUN BENCHMARK FOR OPTIMIZED PERFORMANCE
        // ---------------------
        // After baseline profile is applied & shipped,
        // simply rebuild & rerun benchmarks:
        //    ./gradlew :app:installBenchmark
        //    ./gradlew :benchmark:connectedAndroidTest "-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile"
        //
        // All reports and results will get at same locations as above discussed.
        // → This lets you compare performance before vs after baseline profiles.
        //
        // 🔑 IMP NOTE -   BEFORE "RE-RUN BENCHMARK FOR OPTIMIZED PERFORMANCE" IF BASELINE PROFILE IS GENERATED && PLACED AT "app/src/main/baselineProfiles/baseline-prof.txt"
        //                  --> IT WILL BE INCLUDED AUTOMATICALLY (IF YOUR AGP VERSION SUPPORTS) & WILL GET FINAL OPTIMIZED PERFORMANCE RESULTS.
        //                  --> IT WILL NOT BE INCLUDED  AUTOMATICALLY (IF YOUR AGP VERSION NOT SUPPORTS) SO MANUALLY MENTION IN THIS APP GRADLE INSIDE RELEASE & BENCHMARK BUILD TYPES.
        //                 AND IF FILE IS GENERATED BUT NOT PLACED THERE
        //                 --> GRADLE WILL SKIP IT & INCUDES DEFAULT ONE BY ANDROID ITSELF
        //                 --> WILL NOT GET OPTIMIZED PERFORMANCE.
        //                 AND IF FILE NOT GENERATED (BASELINE PROFILE IS NOT USED)
        //                 --> WILL NOT GET OPTIMIZED PERFORMANCE.
        // 🔑 NOTE - TO VERIFY IS SHIPPED BASELINE PROFILE INCLUDED IN Benchmark BUILD
        //           -→ Confirm via app/build/intermediates/merged_art_profile/benchmark/baseline-prof.txt Its size and number of lines increase noticeably (as compared to before shipping file).
        //              &
        //              app/build/outputs/apk/release/benchmark-app.apk/assets/dexopt/(baseline.prof, baseline.profm) Its sizes increase (as compared to before shipping file) when applied.
        //           ⚠️ The Gradle terminal logs (even with --info) may only mention
        //              “src/benchmark/baselineProfiles not found” or skipped paths.
        //              That is normal — AGP automatically merges from src/main/baselineProfiles/ at fallback internally without explicitly logging it.
        create("benchmark") {
            initWith(getByName("release"))  // inherit release behavior (non-debuggable)
            signingConfig = signingConfigs.getByName("release") // 🔑 important!
            // signingConfig = signingConfigs.getByName("debug") // Easy testing, just key, not debug mode

            isDebuggable = false  // true → allows profiling and debugging tools
            isJniDebuggable = false
            isProfileable = true  // Critical for profiling
            isMinifyEnabled = false // optional but recommended for simpler profiling
            isShrinkResources = false // true //  Enable resource shrinking
            applicationIdSuffix = ".benchmark"  // recommended
            matchingFallbacks += listOf("release")
            buildConfigField("boolean", "ENABLE_HTTP_LOGGING", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true

        ///
        buildConfig = true
    }

    ///
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4" // "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE*"
            excludes += "META-INF/NOTICE*"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.ui.unit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    ///
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature-trending"))
    implementation(project(":feature-details"))
    implementation(project(":feature-watchlist"))


    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")

    // Koin
    implementation("io.insert-koin:koin-core:3.4.3")
    implementation("io.insert-koin:koin-android:3.4.3")
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // SplashScreen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Glance AppWidget
    implementation("androidx.glance:glance:1.1.1")
    implementation("androidx.glance:glance-appwidget:1.0.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Benchmark and profiling
    implementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
    testImplementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
    androidTestImplementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
//    benchmarkImplementation("androidx.profileinstaller:profileinstaller:1.3.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
    androidTestImplementation("androidx.profileinstaller:profileinstaller:1.3.1")



}