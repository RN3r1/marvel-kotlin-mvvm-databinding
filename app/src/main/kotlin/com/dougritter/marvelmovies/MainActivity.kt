package com.dougritter.marvelmovies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class MainActivity : AppCompatActivity() {

    val defaultLimit = 20
    var countLimit = 0
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
        val linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout
        recyclerView.addOnScrollListener(InfiniteScrollListener({requestMoreNews()}, linearLayout))

    }

    fun charactersList(characterResponse: Model.CharacterResponse) {
        val adapter = CharactersAdapter(characterResponse)
        recyclerView.adapter = adapter
    }

    fun endCallProgress(response: Model.CharacterResponse?, m: String) {
        if (response != null) {
            charactersList(response)
            countLimit = response.data.limit
        }
    }

    fun endCallMoreProgress(response: Model.CharacterResponse?) {
        if (response != null) {
            (recyclerView.adapter as CharactersAdapter).characterResponse = response
            (recyclerView.adapter as CharactersAdapter).notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
            countLimit += defaultLimit

        }
    }

    fun startCallProgress() {
        Log.e(MainActivity::class.java.simpleName, "progress started")
    }

    override fun onResume() {
        super.onResume()
        val timestamp = Date().time;
        val hash = Utils.md5(timestamp.toString()+BuildConfig.MARVEL_PRIVATE_KEY+BuildConfig.MARVEL_PUBLIC_KEY)

        manageSub(
                service.getCharacters(timestamp.toString(), BuildConfig.MARVEL_PUBLIC_KEY, hash, defaultLimit)
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

    private fun requestMoreNews() {
        val timestamp = Date().time;
        val hash = Utils.md5(timestamp.toString()+BuildConfig.MARVEL_PRIVATE_KEY+BuildConfig.MARVEL_PUBLIC_KEY)

        manageSub(
                service.getCharacters(timestamp.toString(), BuildConfig.MARVEL_PUBLIC_KEY, hash, countLimit + defaultLimit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( { c -> endCallMoreProgress(c)},
                                { e -> endCallMoreProgress(null)
                                    Log.e(MainActivity::class.java.simpleName, e.message)})
        )

    }

    class InfiniteScrollListener(val func:() -> Unit, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

        private var previousTotal = 0
        private var loading = true
        private var visibleThreshold = 2
        private var firstVisibleItem = 0
        private var visibleItemCount = 0
        private var totalItemCount = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy > 0) {
                visibleItemCount = recyclerView.childCount;
                totalItemCount = layoutManager.itemCount;
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    func()
                    loading = true;
                }
            }
        }

    }

}

























