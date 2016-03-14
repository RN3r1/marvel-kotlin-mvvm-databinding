package com.dougritter.marvelmovies

import android.content.Context
import android.databinding.*
import android.widget.ImageView
import com.squareup.picasso.Picasso

object ViewModel {
    class CharacterViewModel(val context: Context, var model: Model.Character) {
        var imageUrl = modelImageUrl()

        fun modelImageUrl(): String = model.thumbnail.path + "/landscape_incredible." + model.thumbnail.extension

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Picasso.with(view.context).load(url).into(view)
            }
        }
    }

}