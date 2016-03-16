package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dougritter.marvelmovies.databinding.ItemCollectionBinding

class CollectionItemFragment : Fragment() {

    lateinit var itemModel: Model.CollectionItem

    companion object {
        fun newInstance(model: Model.CollectionItem): CollectionItemFragment {
            return CollectionItemFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<ItemCollectionBinding>(inflater, R.layout.item_collection, container, false)
        binding.viewmodel = ViewModel.CollectionItemViewModel(context, itemModel)
        return binding.root
    }

}