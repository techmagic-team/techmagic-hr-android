package co.techmagic.hr.presentation.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import java.util.*


/**
 * Created by Roman Ursu on 6/7/17
 */
class RequestTimeOffDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        val DATE = "DATE"
    }

    interface DateSetListener {
        fun onDateSet(year: Int, month: Int, dayOfMonth: Int)
    }

    var listener: DateSetListener? = null

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onDateSet(year, month, dayOfMonth)
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = arguments.get(DATE) as Calendar
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, yy, mm, dd)
    }
}