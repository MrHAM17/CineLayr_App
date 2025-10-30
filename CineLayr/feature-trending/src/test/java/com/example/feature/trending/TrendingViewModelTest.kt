package com.example.feature.trending


import androidx.paging.PagingData
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repo = mockk<MovieRepository>()
    private lateinit var viewModel: TrendingViewModel

    @Before
    fun setup() {
        val movies = listOf(
            Movie(1, "Inception", "Sci-Fi", "", "", "2020-01-01", 8.0, 1000),
            Movie(2, "Tenet", "Action", "", "", "2021-01-01", 7.5, 900)
        )
        coEvery { repo.getTrendingMovies() } returns flowOf(PagingData.from(movies))
        viewModel = TrendingViewModel(repo)
    }

    @Test
    fun moviesFlowEmitsPagingData() = runTest {
        val emissions = mutableListOf<PagingData<Movie>>()
        val job = launch { viewModel.movies.collect { emissions.add(it) } }
        this.testScheduler.advanceUntilIdle() // make sure all coroutines finish
        job.cancel()
        assertNotNull(emissions.firstOrNull())
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
