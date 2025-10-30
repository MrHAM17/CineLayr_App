package com.example.feature.details.navigation

import com.example.core.navigation.NavigationDestination

object DetailsDestination : NavigationDestination {
    override val route = "details_route"
    override val destination = "details_destination"
    const val movieIdArg = "movieId"
    val routeWithArgs = "$route/{$movieIdArg}"
}