pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CineLayr"
include(":app")

include(":core:database")
include(":core:designsystem")
include(":core:navigation")
include(":core:network")

include(":domain")
include(":data")

include(":feature-trending")
include(":feature-details")
include(":feature-watchlist")

include(":benchmark")
