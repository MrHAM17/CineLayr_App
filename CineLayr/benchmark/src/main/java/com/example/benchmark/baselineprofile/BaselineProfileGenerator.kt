///**
// * BaselineProfileGenerator
// *
// * This test generates a Baseline Profile for the app.
// *
// * The Baseline Profile precompiles critical code paths (e.g., startup and main user journeys)
// * to improve app launch time and runtime performance on user devices.
// *
// * ➤ This test simulates realistic user interactions such as scrolling and navigating
// *   through key screens (Trending, Watchlist).
// *
// * Note:
// * - Only one cold-start test is required to generate a complete profile.
// * - Warm/Hot startups are omitted here since they don’t contribute additional methods
// *   to the Baseline Profile and would only duplicate execution.
// *
// * V IMP:
// * - To generate baseline profile, Please check the detailed instructions in app/build.gradle file & README.md file.
// */
//
//package com.example.benchmark.baselineprofile
//
//import androidx.benchmark.macro.*
//import androidx.benchmark.macro.junit4.BaselineProfileRule
//import androidx.benchmark.macro.junit4.MacrobenchmarkRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.uiautomator.UiDevice
//import androidx.test.uiautomator.By
//import androidx.test.uiautomator.Until
//import androidx.test.uiautomator.UiObject2
//import perfetto.protos.AndroidCpuMetric
//import java.util.regex.Pattern
//
//@RunWith(AndroidJUnit4::class)
//class BaselineProfileGenerator {
//
//    @get:Rule
////    val benchmarkRule = MacrobenchmarkRule()  // MacrobenchmarkRule provides the benchmarking environment used to collect performance metrics.
//    val baselineProfileRule = BaselineProfileRule()
//
//    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())  // UiDevice allows UI automation (clicks, scrolls, navigation) using the Android UI Automator API.
//    private val packageName = "com.example.cinelayr.benchmark"  // The target app package under test
//    private val iterations = 1  // Number of iterations for the benchmark run
//
//    // 🔹 Helper swipe (fast & human-like)
//    private fun UiDevice.fastSwipeUp(times: Int = 1) {
//        repeat(times) {
//            val width = displayWidth / 2
//            val startY = (displayHeight * 0.75).toInt()
//            val endY = (displayHeight * 0.25).toInt()
//            swipe(width, startY, width, endY, 10)
//            waitForIdle()
//        }
//    }
//
//    private fun performTrendingScreenActions() {
//        device.waitForIdle()
//
//        // Wait until trending list appears
//        device.wait(Until.hasObject(By.desc("trending_list")), 5000)
//
//        // 👉 Scroll a bit first (for realism)
//        device.fastSwipeUp(1)
//
//        // Make sure at least one movie is visible
//        Thread.sleep(1000)
//        device.wait(Until.hasObject(By.desc(Pattern.compile("movie_item_.*"))), 3000)
//
//        // 👉 Try to open at least one movie
//        openOneMovieFromTrending()
//
//        // Theme toggled on trending
//        Thread.sleep(1500)
//    }
//
//    private fun openOneMovieFromTrending() {
//        // Find first visible movie
//        Thread.sleep(500)
//        var item = device.wait(Until.hasObject(By.desc(Pattern.compile("movie_item_.*"))), 3000)
//        if (item == null) {
//            item = device.wait(Until.hasObject(By.desc(Pattern.compile("movie_item_.*"))), 5000)
////            println("🎬 movie_item_ Has_Object - $item")
//        }
//
//        var movieItem = device.findObjects(By.desc(Pattern.compile("movie_item_.*"))).firstOrNull()
//        if (movieItem == null) {
//            movieItem = device.wait(Until.hasObject(By.desc(Pattern.compile("movie_item_.*"))), 5000) as UiObject2?
////            println("🎬 movie_item_ Find_Object - $item")
//        }
//
//        if (movieItem != null) {
//            movieItem.click()
//
//            // Toggle FAB (save)
//            Thread.sleep(500)
//            device.clickSafely(
//                testTag = "details_fab",
//                contentDescs = listOf("Add to watchlist", "Remove from watchlist")
//            )
//
//            // Scroll in details
//            device.fastSwipeUp(1)
//
//            // Toggle theme
//            Thread.sleep(1000)
//            device.clickSafely(
//                testTag = "topbar_theme_toggle",
//                contentDescs = listOf("Switch to Dark Mode", "Switch to Light Mode")
//            )
//
//            // Go back to trending
//            device.pressBack()
//            device.wait(Until.hasObject(By.res("trending_list")), 3000)
//        } else {
////            println("⚠️ No movie found to click!")
//        }
//    }
//
//    private fun performWatchlistScreenActions() {
//        // Open Watchlist tab
//        Thread.sleep(1000)
//        device.clickSafely( text = "Watchlist", testTag = "Watchlist", contentDescs = listOf("watchlist"))
//
//
//        // Ensure watchlist has loaded
//        device.wait(Until.hasObject(By.res("watchlist_list")), 2000)
//
//        // Scroll watchlist
//        device.fastSwipeUp(1)
//
//        // Toggle theme twice
//        Thread.sleep(1000)
//        device.clickSafely(
//            testTag = "topbar_theme_toggle",
//            contentDescs = listOf("Switch to Light Mode", "Switch to Dark Mode")
//        )
//
//        // Make sure at least one movie is visible - so open it to delete to simulate removal
//        Thread.sleep(1000)
//        var item = device.wait(Until.hasObject(By.desc(Pattern.compile("watchlist_item_.*"))), 3000)
//        if (item == null) {
//            item = device.wait(Until.hasObject(By.desc(Pattern.compile("watchlist_item_.*"))), 5000)
////            println("🎬 watchlist_item_ Has_Object - $item")
//        }
//
//        // Find first visible movie
//        var movieItem = device.findObjects(By.desc(Pattern.compile("watchlist_item_.*"))).firstOrNull()
//        if (movieItem == null) {
//            movieItem = device.wait(Until.findObjects(By.desc(Pattern.compile("watchlist_item_.*"))), 5000) as UiObject2?
////            println("🎬 watchlist_item_ Find_Object - $movieItem")
//        }
//
//        if (movieItem != null) {
//            movieItem.click()
//
//            // Toggle FAB (save)
//            Thread.sleep(500)
//            device.clickSafely(
//                testTag = "details_fab",
//                contentDescs = listOf("Remove from watchlist", "Add to watchlist")
//            )
//
//            device.pressBack()
//            device.waitForIdle()
//        }
//        else {
////            println("⚠️ No watchlist movie item found to click!")
//        }
//    }
//
//    @Test
////    fun coldStartupBenchmark() = benchmarkRule.measureRepeated(
////        packageName = packageName,
////        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
////        iterations = iterations,
////        startupMode = StartupMode.COLD,
////        compilationMode = CompilationMode.Partial(
////            baselineProfileMode = BaselineProfileMode.Require
////        )
////    )
//    fun generateBaselineProfile() = baselineProfileRule.collect(packageName = packageName,1)
//    {
//        pressHome()
//        startActivityAndWait()
//        performTrendingScreenActions()
//        performWatchlistScreenActions()
//    }
//
////    @Test
////    fun warmStartupBenchmark() = benchmarkRule.measureRepeated(
////        packageName = packageName,
////        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
////        iterations = iterations,
////        startupMode = StartupMode.WARM,
////        compilationMode = CompilationMode.Partial(
////            baselineProfileMode = BaselineProfileMode.Require
////        )
////    ) {
////        pressHome()
////        startActivityAndWait()
////        performTrendingScreenActions()
////        performWatchlistScreenActions()
////    }
////
////    @Test
////    fun hotStartupBenchmark() = benchmarkRule.measureRepeated(
////        packageName = packageName,
////        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
////        iterations = iterations,
////        startupMode = StartupMode.HOT,
////        compilationMode = CompilationMode.Partial(
////            baselineProfileMode = BaselineProfileMode.Require
////        )
////    ) {
////        startActivityAndWait()
////        performTrendingScreenActions()
////        performWatchlistScreenActions()
////    }
//
//}
//
//// 🔹 Helper extensions
//
//// Helper extension to find UI elements by testTag (either via content description or resource name)
//fun UiDevice.findObjectByTestTag(tag: String): UiObject2? {
//    return findObject(By.desc(tag)) ?: findObject(By.res(tag))
//}
//
//// Helper to find the first element whose resource name starts with a prefix
//fun UiDevice.findObjectByTestTagStartsWith(prefix: String): UiObject2? {
//    return findObjects(By.res(Pattern.compile(".*"))).firstOrNull {
//        it.resourceName?.startsWith(prefix) == true
//    }
//}
//
//// Helper to find UI elements by content description
//fun UiDevice.findObjectByDescription(desc: String): UiObject2? {
//    return findObject(By.desc(desc))
//}
//
////  Generic safe click helper for Compose testTags or content descriptions. [Tries to find an element by :- testTag (mapped to contentDescription), explicit contentDescription string(s), optional text match (if provided)]
//fun UiDevice.clickSafely(
//    testTag: String? = null,
//    contentDescs: List<String> = emptyList(),
//    text: String? = null,
//    waitMs: Long = 9000L
//) {
//    // Wait for any possible target to appear
//    waitForIdle()
//    if (testTag != null) wait(Until.hasObject(By.desc(testTag)), waitMs)
//
//    // Try in priority order
//    val obj = when {
//        testTag != null -> findObjectByTestTag(testTag)
//        else -> null
//    } ?: contentDescs.firstNotNullOfOrNull { desc -> findObject(By.desc(desc)) }
//    ?: if (text != null) findObject(By.text(text)) else null
//
//    if (obj != null) {
//        try {
//            Thread.sleep(1000)
//            obj.click()
//            waitForIdle()
//        } catch (e: Exception) {
//            println("⚠️ [clickSafely] Click failed for $testTag / $contentDescs — ${e.message}")
//        }
//    } else { println("⚠️ [clickSafely] No UI element found for testTag=$testTag contentDescs=$contentDescs text=$text") }
//}
