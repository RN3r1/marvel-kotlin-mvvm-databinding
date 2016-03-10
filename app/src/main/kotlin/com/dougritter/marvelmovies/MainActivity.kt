package com.dougritter.marvelmovies

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import android.R as AR

class MainActivity : AppCompatActivity() {

    lateinit var characterResponse: Model.CharacterResponse

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

        val timestamp = "1457563230"
        val privateKey = "083203d3fad457db9c2733611f21a71e1f8a78fd"
        val publicKey = "a4734679191fb19cf3573a65926dd720"

        Utils.md5(timestamp+privateKey+publicKey)

        Log.e(MainActivity::class.java.simpleName, Utils.md5(timestamp+privateKey+publicKey))

        service = MarvelService.create()

    }

    fun endCallProgress(m : String, length: Int) {
        Toast.makeText(this, m, length).show()
        Log.e(MainActivity::class.java.simpleName, "progress finished")
    }

    fun startCallProgress() {
        Log.e(MainActivity::class.java.simpleName, "progress started")
    }

    override fun onResume() {
        super.onResume()

        manageSub(
                service.getCharacters("1457563230", "a4734679191fb19cf3573a65926dd720", "03b8b27175b7c3d2e7e732f3d464e87b")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( { c -> endCallProgress("Characters number: ${c.data.count} Copyright: ${c.attributionText}",
                                Toast.LENGTH_SHORT)
                        characterResponse = c},
                                { e -> endCallProgress(e.message?:"Unknown error happened during getting characters",
                                        Toast.LENGTH_LONG)
                                Log.e(MainActivity::class.java.simpleName, e.message)})
        )

        startCallProgress()


    }

    override fun onDestroy() {
        compoSub.unsubscribe()
        super.onDestroy()
    }

}

























