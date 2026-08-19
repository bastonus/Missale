package com.example.liturgy.gabc

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

/**
 * Official Gregorian Chant renderer powered directly by the jgabc / exsurge JavaScript and SVG engine.
 * Renders authentic Solesmes square neumes, ligatures, custos, horizontal/vertical episemas,
 * and text hyphenation using the exact codebase from Benjamin Bloomfield's jgabc project.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExsurgeChantView(
    rawGabc: String,
    modifier: Modifier = Modifier,
    useRedStaff: Boolean = true,
    zoomScale: Float = 1.0f
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isDark = isSystemInDarkTheme() || (MaterialTheme.colorScheme.background.luminance() < 0.5f)

    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isPageLoaded by remember { mutableStateOf(false) }
    var contentHeightDp by remember { mutableIntStateOf(160) }

    val cleanGabcJson = remember(rawGabc) {
        JSONObject.quote(rawGabc)
    }

    LaunchedEffect(isPageLoaded, cleanGabcJson, isDark, useRedStaff, zoomScale) {
        if (isPageLoaded && webViewRef != null) {
            val js = "if (typeof renderScore === 'function') { renderScore($cleanGabcJson, window.innerWidth * $zoomScale, $isDark, $useRedStaff); }"
            webViewRef?.evaluateJavascript(js, null)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = (contentHeightDp + 20).dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height((contentHeightDp + 10).dp),
            factory = { ctx ->
                WebView(ctx).apply {
                    setBackgroundColor(Color.TRANSPARENT)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        allowFileAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        textZoom = (100 * zoomScale).toInt()
                    }

                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onHeightChanged(heightPx: Float) {
                            post {
                                val dpVal = (heightPx / ctx.resources.displayMetrics.density).toInt()
                                if (dpVal in 80..2000 && dpVal != contentHeightDp) {
                                    contentHeightDp = dpVal
                                }
                            }
                        }
                    }, "AndroidBridge")

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isPageLoaded = true
                            val js = "if (typeof renderScore === 'function') { renderScore($cleanGabcJson, window.innerWidth * $zoomScale, $isDark, $useRedStaff); }"
                            view?.evaluateJavascript(js, null)
                        }
                    }

                    webChromeClient = WebChromeClient()
                    loadUrl("file:///android_asset/jgabc/score_viewer.html")
                    webViewRef = this
                }
            },
            update = { view ->
                view.settings.textZoom = (100 * zoomScale).toInt()
                if (isPageLoaded) {
                    val js = "if (typeof renderScore === 'function') { renderScore($cleanGabcJson, window.innerWidth * $zoomScale, $isDark, $useRedStaff); }"
                    view.evaluateJavascript(js, null)
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
            webViewRef = null
        }
    }
}
