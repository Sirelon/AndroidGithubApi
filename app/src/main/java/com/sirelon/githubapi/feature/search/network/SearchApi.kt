package com.sirelon.githubapi.feature.search.network

import androidx.annotation.IntRange
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created on 2019-09-05 21:03 for GithubAPi.
 */
interface SearchApi {

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q")
        query: String,
        @Query("page")
        @IntRange(from = 1, to = 1000)
        page: Int,
        @Query("per_page")
        pageLimit: Int,
        @Query("sort")
        sortBy: String
    ): ServerSearchResults

}