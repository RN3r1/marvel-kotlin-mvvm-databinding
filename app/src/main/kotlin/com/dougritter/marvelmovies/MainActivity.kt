package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.dougritter.marvelmovies.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewModel.MainViewModel.MainActivityViewModel {

    lateinit var mainViewModel: ViewModel.MainViewModel
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModel.MainViewModel(this)
        binding.viewmodel = mainViewModel
        setSupportActionBar(binding.toolbar)

        mainViewModel.loadCharactersList(this)

        val linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout
        recyclerView.addOnScrollListener(Utils.InfiniteScrollListener({ mainViewModel.loadMoreCharacters(this, recyclerView.adapter as CharactersAdapter) }, linearLayout))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_activity_main, menu);

        val item = menu?.findItem(R.id.search)
        searchView = MenuItemCompat.getActionView(item) as SearchView

        return super.onCreateOptionsMenu(menu)

    }

    fun charactersList(characterResponse: Model.CharacterResponse) {
        val adapter = CharactersAdapter(characterResponse)
        recyclerView.adapter = adapter
        searchView.setOnQueryTextListener(mainViewModel.getOnQueryTextChange(recyclerView.adapter as CharactersAdapter));

    }

    override fun endCallProgress(any: Any?) {
        if (any is Throwable) {
            println("Error: " + any.message)
        } else if (any is Model.CharacterResponse) {
            charactersList(any)
            println("response "+ any.data.results[0].name)
        }
    }

    override fun onDestroy() {
        mainViewModel.unsubscribe()
        super.onDestroy()
    }


}
