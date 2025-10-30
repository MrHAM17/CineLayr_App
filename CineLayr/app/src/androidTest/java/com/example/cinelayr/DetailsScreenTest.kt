//package com.example.cinelayr
//
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.performClick
//import com.example.feature.details.DetailsScreen
//import com.example.feature.details.DetailsUiState
//import org.junit.Rule
//import org.junit.Test
//
//class DetailsScreenTest {
//    @get:Rule
//    val rule = createAndroidComposeRule<MainActivity>()
//
//    @Test
//    fun fabVisibleAndClickable() {
//        val fakeViewModel = FakeDetailsViewModel(
//            uiState = DetailsUiState.Success(
//                movieDetails = testMovieDetails
//            ),
//            isInWatchlist = false
//        )
//
//        rule.setContent {
//            DetailsScreen(
//                uiState = fakeViewModel.uiState,
//                isInWatchlist = fakeViewModel.isInWatchlist,
//                onBackClick = {},
//                onToggleWatchlist = {}
//            )
//        }
//
//        rule.onNodeWithTag("details_fab").assertIsDisplayed()
//        rule.onNodeWithTag("details_fab").performClick()
//    }
//
//}
