package co.techmagic.hr.presentation.time_tracker.time_report_detail.base

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.base.HrAppBaseViewFragment
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import co.techmagic.hr.presentation.util.SimpleTextWatcher
import co.techmagic.hr.presentation.util.TimeInputTextWatcher
import co.techmagic.hr.presentation.util.changeShapeStrokeColor
import org.jetbrains.anko.find


open class BaseTimeReportDetailFragment<T : BaseTimeReportDetailPresenter> : HrAppBaseViewFragment<T>(),
        BaseTimeReportDetailView {

    private lateinit var flTimeReportDetailContainer: FrameLayout
    private lateinit var tvSelectedProject: TextView
    private lateinit var tvTimeReportDetailProjectError: TextView
    private lateinit var tvSelectedProjectTask: TextView
    private lateinit var tvTimeReportDetailTaskError: TextView
    private lateinit var edDescription: EditText
    private lateinit var tvDescriptionError: TextView
    private lateinit var btnFifteenMinutes: TextView
    private lateinit var btnThirtyMinutes: TextView
    private lateinit var btnOneHour: TextView
    private lateinit var btnEightHours: TextView
    private lateinit var edTime: EditText
    private lateinit var btnIncreaseTime: ImageView
    private lateinit var btnReduceTime: ImageView
    private lateinit var btnStartTimer: TextView
    private lateinit var btnSave: TextView

    private lateinit var toolbarChangeListener: ActionBarChangeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_report_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        presenter?.onVisibleToUser()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        toolbarChangeListener = context as ActionBarChangeListener
    }

    override fun initView() {
        view ?: return

        flTimeReportDetailContainer = view!!.find(R.id.flTimeReportDetailContainer)
        tvSelectedProject = view!!.find(R.id.tvTimeReportDetailSelectedProject)
        tvTimeReportDetailProjectError = view!!.find(R.id.tvTimeReportDetailProjectError)
        tvSelectedProjectTask = view!!.find(R.id.tvTimeReportDetailSelectedTask)
        tvTimeReportDetailTaskError = view!!.findViewById(R.id.tvTimeReportDetailTaskError)
        edDescription = view!!.find(R.id.edTimeReportDetailDescription)
        tvDescriptionError = view!!.find(R.id.tvTimeReportDetailDescriptionError)
        btnFifteenMinutes = view!!.find(R.id.btnTimeReportDetailDefaultTimeFifteenMinutes)
        btnThirtyMinutes = view!!.find(R.id.btnTimeReportDetailDefaultTimeThirtyMinutes)
        btnOneHour = view!!.find(R.id.btnTimeReportDetailDefaultTimeOneHour)
        btnEightHours = view!!.find(R.id.btnTimeReportDetailDefaultTimeEightHours)
        edTime = view!!.find(R.id.edTimeReportDetailTime)
        btnIncreaseTime = view!!.find(R.id.btnTimeReportDetailIncreaseTime)
        btnReduceTime = view!!.find(R.id.btnTimeReportDetailReduceTime)
        btnStartTimer = view!!.find(R.id.btnTimeReportDetailStartTimer)
        btnSave = view!!.find(R.id.btnTimeReportDetailSave)
    }

    private fun initListeners() {
        tvSelectedProject.setOnClickListener { presenter?.changeProjectClicked() }
        tvSelectedProjectTask.setOnClickListener { presenter?.changeTaskClicked() }
        edDescription.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter?.descriptionChanged(s.toString())
            }
        })
        btnFifteenMinutes.setOnClickListener { presenter?.addFifteenMinutesClicked() }
        btnThirtyMinutes.setOnClickListener { presenter?.addThirtyMinutesClicked() }
        btnOneHour.setOnClickListener { presenter?.addOneHourClicked() }
        btnEightHours.setOnClickListener { presenter?.addEightHoursClicked() }
        btnIncreaseTime.setOnClickListener { presenter?.increaseTimeClicked() }
        btnReduceTime.setOnClickListener { presenter?.reduceTimeClicked() }
        btnStartTimer.setOnClickListener { presenter?.startTimerClicked() }
        btnSave.setOnClickListener { presenter?.saveClicked() }

        edTime.addTextChangedListener(TimeInputTextWatcher(edTime))
    }

    override fun setDeleteReportButtonVisible(visible: Boolean) {
        setHasOptionsMenu(true)
    }

    override fun showDate(date: String) {
        toolbarChangeListener.setActionBarTitle(date)
    }

    override fun showProject(project: String) {
        tvSelectedProject.setText(project)
    }

    override fun showTask(task: String) {
        tvSelectedProjectTask.setText(task)
    }

    override fun showDescription(description: String) {
        edDescription.setText(description)
    }

    override fun setDescriptionValid(isValid: Boolean) {
        tvDescriptionError.visibility = if (isValid) View.INVISIBLE else View.VISIBLE
        setBackgroundByValid(edDescription, isValid)
    }

    override fun setProjectValid(isValid: Boolean) {
        tvTimeReportDetailProjectError.visibility = if (isValid) View.INVISIBLE else View.VISIBLE
        setBackgroundByValid(tvSelectedProject, isValid)
    }

    override fun setTaskValid(isValid: Boolean) {
        tvTimeReportDetailTaskError.visibility = if (isValid) View.INVISIBLE else View.VISIBLE
        setBackgroundByValid(tvSelectedProjectTask, isValid)
    }

    override fun showTime(formattedTime: String) {
        edTime.setText(formattedTime)
    }

    private fun setBackgroundByValid(view: View, isValid: Boolean) {
        view.changeShapeStrokeColor(
                R.dimen.activity_time_report_detail_small_border_width,
                if (isValid) R.color.color_time_report_detail_input_border_color else R.color.color_time_report_detail_picker_description_error_color
        )
    }
}