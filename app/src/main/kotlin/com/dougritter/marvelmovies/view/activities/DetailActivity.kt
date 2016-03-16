package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dougritter.marvelmovies.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), ViewModel.CharacterDetailViewModel.DetailViewModel {

    companion object { val MODEL_EXTRA = "model" }

    lateinit var detailViewModel: ViewModel.CharacterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel = ViewModel.CharacterDetailViewModel(this, intent)
        detailViewModel.loadCharacter(this)

        binding.viewmodel = detailViewModel

    }

    override fun endCallProgress(any: Any?) {
        if (any is Throwable) {
            println("Error: " + any.message)
        } else if (any is Model.CharacterResponse) {
            println("response "+ any.data.results[0].name)
            testAddSubView(any.data.results[0].comics)
        }
    }

    fun testAddSubView(list: Model.CollectionItem) {
        dynamicItems.addView(CollectionView(this, list))
    }

    override fun onDestroy() {
        detailViewModel.unsubscribe()
        super.onDestroy()
    }

}
