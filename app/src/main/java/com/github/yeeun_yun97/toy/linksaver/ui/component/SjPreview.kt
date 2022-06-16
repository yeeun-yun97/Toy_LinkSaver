package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.yeeun_yun97.toy.linksaver.R

enum class SjPreviewMode {
    LOADING, WEB, IMAGE;
}

@SuppressLint("SetJavaScriptEnabled")
class SjPreview @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    private val previewImageView: ImageView
    private val previewWebView: WebView
    private val loading: ProgressBar

    private val requestListener: RequestListener<Drawable>
    private val webViewClient: WebViewClient

    private var mode: SjPreviewMode = SjPreviewMode.LOADING

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_preview, this, false)
        this.addView(view)
        previewImageView = findViewById(R.id.previewImageView)
        previewWebView = findViewById(R.id.previewWebView)
        loading = findViewById(R.id.loading)
        previewImageView.visibility = View.INVISIBLE
        previewWebView.visibility = View.INVISIBLE

        requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean = false

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onPreviewLoadFinished()
                return false
            }
        }

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                onPreviewLoadFinished()
            }
        }
        previewWebView.webViewClient = webViewClient              // prevent opening browser
        previewWebView.settings.javaScriptEnabled = true            // show javascript needed pages

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        previewWebView.setOnTouchListener { view, _ ->
            l!!.onClick(view)
            true
        }
    }

    private fun onPreviewLoadFinished() {
        loading.visibility = View.INVISIBLE
        when (mode) {
            SjPreviewMode.WEB -> previewWebView.visibility = View.VISIBLE
            SjPreviewMode.IMAGE -> previewImageView.visibility = View.VISIBLE
            else -> Log.d("onPreviewLoadFinished","SjPreview NONE")
        }
    }

    fun setPreview(isVideo: Boolean, contentUrl: String?, previewUrl: String?) {
        // clear previous image loaded
        mode = SjPreviewMode.LOADING
        Glide.with(context).clear(previewImageView)
        previewWebView.stopLoading()
        previewImageView.visibility = View.INVISIBLE
        previewWebView.visibility = View.INVISIBLE

        // case 01 have preview Image
        if (!previewUrl.isNullOrEmpty()) {
            mode = SjPreviewMode.IMAGE
            showImagePreview(previewUrl)

            // case 02 is video type
        } else if (isVideo && !contentUrl.isNullOrEmpty()) {
            mode = SjPreviewMode.IMAGE
            showVideoPreview(contentUrl)

            //case 03 is not video type
        } else if (!isVideo && !contentUrl.isNullOrEmpty()) {
            mode = SjPreviewMode.WEB
            showWebPreview(contentUrl)

            //have nothing to show
        } else {
            mode = SjPreviewMode.IMAGE
            showNoImagePreview()
        }
    }

    private fun showWebPreview(url: String) {
        previewWebView.loadUrl(url)
    }

    private fun showImagePreview(url: String) {
        Glide.with(context)
            .load(url)
            .listener(requestListener)
            .centerCrop()
            .into(previewImageView)
    }

    private fun showVideoPreview(url: String) {
        Glide.with(context)
            .load(url)
            .listener(requestListener)
            .centerCrop()
            .override(720, 360)
            .into(previewImageView)
    }

    private fun showNoImagePreview() {
        Glide.with(context)
            .load(R.drawable.ic_icons8_no_image_100)
            .listener(requestListener)
            .into(previewImageView)
    }


}