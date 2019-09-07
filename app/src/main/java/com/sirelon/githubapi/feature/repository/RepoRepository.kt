package com.sirelon.githubapi.feature.repository

import android.util.Log
import androidx.annotation.WorkerThread

/**
 * I Have no idea, how to name it properly =)
 *
 * Basically, it's just wrapper over dao. but in future, we can easly implement all these feature in some server side.
 *
 * Created on 2019-09-07 13:18 for GithubAPi.
 */
class RepoRepository(private val repositoryDao: RepositoryDao) {

    fun loadAll() = repositoryDao.loadAll()

    @WorkerThread
    suspend fun markAsViewed(item: Repository) {
        repositoryDao.insert(item)
    }

    @WorkerThread
    suspend fun remove(repository: Repository) {
        repositoryDao.delete(repository)
    }

    @WorkerThread
    suspend fun updateRepositoriesList(list: List<Repository>) {
        val updated = repositoryDao.updateAll(list)
        Log.d("Sirelon", "updateRepositoriesList: updated $updated")
    }

}