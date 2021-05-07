package com.nerdscorner.guiad.stats.ui.mvp.view

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.guiad.stats.ui.activities.CreditsActivity

class CreditsView(activity: CreditsActivity) : BaseActivityView(activity) {
    private val webView: WebView = activity.findViewById(R.id.web_view)

    init {
        webView.settings.javaScriptEnabled = true
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
    }
}
