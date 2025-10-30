plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    ///
//    id("kotlin-kapt")
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.example.core.database"
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

    // Room Runtime & KTX (fixes withTransaction + RoomDatabase access)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1") // <-- REQUIRED
//    kapt("androidx.room:room-compiler:2.6.1") // if using KAPT
//     OR if you're using KSP:
    ksp("androidx.room:room-compiler:2.6.1")


    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    // Room Paging (needed for PagingSource DAO queries)
    implementation("androidx.room:room-paging:2.6.1")

    // Koin
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")
    // For annotation-based DI
    implementation("io.insert-koin:koin-annotations:1.3.1")
}