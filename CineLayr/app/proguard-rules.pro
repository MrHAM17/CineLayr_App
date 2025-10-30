# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

### ///
# Add project specific ProGuard rules here.
-keep class com.cinelayr.** { *; }
-keepclassmembers class com.cinelayr.** { *; }

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.util.debug.**
-dontwarn java.lang.management.**

# SLF4J (logging)
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }

# Serialization
-keepclassmembers class kotlinx.serialization.json.Json { *; }

# (Optional) General Kotlin keep rules
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { *; }