package com.dougritter.marvelmovies

import android.content.Context
import android.content.Intent
import android.databinding.*
import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

object ViewModel {
    class CharacterViewModel(val context: Context, var model: Model.Character) {

        companion object {
            val IMAGE_TYPE = "/landscape_incredible."
        }

        var imageUrl = modelImageUrl()

        fun modelImageUrl(): String = model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Picasso.with(view.context).load(url).into(view)
            }
        }

        public fun openDetailActivity() {
            var intent = Intent(context, DetailActivity::class.java)
            val json = Gson().toJson(model)
            intent.putExtra(DetailActivity.MODEL_EXTRA, json)
            context.startActivity(intent)
        }
    }

    class CharacterDetailViewModel(val context: Context, var model: Model.Character) {

        companion object {
            val IMAGE_TYPE = "/standard_fantastic."
        }

        var detailImageUrl = detailImageUrl()

        fun detailImageUrl(): String = model.thumbnail.path + "/standard_fantastic." + model.thumbnail.extension

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:detailImageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Picasso.with(view.context).load(url).into(view)
            }
        }

        lateinit var service: MarvelService
        private var _compoSub = CompositeSubscription()
        private val compoSub: CompositeSubscription
            get() {
                if (_compoSub.isUnsubscribed) {
                    _compoSub = CompositeSubscription()
                }
                return _compoSub
            }

        protected final fun manageSub(s: Subscription) = compoSub.add(s)

        fun unsubscribe() { compoSub.unsubscribe() }

        fun loadCharacter(context: Context) {
            service = MarvelService.create()

            val timestamp = Date().time;
            val hash = Utils.md5(timestamp.toString()+BuildConfig.MARVEL_PRIVATE_KEY+BuildConfig.MARVEL_PUBLIC_KEY)

            manageSub(
                    service.getCharacterDetail(model.id.toString(), timestamp.toString(), BuildConfig.MARVEL_PUBLIC_KEY, hash)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe( { c -> (context as DetailActivity).endCallProgress(c)},
                                    { e -> (context as DetailActivity).endCallProgress(e)
                                        Log.e(DetailActivity::class.java.simpleName, e.message)})
            )

        }


    }

}