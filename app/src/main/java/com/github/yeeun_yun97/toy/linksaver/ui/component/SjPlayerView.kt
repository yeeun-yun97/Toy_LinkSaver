package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceView

class SjPlayerView(context: Context, attr:AttributeSet) : SurfaceView(context, attr) {
    fun setThumbnailImage(thumbnail: Bitmap){
        val canvas = Canvas(thumbnail)
        this.draw(canvas)
    }


}
