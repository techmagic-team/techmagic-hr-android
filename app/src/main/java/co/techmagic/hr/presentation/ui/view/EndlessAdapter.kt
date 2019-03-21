package co.techmagic.hr.presentation.ui.view

import android.support.v7.widget.RecyclerView

abstract class EndlessAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    val centerIndex = itemCount / 2

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.scrollToPosition(centerIndex)
    }

    final override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }
}
