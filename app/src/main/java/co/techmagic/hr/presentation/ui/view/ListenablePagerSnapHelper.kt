package co.techmagic.hr.presentation.ui.view

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
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

    private fun dispatchOnPageOffsetChanged(page: Int, offset: Float) {
        for (listener in onPageChangeListeners) {
            listener.onPageOffsetChanged(page, offset)
        }
    }

    private inner class ScrollListener : RecyclerView.OnScrollListener() {

        var startPosition: Int? = null
        var scrollValue: Int = 0
        var totalSize: Int = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (recyclerView == null) {
                return
            }

            when (newState) {
                SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING -> {
                    if (startPosition == null) {
                        startPosition = snapPosition ?: return
                        scrollValue = 0
                        totalSize = if (isHorizontal) recyclerView.width else recyclerView.height
                        startPosition?.let {
                            dispatchOnPageOffsetChanged(it, 0f)
                        }
                    }
                }

                SCROLL_STATE_IDLE -> {
                    startPosition = null
                    dispatchOnPageChanged(snapPosition ?: return)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            recyclerView ?: return
            scrollValue += if (isHorizontal) dx else dy
            dispatchOnPageOffsetChanged(startPosition ?: return, scrollValue.toFloat() / totalSize)
        }

        private val isHorizontal: Boolean
            get() = recyclerView?.layoutManager?.canScrollHorizontally() ?: true

        private var snapPosition: Int? = null
            get() {
                val layoutManager = recyclerView?.layoutManager ?: return null
                val snapView = findSnapView(layoutManager)
                return recyclerView?.findContainingViewHolder(snapView)?.adapterPosition
            }
    }

    interface OnPageChangeListener {
        fun onPageSelected(page: Int)
        fun onPageOffsetChanged(page: Int, offset: Float)
    }
}
