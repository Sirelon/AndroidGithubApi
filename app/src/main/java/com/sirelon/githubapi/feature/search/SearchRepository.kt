package com.sirelon.githubapi.feature.search

import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.BySort.FORKS
import com.sirelon.githubapi.feature.search.BySort.STARS
import com.sirelon.githubapi.feature.search.BySort.UPDATED
import com.sirelon.githubapi.feature.search.ByType.DESCRIPTION
import com.sirelon.githubapi.feature.search.ByType.OWNER
import com.sirelon.githubapi.feature.search.ByType.README
import com.sirelon.githubapi.feature.search.ByType.TITLE

/**
 * Created on 2019-09-05 21:27 for GithubAPi.
 */
class SearchRepository(private val searchApi: SearchApi) {

    private val criteria: SearchCriteria = SearchCriteria("", 1, TITLE, STARS)

    suspend fun searchRepositories(query: String): List<Repository> {
        val queryForServer = criteria.query(query).constructQueryForServer()

        val sortParameter = when (criteria.sort) {
            STARS -> "stars"
            FORKS -> "forks"
            UPDATED -> "updated"
        }
        val searchResults =
            searchApi.searchRepositories(
                query = queryForServer,
                page = criteria.page,
                pageLimit = criteria.pageLimit,
                sortBy = sortParameter
            )
        val list = searchResults.result.map(ServerRepository::map)
        return list
    }

    private fun SearchCriteria.constructQueryForServer(): String {
        // https://help.github.com/en/articles/searching-for-repositories#search-by-repository-name-description-or-contents-of-the-readme-file
        val type = when (type) {
            TITLE -> "in:title" // "in:name, as on documentation, doesn't work ¯ \ _ (ツ) _ / ¯ "
            DESCRIPTION -> "in:description"
            README -> "in:readme"
            OWNER -> "repo:owner/name"
        }
        return "$searchQuery+$type"
    }
}

fun ServerRepository.map() =
    Repository(id = id, name = name, description = description, starCount = countOfStars, url = url)