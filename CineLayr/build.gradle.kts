// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false

    ///
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.test) apply false


    //    alias(libs.plugins.baselineprofile) apply false // <- Baseline profile definition with version
    //    id("com.android.tools.build.baselineprofile") apply false
    id("androidx.baselineprofile") version "1.4.1" apply false


}

val localProperties = java.util.Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {  load(localFile.inputStream())  }
}
val tmdbApiKey: String = localProperties.getProperty("TMDB_API_KEY", "")
ext["TMDB_API_KEY"] = tmdbApiKey // Make it available for all modules
// // Example - android {  defaultConfig { buildConfigField( "String", "TMDB_API_KEY", "\"${rootProject.extra["TMDB_API_KEY"]}\"" ) }  }


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}