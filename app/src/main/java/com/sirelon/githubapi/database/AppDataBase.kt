package com.sirelon.githubapi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sirelon.githubapi.feature.ui.repository.Repository
import com.sirelon.githubapi.feature.repository.RepositoryDao

/**
 * Created on 2019-09-05 20:49 for GithubAPi.
 */
@Database(entities = [Repository::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}