package com.example.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TMDBService {
    suspend fun getTrendingMovies(page: Int): TrendingMoviesResponse
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
}

class TMDBServiceImpl(private val client: HttpClient) : TMDBService {

    override suspend fun getTrendingMovies(page: Int): TrendingMoviesResponse {
        return client.get("trending/movie/week") {
            parameter("page", page)
        }.body()
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse {
        return client.get("movie/$movieId") {
            parameter("append_to_response", "credits,videos")
        }.body()
    }
}

@Serializable
data class TrendingMoviesResponse(
    val page: Int,
    val results: List<MovieResponse>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    val overview: String,

//    val poster_path: String?,
//    val backdrop_path: String?,
//    val release_date: String,
//    val vote_average: Double,
//    val vote_count: Int
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)

@Serializable
data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val overview: String,

//    val poster_path: String?,
//    val backdrop_path: String?,
//    val release_date: String,
//    val vote_average: Double,
//    val vote_count: Int,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,

    val runtime: Int?,
    val genres: List<GenreResponse>,
    val credits: CreditsResponse?,
    val videos: VideosResponse?
)

@Serializable
data class GenreResponse(val id: Int, val name: String)

@Serializable
data class CreditsResponse(
    val cast: List<CastResponse>,
    val crew: List<CrewResponse>
)

@Serializable
data class CastResponse(
    val id: Int,
    val name: String,
    val character: String,

//    val profile_path: String?
    @SerialName("profile_path") val profilePath: String?

)

@Serializable
data class CrewResponse(
    val id: Int,
    val name: String,
    val job: String,

//    val profile_path: String?
    @SerialName("profile_path") val profilePath: String?

)

@Serializable
data class VideosResponse(val results: List<VideoResponse>)

@Serializable
data class VideoResponse(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)