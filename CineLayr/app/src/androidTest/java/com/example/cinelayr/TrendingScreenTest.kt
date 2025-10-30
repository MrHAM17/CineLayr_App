//package com.example.cinelayr
//
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performScrollTo
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.feature.trending.TrendingScreen
//import com.example.feature.trending.TrendingViewModel
//import kotlinx.coroutines.test.runTest
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class TrendingScreenTest {
//
//    @get:Rule
//    val rule = createAndroidComposeRule<MainActivity>()
//
//    @Test
//    fun scrollAndOpenDetails() = runTest {
//        val fakeRepo = FakeMovieRepository() // returns PagingData.from(listOf(...))
//        val viewModel = TrendingViewModel(fakeRepo)
//
//        rule.setContent {
//            TrendingScreen(
//                movies = viewModel.movies.collectAsLazyPagingItems(),
//                onMovieClick = {}
//            )
//        }
//
//        // wait for Compose recomposition
//        rule.waitForIdle()
//
//        rule.onNodeWithTag("movie_item_1").performScrollTo().performClick()
//        rule.onNodeWithTag("details_fab").assertExists()
//    }
//
//    @Test
//    fun toggleTheme() {
//        rule.onNodeWithTag("topbar_theme_toggle").performClick()
//    }
//}
