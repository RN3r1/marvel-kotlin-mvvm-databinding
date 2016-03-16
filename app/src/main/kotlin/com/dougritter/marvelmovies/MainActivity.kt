package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import com.dougritter.marvelmovies.databinding.ActivityDetailBinding
import com.dougritter.marvelmovies.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var originalList: MutableList<Model.Character>
    val defaultLimit = 20
    var countLimit = 0
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        service = MarvelService.create()
        val linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout
        recyclerView.addOnScrollListener(InfiniteScrollListener({ requestMoreCharacters()}, linearLayout))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_activity_main, menu);

        val item = menu?.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(term: String?): Boolean {
                if (term != null) {
                    filterList(term)
                }
                return false
            }

            override fun onQueryTextSubmit(term: String?): Boolean {
                if (term != null) {
                    filterList(term)
                }
                return false
            }

        });

        return super.onCreateOptionsMenu(menu)

    }

    fun filterList(term: String) {
        if (term != "") {
            val list = (recyclerView.adapter as CharactersAdapter).characterResponse.data.results.filter { it.name.contains(term.trim(), true) } as MutableList<Model.Character>
            (recyclerView.adapter as CharactersAdapter).characterResponse.data.results = list
            (recyclerView.adapter as CharactersAdapter).notifyDataSetChanged()

        } else {
            (recyclerView.adapter as CharactersAdapter).characterResponse.data.results = originalList
            (recyclerView.adapter as CharactersAdapter).notifyDataSetChanged()
        }

    }



    fun charactersList(characterResponse: Model.CharacterResponse) {
        val adapter = CharactersAdapter(characterResponse)
        recyclerView.adapter = adapter
    }

    fun endCallProgress(response: Model.CharacterResponse?) {
        if (response != null) {
            charactersList(response)
            originalList = response.data.results
            countLimit = response.data.limit
        }
    }

    fun endCallMoreProgress(response: Model.CharacterResponse?) {
        if (response != null) {
            (recyclerView.adapter as CharactersAdapter).characterResponse = response
            (recyclerView.adapter as CharactersAdapter).notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
            originalList = response.data.results
            countLimit += defaultLimit

        }
    }

    override fun onResume() {
        super.onResume()
        val timestamp = Date().time;
        val hash = Utils.md5(timestamp.toString()+BuildConfig.MARVEL_PRIVATE_KEY+BuildConfig.MARVEL_PUBLIC_KEY)

        manageSub(
                service.getCharacters(timestamp.toString(), BuildConfig.MARVEL_PUBLIC_KEY, hash, defaultLimit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( { c -> endCallProgress(c)},
                                { e -> endCallProgress(null)
                                Log.e(MainActivity::class.java.simpleName, e.message)})
        )

    }

    override fun onDestroy() {
        compoSub.unsubscribe()
        super.onDestroy()
    }

    private fun requestMoreCharacters() {
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
