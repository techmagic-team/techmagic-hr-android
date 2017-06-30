package co.techmagic.hr.presentation.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import co.techmagic.hr.R
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by Roman Ursu on 6/7/17
 */
class RequestTimeOffDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        val DATE = "DATE"
        val START_DATE = "START_DATE"
        val END_DATE = "END_DATE"
    }

    interface DateSetListener {
        fun onDateSet(year: Int, month: Int, dayOfMonth: Int)
    }

    var listener: DateSetListener? = null

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val startDateCalendar: Calendar = arguments.get(START_DATE) as Calendar
        val endDateCalendar: Calendar = arguments.get(END_DATE) as Calendar
        val selectedDate: Calendar = Calendar.getInstance()

        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        listener?.onDateSet(year, month, dayOfMonth)
        dismiss()
//        if (selectedDate.before(endDateCalendar) && selectedDate.after(startDateCalendar)) {
//            listener?.onDateSet(year, month, dayOfMonth)
//            dismiss()
//        } else {
//            toast(R.string.tm_hr_wrong_date)
//        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Locale.setDefault(Locale.US)

        val calendar: Calendar = arguments.get(DATE) as Calendar
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, yy, mm, dd)
    }
}