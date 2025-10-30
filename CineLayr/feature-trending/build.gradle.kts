plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    ///
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.plugin.compose") // <- add this

}

android {
    namespace = "com.example.feature.trending"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    ///
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4" // "1.4.7"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    ///
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.compose.ui:ui")
    // For preview support
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Optional: adds preview functionality for custom composables
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")


    // Paging
    implementation("androidx.paging:paging-compose:3.3.2")
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Koin
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")
    // For annotation-based DI
    implementation("io.insert-koin:koin-annotations:1.3.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // --- UNIT TEST DEPENDENCIES ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("androidx.paging:paging-common:3.3.2")
}