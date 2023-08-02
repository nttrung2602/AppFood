package com.trungdz.appfood.presentation.customScrollRecyclerView

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class RecyclerViewLoadMoreScroll(layoutManager: LayoutManager) :
    RecyclerView.OnScrollListener() {
    private lateinit var layoutManager: LayoutManager
    private var visibleThreshold = 5
    private var isLoading = false
    var lastVisibleItem = 0 // position of item in listAdapter which is displayed last position on screen
    var firstVisibleItem = 0
    private var totalItemCount = 0

    init {
        when (layoutManager) {
            is LinearLayoutManager -> {
                this.layoutManager = layoutManager
            }
            is GridLayoutManager -> {
                this.layoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
            is StaggeredGridLayoutManager -> { //StaggeredLayout
                this.layoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
        }
    }

    fun setLoaded() {
        isLoading = false
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        totalItemCount = layoutManager.itemCount

        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisibleItem = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                firstVisibleItem=(layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItem =
                    (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                firstVisibleItem=(layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            }
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemInArray =
                    (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                lastVisibleItem = getLastVisibleItem(lastVisibleItemInArray)

              //  firstVisibleItem =
            }
        }

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            loadMoreItems()
            isLoading = true
        }

    }

    private fun getLastVisibleItem(lastVisibleItemInArray: IntArray): Int {
        var maxIndexPosition = 0
        for (i in lastVisibleItemInArray.indices) {
            if (i == 0) {
                maxIndexPosition = lastVisibleItemInArray[i]
            } else if (lastVisibleItemInArray[i] > maxIndexPosition) {
                maxIndexPosition = lastVisibleItemInArray[i]
            }
        }

        return maxIndexPosition
    }

    abstract fun loadMoreItems()
}