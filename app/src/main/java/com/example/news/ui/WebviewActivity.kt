package com.example.news.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.news.R
import com.example.news.databinding.ActivityWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    private var url: String? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
        url = intent.getStringExtra(URL)
        title = intent.getStringExtra(TITLE)

        initToolbar()
        initData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initData() {
        binding.webView.settings.allowFileAccess = false
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.setInitialScale(1)
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false
        binding.webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("PaymentWebView", consoleMessage.message())
                return true
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.isVisible = newProgress != 100
            }
        }
        url?.let { binding.webView.loadUrl(it) }

    }

    private fun initToolbar() {
        title?.let {
            binding.toolbar.title = it
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left)
                }
            }
            true
        }
    }

    companion object {
        const val URL = "URL"
        const val TITLE = "TITLE"
    }
}