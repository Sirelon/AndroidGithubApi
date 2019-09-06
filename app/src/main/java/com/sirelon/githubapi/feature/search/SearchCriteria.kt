package com.sirelon.githubapi.feature.search

/**
 * Created on 2019-09-06 15:15 for GithubAPi.
 */
data class SearchCriteria(
    val searchQuery: String,
    val type: ByType,
    val page: Int,
    val sort: BySort
)

enum class ByType {
    TITLE, DESCRIPTION
}

enum class BySort {
    STARS, FORKS, UPDATED
}