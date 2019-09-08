package com.sirelon.githubapi.feature.search

/**
 * Created on 2019-09-06 15:15 for GithubAPi.
 */
// Mutable
data class SearchCriteria(
    var keyword: String,
    var page: Int,
    val type: ByType,
    val sort: BySort
) {
    //
    var pageLimit: Int = 15
}

enum class ByType {
    TITLE, DESCRIPTION, README, OWNER
}

enum class BySort {
    STARS, FORKS, UPDATED
}