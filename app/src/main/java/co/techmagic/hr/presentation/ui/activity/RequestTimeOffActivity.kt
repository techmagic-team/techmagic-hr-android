package co.techmagic.hr.presentation.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl
import co.techmagic.hr.presentation.pojo.AvailableTimeOffsData
import co.techmagic.hr.presentation.pojo.PeriodPair
import co.techmagic.hr.presentation.ui.fragment.RequestTimeOffDatePickerFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffActivity : BaseActivity<RequestTimeOffViewImpl, RequestTimeOffPresenter>() {

    private var actionBar: ActionBar? = null
    private lateinit var vgTimeOffType: ViewGroup
    private lateinit var vgFilterFrom: ViewGroup
    private lateinit var vgFilterTo: ViewGroup
    private lateinit var tvTimeOffTypeSelected: TextView
    private lateinit var tvSelectedFrom: TextView
    private lateinit var tvSelectedTo: TextView
    private lateinit var tvAvailableDays: TextView
    private lateinit var btnRequest: Button
    private lateinit var rgPeriods: RadioGroup
    private lateinit var rbFirstPeriod: RadioButton
    private lateinit var rbSecondPeriod: RadioButton

    private val dateFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setTitle(R.string.tm_hr_request_time_off_title)
    }

    override fun onStart() {
        super.onStart()

        presenter.loadData()

        tvTimeOffTypeSelected.setText(R.string.tm_hr_vacation_time_off_name)
        initTimeOffDate()
    }

    override fun initLayout() {
        setContentView(R.layout.activity_request_timeoff)

        vgTimeOffType = find(R.id.vgTimeOffType)
        vgFilterFrom = find(R.id.vgFilterFrom)
        vgFilterTo = find(R.id.vgFilterTo)
        tvTimeOffTypeSelected = find(R.id.tvTimeOffTypeSelected)
        tvSelectedFrom = find(R.id.tvSelectedFrom)
        tvSelectedTo = find(R.id.tvSelectedTo)
        tvAvailableDays = find(R.id.tvAvailableDays)
        btnRequest = find(R.id.btnRequest)
        rgPeriods = find(R.id.rgPeriods)
        rbFirstPeriod = find(R.id.rbFirstPeriod)
        rbSecondPeriod = find(R.id.rbSecondPeriod)

        rgPeriods.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbFirstPeriod -> run { presenter.onFirstPeriodSelected() }
                R.id.rbSecondPeriod -> run { presenter.onSecondPeriodSelected() }
            }
        }
        vgTimeOffType.setOnClickListener { presenter.onTimeOffTypeClicked() }
        vgFilterFrom.setOnClickListener { presenter.onFromDateClicked() }
        vgFilterTo.setOnClickListener { presenter.onToDateClicked() }
        btnRequest.setOnClickListener { presenter.onRequestButtonClicked() }
    }

    override fun initView(): RequestTimeOffViewImpl {
        return object : RequestTimeOffViewImpl(this, findViewById(android.R.id.content)) {
            override fun showInvalidInputData() {
                toast(R.string.tm_hr_invalid_input_data)
            }

            override fun showRequestTimeOffError() {
                toast(R.string.tm_hr_failed_request_time_off)
            }

            override fun showRequestTimeOffSuccess() {
                toast(R.string.tm_hr_successfully_requested_time_off)
            }

            override fun showUserPeriods(userPeriods: List<PeriodPair>) {
                with(userPeriods) {
                    rbFirstPeriod.text = dateFormat.format(elementAt(0).startDate) + " - " + dateFormat.format(elementAt(0).endDate)
                    rbSecondPeriod.text = dateFormat.format(elementAt(1).startDate) + " - " + dateFormat.format(elementAt(1).endDate)

                    presenter.onFirstPeriodSelected()
                }
            }

            override fun showTimeOffsData(daysAmount: Int?) {
                if (daysAmount == null) {
                    tvAvailableDays.visibility = View.GONE
                } else {
                    tvAvailableDays.visibility = View.VISIBLE
                    tvAvailableDays.text = "Available days $daysAmount"
                }
            }

            override fun showTimeOffsDataError() {
                toast(R.string.tm_hr_request_time_off_error_retrieving_time_off_data)
            }

            override fun selectTimeOff(timeOffType: TimeOffType) {
                when (timeOffType) {
                    TimeOffType.VACATION -> run { tvTimeOffTypeSelected.setText(R.string.tm_hr_vacation_time_off_name) }
                    TimeOffType.DAYOFF -> run { tvTimeOffTypeSelected.setText(R.string.tm_hr_dayoff_time_off_name) }
                    TimeOffType.ILLNESS -> run { tvTimeOffTypeSelected.setText(R.string.tm_hr_illness_time_off_name) }
                    else -> run { toast("wrong type") }
                }

                when (rgPeriods.checkedRadioButtonId) {
                    R.id.rbFirstPeriod -> run { presenter.onFirstPeriodSelected() }
                    R.id.rbSecondPeriod -> run { presenter.onSecondPeriodSelected() }
                }
            }

            override fun showDatePicker(from: Calendar, to: Calendar, isDateFromPicker: Boolean) {
                val fragment: RequestTimeOffDatePickerFragment = RequestTimeOffDatePickerFragment()

                fragment.listener = object : RequestTimeOffDatePickerFragment.DateSetListener {
                    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                        if (isDateFromPicker) {
                            presenter.onFromDateSet(year, month, dayOfMonth)
                        } else {
                            presenter.onToDateSet(year, month, dayOfMonth)
                        }

                        initTimeOffDate()
                    }
                }

                val calendarFrom: Calendar = Calendar.getInstance()
                val calendarTo: Calendar = Calendar.getInstance()

                calendarFrom.timeInMillis = presenter.selectedPeriod!!.startDate.time
                calendarTo.timeInMillis = presenter.selectedPeriod!!.endDate.time

                val bundle: Bundle = Bundle()
                bundle.putSerializable(RequestTimeOffDatePickerFragment.DATE, from)
                bundle.putSerializable(RequestTimeOffDatePickerFragment.START_DATE, calendarFrom)
                bundle.putSerializable(RequestTimeOffDatePickerFragment.END_DATE, calendarTo)

                fragment.arguments = bundle
                fragment.show(fragmentManager, CalendarFiltersActivity.DIALOG_FRAGMENT_TAG)
            }

            override fun showTimeOffsDialog() {
                showSelectTimeOffTypeDialog()
            }
        }
    }

    override fun initPresenter(): RequestTimeOffPresenter {
        return RequestTimeOffPresenter()
    }

    fun initTimeOffDate() {
        setDate(tvSelectedFrom, presenter.requestTimeOffDateFrom.time)
        setDate(tvSelectedTo, presenter.requestTimeOffDateTo.time)
    }

    private fun setDate(dateView: TextView, date: Date) {
        val dateString: String = dateFormat.format(date)
        dateView.text = dateString
    }

    private fun setTimeOffType(timeOffTypeString: String) {
        val timeOffType: TimeOffType = TimeOffType.valueOf(timeOffTypeString)
        presenter.onTimeOffTypeSelected(timeOffType)
    }

    private fun showSelectTimeOffTypeDialog() {
        val itemsList: List<String> = TimeOffType.values().filter { it != TimeOffType.REQUESTED }.map { timeOffType -> timeOffType.name }
        val items: Array<String> = itemsList.toTypedArray()

        val selectedItem: Int = getSelectedTimeOffItemIndex()

        AlertDialog.Builder(this)
                .setTitle(R.string.tm_hr_select_time_off_type)
                .setSingleChoiceItems(items, selectedItem) { dialog, which ->
                    setTimeOffType(items[which])
                    dialog.dismiss()
                }.show()
    }

    private fun getSelectedTimeOffItemIndex(): Int {
        val selectedTimeOffString: String = tvTimeOffTypeSelected.text.toString()

        if (selectedTimeOffString.isEmpty()) {
            return 0
        }

        try {
            val timeOffType: TimeOffType = TimeOffType.valueOf(selectedTimeOffString.toUpperCase())
            val values: List<TimeOffType> = TimeOffType.values().filter { it != TimeOffType.REQUESTED }

            values.indices
                    .filter { values[it] == timeOffType }
                    .forEach { return it }

            return 0

        } catch (e: Exception) {
            return 0
        }
    }
}