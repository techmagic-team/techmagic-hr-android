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
import co.techmagic.hr.domain.pojo.DatePeriodDto
import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl
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

    private val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setTitle(R.string.tm_hr_request_time_off_title)

        if (presenter.timeOffType == null) {
            presenter.onTimeOffTypeSelected(TimeOffType.VACATION)
        }
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
                R.id.rbFirstPeriod -> run { toast("First radio") }
                R.id.rbSecondPeriod -> run { toast("Second radio") }
            }
        }
        vgTimeOffType.setOnClickListener { presenter.onTimeOffTypeClicked() }
        vgFilterFrom.setOnClickListener { presenter.onFromDateClicked() }
        vgFilterTo.setOnClickListener { presenter.onToDateClicked() }
        btnRequest.setOnClickListener { presenter.onRequestButtonClicked() }
    }

    override fun initView(): RequestTimeOffViewImpl {
        return object : RequestTimeOffViewImpl(this, findViewById(android.R.id.content)) {
            override fun showPeriods(datePeriodDto: List<DatePeriodDto>) {
                rbFirstPeriod.text = dateFormat.format(datePeriodDto[0].dateFrom) + " - " + dateFormat.format(datePeriodDto[0].dateTo)
                rbSecondPeriod.text = dateFormat.format(datePeriodDto[1].dateFrom) + " - " + dateFormat.format(datePeriodDto[1].dateTo)
            }

            override fun showTimeOffsData() {
                showRemainedTimeOffs()
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

                showRemainedTimeOffs()
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

                val bundle: Bundle = Bundle()
                bundle.putSerializable(RequestTimeOffDatePickerFragment.DATE, from)

                fragment.arguments = bundle
                fragment.show(fragmentManager, CalendarFiltersActivity.DIALOG_FRAGMENT_TAG)
            }

            override fun showTimeOffsDialog() {
                showSelectTimeOffTypeDialog()
            }
        }
    }

    private fun showRemainedTimeOffs() {
        val allTimeOffsDto: RemainedTimeOffsAmountDto? = presenter.remainedDays

        if (allTimeOffsDto != null) {
            when (presenter.timeOffType) {
                TimeOffType.VACATION -> run {
                    val daysAmount = allTimeOffsDto.map[TimeOffType.VACATION]
                    tvAvailableDays.visibility = View.VISIBLE
                    tvAvailableDays.text = "Available days $daysAmount"

                }
                TimeOffType.DAYOFF -> run {
                    val daysAmount = allTimeOffsDto.map[TimeOffType.DAYOFF]
                    tvAvailableDays.visibility = View.VISIBLE
                    tvAvailableDays.text = "Available days $daysAmount"
                }
                TimeOffType.ILLNESS -> run {
                    val daysAmount = allTimeOffsDto.map[TimeOffType.ILLNESS]
                    tvAvailableDays.visibility = View.VISIBLE
                    tvAvailableDays.text = "Available days $daysAmount"
                }
                else -> run { tvAvailableDays.visibility = View.GONE }
            }

        } else {
            tvAvailableDays.visibility = View.GONE
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