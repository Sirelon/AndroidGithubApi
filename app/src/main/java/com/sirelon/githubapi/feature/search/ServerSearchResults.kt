package com.sirelon.githubapi.feature.search

import com.google.gson.annotations.SerializedName

/**
 * Created on 2019-09-05 21:09 for GithubAPi.
 */
class ServerSearchResults(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val result: List<ServerRepository>
)

class ServerRepository(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("stargazers_count")
    val countOfStars: Int,
    @SerializedName("url")
    val url: String
)