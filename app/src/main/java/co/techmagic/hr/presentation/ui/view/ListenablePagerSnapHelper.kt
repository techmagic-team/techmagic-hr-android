package co.techmagic.hr.presentation.ui.view

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING
import java.util.*

class ListenablePagerSnapHelper : PagerSnapHelper() {

    private val onPageChangeListeners = ArrayList<OnPageChangeListener>()

    private val scrollListener = ScrollListener()

    private var recyclerView: RecyclerView? = null

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)

        this.onPageChangeListeners.clear()
        this.recyclerView?.removeOnScrollListener(scrollListener)

        this.recyclerView = recyclerView

        this.recyclerView?.addOnScrollListener(scrollListener)
    }

    fun addOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.onPageChangeListeners.add(onPageChangeListener)
    }

    fun removeOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.onPageChangeListeners.remove(onPageChangeListener)
    }

    private fun dispatchOnPageChanged(page: Int) {
        for (listener in onPageChangeListeners) {
            listener.onPageSelected(page)
        }
    }

    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (recyclerView != null && (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_SETTLING)) {
                val layoutManager = recyclerView.layoutManager
                val snapView = findSnapView(layoutManager)
                val page = recyclerView.findContainingViewHolder(snapView)?.adapterPosition
                if (page != null) {
                    dispatchOnPageChanged(page)
                }
            }
        }
    }

    interface OnPageChangeListener {
        fun onPageSelected(page: Int)
    }
}
