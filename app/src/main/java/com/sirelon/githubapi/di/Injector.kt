package com.sirelon.githubapi.di

import android.app.Application
import androidx.room.Room
import com.sirelon.githubapi.database.AppDataBase
import com.sirelon.githubapi.network.createSimpleRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Created on 2019-09-05 20:27 for GithubAPi.
 */
object Injector {

    private const val BASE_URL: String = "baseUrl"

    /**
     * Should be called only once per application start
     */
    fun init(application: Application) {
        startKoin {
            properties(mapOf(BASE_URL to "https://api.github.com/"))
            androidLogger()
            androidContext(application)
            commonModule()
            repositoryModule()
        }
    }

    /**
     * Create module for common (shared) definitions, which can be used in any features
     */
    private fun commonModule() = module {
        single { createSimpleRetrofit(getProperty(BASE_URL)) }
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDataBase::class.java,
                ".githubApiDatabase"
            )
        }
    }

    /**
     * Module for repository feature
     */
    private fun repositoryModule() = module {
        factory { get<AppDataBase>().repositoryDao() }
    }
}