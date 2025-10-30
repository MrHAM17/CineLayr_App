plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    ///
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.example.data"
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
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    // Paging
    implementation("androidx.paging:paging-common-ktx:3.2.1")

    // Koin
    implementation("io.insert-koin:koin-android:3.4.3")

    // ✅ Room Runtime & KTX (fixes withTransaction + RoomDatabase access)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1") // <-- REQUIRED
//    kapt("androidx.room:room-compiler:2.6.1") // if using KAPT
//     OR if you're using KSP:
     ksp("androidx.room:room-compiler:2.6.1")


    // --- UNIT TEST DEPENDENCIES ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("androidx.paging:paging-common:3.3.2")

}