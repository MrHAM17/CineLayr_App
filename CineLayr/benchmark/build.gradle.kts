plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.benchmark"
    compileSdk = 36

    // ----------------------
    // Target App module
    // ----------------------
    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    defaultConfig {
        minSdk = 29 // ← CHANGE from 24 to 28 (BaselineProfile requires API 28+)
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        // Suppress DEBUGGABLE error for benchmark tests (optional)
//        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "DEBUGGABLE"

        // CRITICAL: Specify the package name of the app variant being tested
        testInstrumentationRunnerArguments["androidx.benchmark.targetPackage"] = "com.example.cinelayr.benchmark"
        // CRITICAL: Specify the build type of the app variant being tested
        testInstrumentationRunnerArguments["androidx.benchmark.targetBuildType"] = "benchmark"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "LOW-BATTERY,DEBUGGABLE"

    }


    // ❌❌❌ NO NEED for debug/release or any build types here! ❌❌❌
    // ❌❌❌ The benchmark module doesn't define its own build types ❌❌❌
    // Benchmark Tests → Run against → App's "benchmark" build type → Generates performance data
    // // buildTypes {}

//    buildTypes {
//        // This benchmark buildType is used for benchmarking, and should function like your
//        // release build (for example, with minification on). It"s signed with a debug key
//        // for easy local/CI testing.
//        create("benchmark") {
//            isDebuggable = true
//            signingConfig = getByName("debug").signingConfig
//            matchingFallbacks += listOf("release")
//        }
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)

    ///
    implementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
    implementation("androidx.test:runner:1.7.0")
    implementation("androidx.test.ext:junit:1.3.0")
}

/*

  Remove the androidComponents block entirely - it’s redundant when
  you explicitly use testInstrumentationRunnerArguments and correct targetBuildType.

*/
//androidComponents {
//    beforeVariants(selector().all()) {
//        it.enable = it.buildType == "benchmark"
//    }
//}