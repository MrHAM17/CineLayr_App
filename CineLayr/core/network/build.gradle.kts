plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    //
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21"

}

android {
    namespace = "com.example.core.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ///
        /// If used Gradle.Properties
//        buildConfigField("String", "TMDB_API_KEY", "\"${project.properties["TMDB_API_KEY"] ?: ""}\"")
        /// If used Local.Properties (with app build gradle)
        buildConfigField( "String","TMDB_API_KEY","\"${rootProject.extra["TMDB_API_KEY"]}\"" )
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    ////
    // Ktor
    implementation("io.ktor:ktor-client-core:2.3.2")
    implementation("io.ktor:ktor-client-android:2.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
    implementation("io.ktor:ktor-client-logging:2.3.2")

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Koin
    implementation("io.insert-koin:koin-android:3.4.3")

    // Paging
    implementation("androidx.paging:paging-common-ktx:3.2.1")
}