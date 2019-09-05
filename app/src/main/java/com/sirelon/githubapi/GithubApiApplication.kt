package com.sirelon.githubapi

import android.app.Application
import com.sirelon.githubapi.di.Injector

/**
 * Created on 2019-09-05 20:27 for GithubAPi.
 */
class GithubApiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

}