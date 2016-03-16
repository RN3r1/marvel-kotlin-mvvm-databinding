package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dougritter.marvelmovies.databinding.ItemCollectionBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class CollectionItemFragment : Fragment() {

    lateinit var itemModel: Model.Item

    companion object {
        fun newInstance(model: Model.Item): CollectionItemFragment {
            var args = Bundle()
            args.putString(DetailActivity.MODEL_EXTRA, Gson().toJson(model))
            var fragment = CollectionItemFragment.newInstance()
            fragment.arguments = args

            return fragment
        }

        fun newInstance(): CollectionItemFragment {
            return CollectionItemFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<ItemCollectionBinding>(inflater, R.layout.item_collection, container, false)

        if(arguments != null && arguments.containsKey(DetailActivity.MODEL_EXTRA)) {
            val itemType = object : TypeToken<Model.Item>() {}.type
            itemModel = Gson().fromJson<Model.Item>(arguments.getString(DetailActivity.MODEL_EXTRA), itemType)
            binding.viewmodel = ViewModel.MarvelItemViewModel(context, itemModel)

        }

        return binding.root
    }

}