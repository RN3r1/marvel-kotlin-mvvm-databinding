package com.dougritter.marvelmovies

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.dougritter.marvelmovies.R

class CollectionView : RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, list: Model.CollectionItem) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        println("CollectionView - init")
        inflate(context, R.layout.view_collection, this)
    }

}