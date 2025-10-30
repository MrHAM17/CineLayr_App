package com.example.cinelayr

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.cinelayr.ui.CineLayrApp
import kotlinx.coroutines.delay
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Get movieId from intent extras (widget or deep link)
        val widgetMovieIdFromIntent = intent?.getIntExtra("movieId", 0)?.takeIf { it != 0 }

        //        setContent {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
////                color = MaterialTheme.colorScheme.background
//            ) {
////                    CineLayrApp()
//                CineLayrApp(startMovieId = widgetMovieIdFromIntent)  // For widgetToApp entry...
//
//            }
//
////            CineLayrApp(startMovieId = widgetMovieIdFromIntent)  // For widgetToApp entry...
//
//        }

        setContent {
            var isReady by remember { mutableStateOf(false) }

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isReady) {
                    CineLayrApp(startMovieId = widgetMovieIdFromIntent)     // For widgetToApp entry...
                }
                // Start showing spinner immediately
//                else { CircularProgressIndicator()  }
            }

            LaunchedEffect(Unit) {
                delay(500) // Let Koin and DB warm up
//                // Wait until modules are loaded (approx 0.5–1s)
//                while (GlobalContext.getOrNull()?.koin == null) {
//                    delay(50)
//                }
                isReady = true
            }

        }

    }
}

/*

1) Unit Tests -
data - unit - MovieRepositoryImplTest
fe tre - unit - TrendingViewModelTest.kt
fe de - unit - DetailsViewModelTest.kt
./gradlew test
./gradlew :feature-trending:testDebugUnitTest
./gradlew :feature-details:testDebugUnitTest
./gradlew :data:testDebugUnitTest

2) UI Tests -
app- ui - DetailsScreenTest.kt, TrendingScreenTest.kt (all commented so no ui tests)

3) Macro Benchmark Tests-
benchmark - macrobenchmark ui - CineLayrMacrobenchmark
keep comment out bp code file
./gradlew :app:installBenchmark
./gradlew :benchmark:connectedAndroidTest

4) Baseline Profile Generation -
keep comment out macrobenchmark ui code file & keep bp code file
./gradlew clean
./gradlew :app:installBenchmark
./gradlew :benchmark:connectedAndroidTest "-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile"

5) Re-Run Macro Benchmark Tests -
keep bp code file & comment out the bp text
./gradlew :app:installBenchmark
./gradlew :benchmark:connectedAndroidTest

6) UI version 1 files -
core/designsystem/Theme.kt
core/designsystem/Type.kt
core/designsystem/theme/CineLayrTopBar.kt
app/ui/CineLayrApp.kt
feature-trending/TrendingScreen.kt
feature-trending/MovieItem.kt.kt

*/