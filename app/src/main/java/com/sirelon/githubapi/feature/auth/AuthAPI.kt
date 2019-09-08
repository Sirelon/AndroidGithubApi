package com.sirelon.githubapi.feature.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created on 2019-09-06 23:16 for GithubAPi.
 */
@Keep
interface AuthAPI {
    @POST("https://github.com/login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun authorize(@Body request: AuthRequest): TokenResponse
}

@Keep
class AuthRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)

@Keep
class TokenResponse(
    @SerializedName("access_token")
    val token: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
)