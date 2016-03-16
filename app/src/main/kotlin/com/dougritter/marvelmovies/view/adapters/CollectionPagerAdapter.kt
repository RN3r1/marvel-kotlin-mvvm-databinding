package com.dougritter.marvelmovies

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class CollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    lateinit var items: MutableList<Model.CollectionItem>

    override fun getItem(position: Int): Fragment {
        return CollectionItemFragment.newInstance(items[position])
    }

    override fun getCount(): Int {
        return items.size
    }
}