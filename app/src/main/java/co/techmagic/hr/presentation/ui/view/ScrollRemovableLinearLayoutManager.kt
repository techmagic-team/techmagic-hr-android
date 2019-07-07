package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class ScrollRemovableLinearLayoutManager constructor(context: Context?, @RecyclerView.Orientation orientation : Int, reverseLayout : Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {
    private var isScrollEnabled = true

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled && super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }

    fun setIsScrollEnabled(isEnabled: Boolean) {
        this.isScrollEnabled = isEnabled
    }
}