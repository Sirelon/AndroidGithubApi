package com.sirelon.githubapi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext


/**
 * Created on 2019-09-05 22:12 for GithubAPi.
 */
/**
 * A util function, that make throttle possible on Channel.
 */
fun <T> Channel<T>.throttle(
    wait: Long = 1500,
    context: CoroutineContext = Dispatchers.Default
): ReceiveChannel<T> =
    CoroutineScope(context).produce(context) {

        var mostRecent: T

        consumeEach {
            mostRecent = it

            // Wait some time
            delay(wait)

            // receive items from channel until it would not be empty
            while (!isEmpty) {
                mostRecent = receive()
            }

            // Send to SendChannel item, which was the last one
            send(mostRecent)
        }
    }

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


fun Activity.hideKeyboard() {
    val focus = currentFocus
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focus?.windowToken, 0)
    focus?.clearFocus()
}