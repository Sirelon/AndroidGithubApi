package com.sirelon.githubapi.network

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.sirelon.githubapi.feature.auth.AppSession
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created on 2019-09-05 20:40 for GithubAPi.
 */
fun createSimpleRetrofit(context: Context, appSession: AppSession, baseUrl: String): Retrofit {
    val gson = createGson()
    val client = createOkkHttp(context, appSession)

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun createOkkHttp(context: Context, appSession: AppSession) =
    OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(context))
        .addInterceptor {
            val request: Request
            if (appSession.isLoggedIn()) {
                val token = appSession.getAuthToken()
                request = it
                    .request()
                    .newBuilder()
                    .header("Authorization", "token $token")
                    .build()
            } else {
                request = it.request()
            }
            it.proceed(request)
        }
        .build()

fun createGson() = Gson()