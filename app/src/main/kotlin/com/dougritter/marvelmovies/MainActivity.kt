package com.dougritter.marvelmovies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var service: MarvelService
    private var _compoSub = CompositeSubscription()
    private val compoSub: CompositeSubscription
        get() {
            if (_compoSub.isUnsubscribed()) {
                _compoSub = CompositeSubscription()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Subscription) = compoSub.add(s)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service = MarvelService.create()
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun charactersList(characterResponse: Model.CharacterResponse) {
        val adapter = CharactersAdapter(characterResponse)
        recyclerView.adapter = adapter
    }

    fun endCallProgress(response: Model.CharacterResponse?, m: String) {
        if (response != null) {
            charactersList(response)

        }
    }

    fun startCallProgress() {
        Log.e(MainActivity::class.java.simpleName, "progress started")
    }

    override fun onResume() {
        super.onResume()
        val timestamp = Date().time;
        val hash = Utils.md5(timestamp.toString()+BuildConfig.MARVEL_PRIVATE_KEY+BuildConfig.MARVEL_PUBLIC_KEY)
        Log.e(MainActivity::class.java.simpleName, hash)

        manageSub(
                service.getCharacters(timestamp.toString(), BuildConfig.MARVEL_PUBLIC_KEY, hash)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( { c -> endCallProgress(c, "Characters number: ${c.data.count} Copyright: ${c.attributionText}")},
                                { e -> endCallProgress(null, e.message?:"Unknown error happened during getting characters")
                                Log.e(MainActivity::class.java.simpleName, e.message)})
        )

        startCallProgress()

    }

    override fun onDestroy() {
        compoSub.unsubscribe()
        super.onDestroy()
    }

}

























