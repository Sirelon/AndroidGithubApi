package com.sirelon.githubapi.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created on 2019-09-05 20:40 for GithubAPi.
 */
fun createSimpleRetrofit(baseUrl: String): Retrofit {
    val gson = createGson()
    val client = createOkkHttp()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun createOkkHttp() =
    OkHttpClient.Builder().build()

fun createGson() = Gson()