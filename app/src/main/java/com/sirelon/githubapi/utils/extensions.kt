package com.sirelon.githubapi.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


/**
 * Created on 2019-09-05 22:12 for GithubAPi.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun EditText.onTextChange(crossinline callback: (text: CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s)
        }
    })
}

open class TextWatcherAdapter : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

}

fun Activity.openBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    // Cases, when device doesn't have any browser.
    runCatching { startActivity(browserIntent) }
}
