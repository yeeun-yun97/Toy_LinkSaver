package com.github.yeeun_yun97.clone.ynmodule.ui.component

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjImageViewUtil {
    companion object {
        fun setDefaultImage(
            fragment: Fragment,
            imageView: ImageView,
            @DrawableRes defaultImage: Int = -1
        ) {
            //clear image and background color
            Glide.with(fragment).clear(imageView)
            imageView.setBackgroundColor(Color.TRANSPARENT)
            Log.d("image data clear", "complete")

            //set default image
            if (defaultImage != -1) {
                val drawable = ResourcesCompat.getDrawable(
                    fragment.resources,
                    defaultImage,
                    fragment.requireActivity().theme
                )
                imageView.setImageDrawable(drawable)
                Log.d("image default", "complete")
            } else {
                Log.d("image default", "null")
            }
        }

        private fun setBackgroundColor(fragment:Fragment, imageView:ImageView, imageUrl:String){
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = Glide.with(fragment).asBitmap().load(imageUrl).submit().get()
                val palette = Palette.from(bitmap).generate()

                launch(Dispatchers.Main) {
                    val swatch = palette.mutedSwatch ?: palette.darkMutedSwatch
                    if (swatch != null) imageView.setBackgroundColor(swatch.rgb)
                }
            }
        }


        fun setImage(
            fragment: Fragment,
            imageView: ImageView,
            imageUrl: String,
            @DrawableRes defaultImage: Int = -1
        ) {
            setDefaultImage(fragment, imageView, defaultImage)

            //set image from url
            if (imageUrl.isNotEmpty()) {
                Glide.with(fragment)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView)
                Log.d("image loaded", "complete")
//                setBackgroundColor(fragment,imageView,imageUrl)
            }
        }

    }

//    fun setImageResource(
//        imageView: ImageView,
//        imageUrl: String,
//        @DrawableRes defaultImage: Int = -1
//    ) {
//        val context = imageView.context
//        //clear image
//        Glide.with(context).clear(imageView)
//
//        //set image from url
//        if (imageUrl.isNotEmpty()) {
//            Glide.with(context)
//                .load(imageUrl)
//                .into(imageView)
//
//            //set default image
//        } else if (defaultImage != -1) {
//            val drawable = ResourcesCompat.getDrawable(
//                context.resources,
//                defaultImage,
//                context.theme
//            )
//            imageView.setImageDrawable(drawable)
//        }
//    }

}