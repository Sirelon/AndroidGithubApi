package com.sirelon.githubapi.feature.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sirelon.githubapi.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class AuthActivity : AppCompatActivity() {

    // Excuse me, I do not use any architecture here. I will call authorize directly from this service
    private val authAPI by inject<AuthAPI>()
    private val appSession by inject<AppSession>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        // Would be better to create some ConfigHolder or smt like it for hold all these clientKey and secret key
        val clientKey = "757e44567765ae677c26"
        val clientSecret = "e1ca55ee83391c41ef64b2b1c5bf3b924e3ca3d1"

        val scheme = "com.sirelon.githubapi"
        val redirectUri = "$scheme://auth"
        val redirectUrl = "$redirectUri/githubapi"

        // It took me some time to understand whny I cannot login to GitHub  :|
        // https://stackoverflow.com/questions/44425233/why-is-the-github-authorization-button-in-the-oauth-flow-grayed-out
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request ?: return false

                val url = request.url
                if (url.scheme == scheme) {
                    val code = url.getQueryParameter("code")
                    code ?: return false
                    val authRequest = AuthRequest(code, clientKey, clientSecret)
                    loginToServer(authRequest)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        webView.loadUrl("https://github.com/login/oauth/authorize?client_id=$clientKey&redirect_uri=$redirectUrl")
    }

    private fun loginToServer(authRequest: AuthRequest) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { authAPI.authorize(authRequest) }
                appSession.setAuthToken(response.token)

                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AuthActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
