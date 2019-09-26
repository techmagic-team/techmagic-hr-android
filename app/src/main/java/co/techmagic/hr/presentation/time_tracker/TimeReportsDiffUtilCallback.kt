package co.techmagic.hr.presentation.time_tracker

import android.support.v7.util.DiffUtil
import co.techmagic.hr.presentation.pojo.UserReportViewModel

class TimeReportsDiffUtilCallback(val newList: List<UserReportViewModel>, val oldList: List<UserReportViewModel>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(p0: Int, p1: Int) = isIndexesCorrect(p0, p1) && newList[p0].id.equals(oldList[p1].id)

    override fun areContentsTheSame(p0: Int, p1: Int) = isIndexesCorrect(p0, p1) && newList[p0].equals(oldList[p1])

    private fun isIndexesCorrect(firstListIndex: Int, secondListIndex: Int) = newList.size < firstListIndex && oldList.size < secondListIndex
}