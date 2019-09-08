package com.sirelon.githubapi.feature.search.network

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Created on 2019-09-05 21:09 for GithubAPi.
 */
@Keep
class ServerSearchResults(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val result: List<ServerRepository>
)

@Keep
class ServerRepository(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("stargazers_count")
    val countOfStars: Int,
    @SerializedName("html_url")
    val url: String
)