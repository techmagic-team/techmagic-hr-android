package co.techmagic.hr.presentation.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.techmagic.hr.R
import co.techmagic.hr.common.Role
import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl
import co.techmagic.hr.presentation.pojo.WorkingPeriod
import co.techmagic.hr.presentation.util.DateUtil
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffActivity : BaseActivity<RequestTimeOffViewImpl, RequestTimeOffPresenter>() {

    private val DATE_PICKER_FRAGMENT: String = "DatePickerFragment"
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
    private lateinit var rvRequestedTimeOffs: RecyclerView
    private lateinit var availableDaysLabel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Locale.setDefault(Locale.US)

        actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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
        rvRequestedTimeOffs = find(R.id.rvRequestedTimeOffs)
        availableDaysLabel = resources.getString(R.string.tm_hr_available_days_label)

        rvRequestedTimeOffs.layoutManager = LinearLayoutManager(this)

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
            override fun enableDatePickers() {
                vgFilterFrom.setOnClickListener { presenter.onFromDateClicked() }
                vgFilterTo.setOnClickListener { presenter.onToDateClicked() }
            }

            override fun disableDatePickers() {
                vgFilterFrom.setOnClickListener(null)
                vgFilterTo.setOnClickListener(null)
            }

            override fun disableRequestButton() {
                btnRequest.isEnabled = false
            }

            override fun enableRequestButton() {
                btnRequest.isEnabled = true
            }

            override fun showNotEnoughDaysAvailable() {
                toast(R.string.tm_hr_not_enough_days_available)
            }

            override fun showCantRequestDayOffBecauseOfVacations(rolE_USER: Role) {
                toast(R.string.tm_hr_you_have_not_already_used_all_vacations)
            }

            override fun showUserProfileError() {
                toast(R.string.tm_hr_error_loading_user_profile)
            }

            override fun showErrorDeletingRequestedTimeOff() {
                toast(R.string.tm_hr_failed_delete_time_off)
            }

            override fun showRequestedTimeOffs(timeOffs: MutableList<RequestedTimeOffDto>) {
                this@RequestTimeOffActivity.showRequestedTimeOffs(timeOffs)
            }

            override fun showErrorLoadingRequestedTimeOffs() {
                toast(R.string.tm_hr_failed_show_requested_time_off)
            }

            override fun showInvalidInputData() {
                toast(R.string.tm_hr_invalid_input_data)
            }

            override fun showRequestTimeOffError() {
                toast(R.string.tm_hr_failed_request_time_off)
            }

            override fun showRequestTimeOffSuccess() {
                toast(R.string.tm_hr_successfully_requested_time_off)
            }

            override fun showUserPeriods(userPeriods: Pair<WorkingPeriod, WorkingPeriod>) {
                rgPeriods.visibility = View.VISIBLE
                with(userPeriods) {
                    rbFirstPeriod.text = DateUtil.getFormattedFullDate(first.startDate) + " - " + DateUtil.getFormattedFullDate(first.endDate)
                    rbSecondPeriod.text = DateUtil.getFormattedFullDate(second.startDate) + " - " + DateUtil.getFormattedFullDate(second.endDate)

                    presenter.onFirstPeriodSelected()
                }
            }

            override fun showTimeOffsData(daysAmount: Int?) {
                if (daysAmount == null) {
                    tvAvailableDays.visibility = View.GONE
                } else {
                    tvAvailableDays.visibility = View.VISIBLE
                    tvAvailableDays.text = String.format(availableDaysLabel, daysAmount)
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
            }

            override fun showDatePicker(from: Calendar, to: Calendar, isDateFromPicker: Boolean, allowPastDateSelection: Boolean) {
                var initDate: Calendar = Calendar.getInstance()

                if (initDate.before(from)) {
                    initDate = from
                }

                val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (isDateFromPicker) {
                        presenter.onFromDateSet(year, monthOfYear, dayOfMonth)
                    }

                    presenter.onToDateSet(year, monthOfYear, dayOfMonth)
                    initTimeOffDate()
                }

                val datePickerDialog = DatePickerDialog.newInstance(
                        dateSetListener,
                        initDate.get(Calendar.YEAR),
                        initDate.get(Calendar.MONTH),
                        initDate.get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.minDate = presenter.getMinDatePickerDate()
                datePickerDialog.maxDate = presenter.getMaxDatePickerDate()
                datePickerDialog.disabledDays = presenter.getHolidays()!!.toTypedArray()
                datePickerDialog.show(fragmentManager, DATE_PICKER_FRAGMENT)
            }

            override fun showTimeOffsDialog() {
                showSelectTimeOffTypeDialog()
            }
        }
    }

    override fun initPresenter(): RequestTimeOffPresenter {
        return RequestTimeOffPresenter()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out)
    }

    fun initTimeOffDate() {
        setDate(tvSelectedFrom, presenter.requestTimeOffDateFrom.time)
        setDate(tvSelectedTo, presenter.requestTimeOffDateTo.time)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setDate(dateView: TextView, date: Date) {
        val dateString: String = DateUtil.getFormattedFullDate(date)
        dateView.text = dateString
    }

    private fun setTimeOffType(timeOffTypeString: String) {
        val timeOffType: TimeOffType? = TimeOffType.getType(resources, timeOffTypeString)
        if (timeOffType != null) {
            presenter.onTimeOffTypeSelected(timeOffType)
        }
    }

    private fun showSelectTimeOffTypeDialog() {
        val itemsList: List<String> = TimeOffType.values().filter { it != TimeOffType.REQUESTED }.map {
            timeOffType ->
            resources.getString(timeOffType.displayNameId)
        }

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

        val timeOffType: TimeOffType = TimeOffType.getType(resources, selectedTimeOffString) ?: return 0
        val values: List<TimeOffType> = TimeOffType.values().filter { it != TimeOffType.REQUESTED }

        values.indices
                .filter { values[it] == timeOffType }
                .forEach { return it }

        return 0
    }

    private fun showRequestedTimeOffs(timeOffs: MutableList<RequestedTimeOffDto>) {
        if (rvRequestedTimeOffs.adapter != null) {
            val adapter: RequestedTimeOffListAdapter = rvRequestedTimeOffs.adapter as RequestedTimeOffListAdapter
            adapter.resetData(timeOffs)
        } else {
            rvRequestedTimeOffs.adapter = RequestedTimeOffListAdapter(timeOffs)
        }
    }

    inner class RequestedTimeOffListAdapter(timeOffs: MutableList<RequestedTimeOffDto>) : RecyclerView.Adapter<RequestedTimeOffListAdapter.ViewHolder>() {

        val items: MutableList<RequestedTimeOffDto> = mutableListOf()

        init {
            items.addAll(timeOffs)
        }

        fun resetData(newItems: List<RequestedTimeOffDto>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val requestedTimeOff: RequestedTimeOffDto = items[position]
            val dateFrom: String = DateUtil.getFormattedFullDate(requestedTimeOff.dateFrom)
            val dateTo: String = DateUtil.getFormattedFullDate(requestedTimeOff.dateTo)

            val dateRangeString = dateFrom + " - " + dateTo
            holder!!.tvTimeOff.text = dateRangeString

            if (presenter.canBeDeleted(requestedTimeOff)) {
                holder.btDelete.visibility = View.VISIBLE
                holder.btDelete.setOnClickListener {
                    val title = resources.getString(R.string.tm_hr_request_time_off_title)
                    val alertMessagePattern = resources.getString(R.string.tm_hr_time_off_delete_confirm_alert_message)
                    val timeOffName: String = resources.getString(presenter.selectedTimeOffType?.displayNameId!!)
                    val message = String.format(alertMessagePattern, timeOffName, dateRangeString)

                    alert(message, title) {
                        positiveButton(R.string.message_text_yes, { presenter.removeRequestedTimeOff(requestedTimeOff) })
                        negativeButton(R.string.message_text_no, { dismiss() })
                    }.show()
                }

            } else {
                holder.btDelete.visibility = View.INVISIBLE
            }

            when (requestedTimeOff.isAccepted) {
                null -> holder.ivApproved.setImageResource(R.drawable.ic_access_time_grey_24dp)
                false -> holder.ivApproved.setImageResource(R.drawable.ic_cancel_red_24dp)
                true -> holder.ivApproved.setImageResource(R.drawable.ic_check_circle_green_24dp)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_requested_time_off, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = items.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTimeOff: TextView = view.find(R.id.tvPeriod)
            val btDelete: Button = view.find(R.id.btDelete)
            val ivApproved: ImageView = view.find(R.id.ivApproved)
        }
    }
}