package com.sirelon.githubapi.di

import android.app.Application
import androidx.room.Room
import com.sirelon.githubapi.database.AppDataBase
import com.sirelon.githubapi.feature.auth.AppSession
import com.sirelon.githubapi.feature.auth.AuthAPI
import com.sirelon.githubapi.feature.repository.RepoRepository
import com.sirelon.githubapi.feature.saved.SavedItemsViewModel
import com.sirelon.githubapi.feature.search.network.SearchApi
import com.sirelon.githubapi.feature.search.SearchRepository
import com.sirelon.githubapi.feature.search.ui.SearchRepoViewModel
import com.sirelon.githubapi.network.createSimpleRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit

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
            modules(
                listOf(
                    commonModule(),
                    repositoryModule(),
                    searchModule(),
                    viewedRepositoriesModule()
                )
            )
        }
    }

    /**
     * Create module for common (shared) definitions, which can be used in any features
     */
    private fun commonModule() = module {
        single { AppSession(androidContext()) }

        single { createSimpleRetrofit(androidContext(), get(), getProperty(BASE_URL)) }
        single {
//            Room.inMemoryDatabaseBuilder(
//                androidContext(),
//                AppDataBase::class.java
//            ).build()
            Room.databaseBuilder(
                androidContext(),
                AppDataBase::class.java,
                ".githubApiDatabase"
            ).build()
        }

        factory { get<Retrofit>().create(AuthAPI::class.java) }
    }

    /**
     * Module for repository feature
     */
    private fun repositoryModule() = module {
        single { get<AppDataBase>().repositoryDao() }
        factory { RepoRepository(get()) }
    }

    /**
     * Module for search repositories feature
     */
    private fun searchModule() = module {
        single { get<Retrofit>().create(SearchApi::class.java) }
        factory { SearchRepository(get()) }
        viewModel { SearchRepoViewModel(get(), get()) }
    }

    private fun viewedRepositoriesModule() = module {
        viewModel { SavedItemsViewModel(get()) }
    }
}