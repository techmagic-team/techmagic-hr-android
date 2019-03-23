package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R


class ReportPropertyParentHeaderDecorator : RecyclerView.ItemDecoration() {
    private val headers = SparseArray<TextView>()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val currentPosition = parent.getChildLayoutPosition(view)
        if (currentPosition == RecyclerView.NO_POSITION) {
            return
        }

        val currentProperty = (parent.adapter as? ChooseReportPropertyAdapter<*>)?.getItem(currentPosition)
        val previousProperty = (parent.adapter as? ChooseReportPropertyAdapter<*>)?.getItem(currentPosition - 1)

        if (previousProperty == null || previousProperty.getParent() != currentProperty?.getParent() ?: false) {

            if (headers.get(currentPosition) == null) {
                val view = createHeader(parent)

                measureHeader(view, parent)

                headers.put(currentPosition, view)
            }

            headers.get(currentPosition).text = currentProperty?.getParent()

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

    private fun createHeader(parent: ViewGroup) = LayoutInflater.from(parent.context)?.inflate(
            R.layout.item_report_property_header,
            parent,
            false
    ) as TextView

    private fun measureHeader(view : View, parent: RecyclerView) {
        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}