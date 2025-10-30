package com.example.benchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupTimingMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector

@RunWith(AndroidJUnit4::class)
class CineLayrMacrobenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val iterations = 5
    private val packageName = "com.example.cinelayr"

    private fun performTrendingScroll() {
        // Wait for idle UI to be idle
        device.waitForIdle()

//        // Scroll Trending screen (LazyColumn)
//        device.swipe(500, 1600, 500, 300, 20)  // first swipe
//        device.swipe(500, 1600, 500, 300, 20)  // second swipe

        // Scroll using UiScrollable to interact with a scrollable UI component
        val scrollable = UiScrollable(UiSelector().scrollable(true))
        scrollable.scrollForward()  // Scroll down (you can adjust this to scrollBackward(), etc.)
        scrollable.scrollBackward()

    }

    @Test
    fun coldStartupTrending() = benchmarkRule.measureRepeated(
        packageName = packageName,
        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
        iterations = iterations,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.Partial(
            baselineProfileMode = BaselineProfileMode.UseIfAvailable
        )
    ) {
        pressHome()
        startActivityAndWait()
        Thread.sleep(3000)  // wait 5 seconds before scrolling
        performTrendingScroll()
    }

    @Test
    fun warmStartupTrending() = benchmarkRule.measureRepeated(
        packageName = packageName,
        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
        iterations = iterations,
        startupMode = StartupMode.WARM,
        compilationMode = CompilationMode.Partial(
            baselineProfileMode = BaselineProfileMode.UseIfAvailable
        )
    ) {
        pressHome()
        startActivityAndWait()
        Thread.sleep(3000)  // wait 5 seconds before scrolling
        performTrendingScroll()
    }

    @Test
    fun hotStartupTrending() = benchmarkRule.measureRepeated(
        packageName = packageName,
        metrics = listOf(FrameTimingMetric(), StartupTimingMetric()),
        iterations = iterations,
        startupMode = StartupMode.HOT,
        compilationMode = CompilationMode.Partial(
            baselineProfileMode = BaselineProfileMode.UseIfAvailable
        )
    ) {
        // No pressHome here, app already in memory
        startActivityAndWait()
        Thread.sleep(3000)  // wait 5 seconds before scrolling
        performTrendingScroll()
    }
}

// Before BP
//  6,393 lines
// 12.1 KB - 11.5  KB
// 735 B  - 758 B

// v1 -
// After BP
// 62,989 lines
// 19.4 KB  - 18.8  KB
// 1.9 KB - 1.9 KB


// v2 -
// After BP
// 63,421 lines
// 21.6 KB  - 21  KB
// 2.3 KB - 2.3 KB