package com.sirelon.githubapi.feature.search

import android.util.TimingLogger
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.BySort.FORKS
import com.sirelon.githubapi.feature.search.BySort.STARS
import com.sirelon.githubapi.feature.search.BySort.UPDATED
import com.sirelon.githubapi.feature.search.ByType.DESCRIPTION
import com.sirelon.githubapi.feature.search.ByType.OWNER
import com.sirelon.githubapi.feature.search.ByType.README
import com.sirelon.githubapi.feature.search.ByType.TITLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip

/**
 * Created on 2019-09-05 21:27 for GithubAPi.
 */
class SearchRepository(private val searchApi: SearchApi) {

    private val criteria: SearchCriteria = SearchCriteria("", 1, TITLE, STARS)

    // enter on terminal to see logs "adb shell setprop log.tag.Sirelon VERBOSE"
    private val timings = TimingLogger("Sirelon", "Measure Different Approaches")

    fun searchViaFlows(query: String): Flow<List<Repository>> {
        timings.reset()

        criteria.query(query)
        val byDescriptionFlow = callApiFlow(criteria.copy(type = DESCRIPTION))
            .onStart { timings.addSplit("DESCRIPTION:onStart") }
            .onCompletion() { timings.addSplit("DESCRIPTION:onComplete") }

        val byNameFlow = callApiFlow(criteria)
            .onStart { timings.addSplit("NAME:onStart") }
            .onCompletion() { timings.addSplit("NAME:onComplete") }

        return byNameFlow
            .zip(byDescriptionFlow) { list1, list2 ->
                timings.dumpToLog()
                list1 + list2
            }
            .flowOn(Dispatchers.IO)
    }

    suspend fun searchViaAsync(query: String): List<Repository> {
        timings.reset()
        timings.addSplit("DESCRIPTION_NAME:onStart")

        criteria.query(query)
        return coroutineScope {
            val listByNameDeffered = async { callApi(criteria) }
            val listByDescriptionDeffered = async { callApi(criteria.copy(type = DESCRIPTION)) }

            val listByName = listByNameDeffered.await()
            timings.addSplit("NAME:onComplete")

            val listByDescription = listByDescriptionDeffered.await()
            timings.addSplit("DESCRIPTION:onComplete")

            timings.dumpToLog()
            listByName + listByDescription
        }
    }

    private fun callApiFlow(criteria: SearchCriteria): Flow<List<Repository>> {
        return flow {
            val result = callApi(criteria)
            emit(result)
        }
    }

    private suspend fun callApi(criteria: SearchCriteria): List<Repository> {
        val queryForServer = criteria.constructQueryForServer()
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