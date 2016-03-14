package com.dougritter.marvelmovies

import android.content.Context
import android.content.res.Resources
import android.databinding.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

object ViewModel {
    class CharacterViewModel(val context: Context, var model: Model.Character) {
        /*public val characterImageView: ObservableField<Drawable> = ObservableField()
        public var bindableFieldTarget: BindableFieldTarget = BindableFieldTarget(characterImageView, context.resources)

        init {
            Picasso.with(context).load(imageUrl()).into(bindableFieldTarget)
        }*/

        public var imageUrl = modelImageUrl()

        public fun modelImageUrl(): String = model.thumbnail.path + "/landscape_incredible." + model.thumbnail.extension

        /*val attributeString = "android:src"
        val methodString = "setImageResource"

        @BindingMethods({
            @BindingMethod(type = android.widget.ImageView.class, attribute = "android:src", method = "setImageResource")

            @BindingMethod(type = android.widget.ImageView.class, attribute = "", method = "setImageResource").
        })*/

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Picasso.with(view.context).load(url).into(view)
            }
        }



        /*@BindingAdapter("android:src")
        public fun loadImage(view: ImageView, url: String) {
            Picasso.with(context).load(modelImageUrl()).into(view)

        }*/


        /*class BindableFieldTarget(val observableField: ObservableField<Drawable>, val resources: Resources) : com.squareup.picasso.Target {

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                observableField.set(BitmapDrawable(resources, bitmap))
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                observableField.set(errorDrawable)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                observableField.set(placeHolderDrawable)
            }
        }*/


    }

}