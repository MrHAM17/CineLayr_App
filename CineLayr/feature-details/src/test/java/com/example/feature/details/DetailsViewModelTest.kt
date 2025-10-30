package com.example.feature.details

import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repo = mockk<MovieRepository>()
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        val details = MovieDetails(
            10, "Interstellar", "Epic space story", "", "", "2014-11-07",
            9.0, 12000, 169, emptyList(), emptyList(), emptyList(), emptyList()
        )
        coEvery { repo.getMovieDetails(10) } returns details
        coEvery { repo.isMovieInWatchlist(10) } returns false
        coEvery { repo.toggleWatchlistStatus(10, any()) } returns Unit
        viewModel = DetailsViewModel(repo, 10)
    }

    @Test
    fun uiStateSuccessAfterLoad() = runTest {
        assert(viewModel.uiState.value is DetailsUiState.Success)
    }

    @Test
    fun toggleWatchlistCallsRepo() = runTest {
        viewModel.toggleWatchlist()
        // Advance the coroutine until all pending tasks are executed
        this.testScheduler.advanceUntilIdle()

        coVerify { repo.toggleWatchlistStatus(10, any()) }
    }

}


@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}