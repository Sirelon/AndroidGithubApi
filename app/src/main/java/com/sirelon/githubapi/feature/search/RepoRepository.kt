package com.sirelon.githubapi.feature.search

import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.repository.RepositoryDao

/**
 * I Have no idea, how to name it properly =)
 *
 * Created on 2019-09-05 21:27 for GithubAPi.
 */
class RepoRepository(private val repositoryDao: RepositoryDao, private val searchApi: SearchApi) {

    fun loadAll() = repositoryDao.loadAll()

    suspend fun searchRepositories(query: String) {
        val searchResults = searchApi.searchRepositories(query, 1, 15)
        val list = searchResults.result.map(ServerRepository::map)
        repositoryDao.replaceAll(list)
    }
}

fun ServerRepository.map() =
    Repository(id = id, name = name, description = description, starCount = countOfStars)