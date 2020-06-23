package com.example.adoptmypet.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.adoptmypet.R


object GlideUtil {

    fun loadImage(view: ImageView, id: String?) {
        val photoUrl = GlideUrl(
            "${BASE_API_URL}api/photos/$id", LazyHeaders.Builder()
                .build()
        )


        GlideApp
            .with(view.context)
            .load(photoUrl)
            .listener(requestListener)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder_image)
            .fitCenter()
            .into(view)
    }

    private val requestListener: RequestListener<Drawable?> = object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: Target<Drawable?>,
            isFirstResource: Boolean
        ): Boolean {
            // todo log exception to central service or something like that

            Log.e("GlideUtil", e?.localizedMessage)
            // important to return false so the error placeholder can be placed
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d("GlideUtil", "success")
            return false
        }
    }
}