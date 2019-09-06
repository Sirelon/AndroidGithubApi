package com.sirelon.githubapi.feature.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Created on 2019-09-06 22:50 for GithubAPi.
 */
private const val TOKEN_KEY = ".token"

// I don't want to make it as singleton object,
// 'cause in future this class can be replaced to interface and than it will be more safety way to change logic for app session
class AppSession(private val context: Context) {

    private val sharedPreferences: SharedPreferences

    // Save this token somewhere in keystore, database or sharedpreferences
    private var authToken: String? = null

    init {
        sharedPreferences = context.getSharedPreferences("GitHubApi", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun setAuthToken(token: String) {
        this.authToken = token
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthToken() = authToken

    fun isLoggedIn() = authToken != null

    fun invalidate() {
        authToken = null
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }
}