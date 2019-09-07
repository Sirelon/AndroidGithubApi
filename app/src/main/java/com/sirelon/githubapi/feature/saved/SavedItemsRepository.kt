package com.sirelon.githubapi.feature.saved

import com.sirelon.githubapi.feature.repository.RepositoryDao

/**
 * Separate class, instead of call dao's method directly, 'cause in future we can save such information somehow in server side)
 *
 * Created on 2019-09-07 10:51 for GithubAPi.
 */
@Deprecated("ASd")
class SavedItemsRepository(private val dao: RepositoryDao) {
    fun loadAll() = dao.loadAll()
}