package com.sirelon.githubapi.feature.search

/**
 * Created on 2019-09-06 15:15 for GithubAPi.
 */
// Mutable
class SearchCriteria(
    var searchQuery: String,
    var page: Int,
    val type: ByType,
    val sort: BySort
) {
    //
    val pageLimit: Int = 15
}

fun SearchCriteria.page(page: Int) = apply { this.page = page }
fun SearchCriteria.query(query: String) = apply { this.searchQuery = query }

enum class ByType {
    TITLE, DESCRIPTION, README, OWNER
}

enum class BySort {
    STARS, FORKS, UPDATED
}