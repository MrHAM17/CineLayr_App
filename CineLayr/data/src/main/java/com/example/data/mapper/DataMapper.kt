package com.example.data.mapper

import com.example.core.database.entity.MovieEntity
import com.example.core.database.entity.WatchlistMovieEntity
import com.example.core.network.CastResponse
import com.example.core.network.CrewResponse
import com.example.core.network.GenreResponse
import com.example.core.network.MovieDetailsResponse
import com.example.core.network.MovieResponse
import com.example.core.network.VideoResponse
import com.example.domain.model.CastMember
import com.example.domain.model.CrewMember
import com.example.domain.model.Genre
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.model.Video

// Network to Entity mapping
fun MovieResponse.toMovieEntity(isInWatchlist: Boolean = false): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = isInWatchlist
    )
}

// Entity to Domain mapping
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = isInWatchlist
    )
}

// MovieDetails Response to Domain mapping
fun MovieDetailsResponse.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        runtime = runtime,
        genres = genres.map { it.toGenre() },
        cast = credits?.cast?.map { it.toCastMember() } ?: emptyList(),
        crew = credits?.crew?.map { it.toCrewMember() } ?: emptyList(),
        videos = videos?.results?.map { it.toVideo() } ?: emptyList()
    )
}


fun MovieDetails.toMovieEntity(isInWatchlist: Boolean = false): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = isInWatchlist
    )
}

fun GenreResponse.toGenre(): Genre {
    return Genre(id = id, name = name)
}

fun CastResponse.toCastMember(): CastMember {
    return CastMember(
        id = id,
        name = name,
        character = character,
        profilePath = profilePath
    )
}

fun CrewResponse.toCrewMember(): CrewMember {
    return CrewMember(
        id = id,
        name = name,
        job = job,
        profilePath = profilePath
    )
}

fun VideoResponse.toVideo(): Video {
    return Video(
        id = id,
        key = key,
        name = name,
        site = site,
        type = type
    )
}



fun MovieEntity.toWatchlistMovieEntity(): WatchlistMovieEntity {
    return WatchlistMovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = true, // ✅ mark it explicitly
        createdAt = System.currentTimeMillis()
    )
}

fun WatchlistMovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = isInWatchlist
    )
}


// Widget - Network Response to Domain mapping
fun MovieResponse.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = false
    )
}