package com.sirelon.githubapi.feature.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sirelon.githubapi.database.CommonDao

/**
 * Created on 2019-09-05 20:52 for GithubAPi.
 */
@Dao
interface RepositoryDao : CommonDao<Repository> {
//    @Query("SELECT * FROM repository WHERE name")
//    fun searchRepositories(query: String)

    @Query("SELECT * FROM repository")
    fun loadAll(): LiveData<List<Repository>>

}