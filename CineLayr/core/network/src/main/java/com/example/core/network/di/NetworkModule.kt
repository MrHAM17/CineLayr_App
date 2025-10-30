package com.example.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
//import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.example.core.network.BuildConfig
import com.example.core.network.TMDBService
import com.example.core.network.TMDBServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val Core_Network_NetworkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP: $message")
                    }
                }
                level = LogLevel.HEADERS
            }
            defaultRequest {
                url("https://api.themoviedb.org/3/")
                header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                header("Accept", "application/json")
            }
        }
    }
    singleOf(::TMDBServiceImpl) { bind<TMDBService>() }
}


//val client = HttpClient {
//    install(ContentNegotiation) {
//        json(Json { ignoreUnknownKeys = true })
//    }
//    install(Logging) {
//        level = LogLevel.BODY
//    }
//    defaultRequest {
//        url("https://api.themoviedb.org/3/")
//        header("Accept", "application/json")
//    }
//}
//
//// Usage
//suspend fun getPopularMovies(): HttpResponse {
//    return client.get("movie/popular") {
//        parameter("api_key", BuildConfig.TMDB_API_KEY)
//    }
//}
