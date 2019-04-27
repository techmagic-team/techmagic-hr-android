package co.techmagic.hr.presentation.ui.adapter.headers_adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


abstract class BaseHeaderItemDecorator<T : HasHeaderProperty<*>, V : View> : RecyclerView.ItemDecoration() {
    private val headers = SparseArray<V>()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val currentPosition = parent.getChildLayoutPosition(view)
        if (currentPosition == RecyclerView.NO_POSITION) {
            return
        }

        val currentProperty = (parent.adapter as? BaseHeadersAdapter<*, T, *>)?.getItem(currentPosition)//todo do something with warning
        val previousProperty = (parent.adapter as? BaseHeadersAdapter<*, T, *>)?.getItem(currentPosition - 1)

        if (previousProperty == null || previousProperty.getParent() != currentProperty?.getParent() ?: false) {

            if (headers.get(currentPosition) == null) {
                val headerView = createHeader(parent)

                measureHeader(headerView, parent)

                headers.put(currentPosition, headerView)
                currentProperty?.let { fillUpHeader(headerView, currentProperty) }
            }

            outRect.top = headers.get(currentPosition).measuredHeight

        } else {
            headers.remove(currentPosition)
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position != RecyclerView.NO_POSITION && headers.get(position) != null) {
                canvas.save()
                val headerView = headers.get(position)
                canvas.translate(0F, (child.top - headerView.height).toFloat())
                headerView.draw(canvas)
                canvas.restore()
            }
        }
    }

    abstract fun fillUpHeader(view: V, item: T)

    abstract fun createHeader(parent: ViewGroup) : V

    private fun measureHeader(view: View, parent: RecyclerView) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}