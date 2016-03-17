package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dougritter.marvelmovies.databinding.ItemCollectionBinding

class CollectionItemFragment : Fragment() {

    companion object {
        fun newInstance(model: Model.Item): CollectionItemFragment {
            var fragment = CollectionItemFragment.newInstance()
            fragment.arguments = ViewModel.MarvelItemViewModel.putBundleArgs(model)

            return fragment
        }

        fun newInstance(): CollectionItemFragment {
            return CollectionItemFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<ItemCollectionBinding>(inflater, R.layout.item_collection, container, false)

        if(arguments != null && arguments.containsKey(DetailActivity.MODEL_EXTRA)) {
            binding.viewmodel = ViewModel.MarvelItemViewModel(context, ViewModel.MarvelItemViewModel.getModelFromBundle(arguments))

        }

        return binding.root
    }

}