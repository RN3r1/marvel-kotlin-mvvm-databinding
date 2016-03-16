package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dougritter.marvelmovies.databinding.ActivityDetailBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object { val MODEL_EXTRA = "model" }

    lateinit var detailViewModel: ViewModel.CharacterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val characterType = object : TypeToken<Model.Character>() {}.type
        val model = Gson().fromJson<Model.Character>(intent.getStringExtra(MODEL_EXTRA), characterType)

        detailViewModel = ViewModel.CharacterDetailViewModel(this, model)
        detailViewModel.loadCharacter(this)

        binding.viewmodel = detailViewModel

    }

    fun endCallProgress(response: Model.CharacterResponse?) {
        println("response "+ response!!.data.results[0].name)
    }

    fun endCallProgress(error: Throwable) {
        Log.e(DetailActivity::class.java.simpleName, error.message)
    }

    override fun onDestroy() {
        detailViewModel.unsubscribe()
        super.onDestroy()
    }

}
