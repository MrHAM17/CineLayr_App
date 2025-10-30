package com.example.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isInWatchlist: Boolean = false
)

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int?,
    val genres: List<Genre>,
    val cast: List<CastMember>,
    val crew: List<CrewMember>,
    val videos: List<Video>
)

data class Genre(val id: Int, val name: String)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?
)

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    val profilePath: String?
)

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)