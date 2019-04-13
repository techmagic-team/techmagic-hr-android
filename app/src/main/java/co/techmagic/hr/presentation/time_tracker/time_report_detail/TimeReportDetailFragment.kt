package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import co.techmagic.hr.presentation.util.SimpleTextWatcher
import co.techmagic.hr.presentation.util.changeRippleShapeStrokeColor
import com.techmagic.viper.base.BaseViewFragment
import org.jetbrains.anko.find


class TimeReportDetailFragment : BaseViewFragment<TimeReportDetailPresenter>(),
        TimeReportDetailView {

    private lateinit var tvSelectedProject: TextView
    private lateinit var tvSelectedProjectTask: TextView
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
        findViews(view)
        initClicks()
        edDescription.changeRippleShapeStrokeColor(
                R.dimen.activity_time_report_detail_small_border_width,
                R.color.color_time_report_detail_picker_description_error_color
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_time_report_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_delete -> {
                presenter?.deleteClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        toolbarChangeListener = context as ActionBarChangeListener
    }

    private fun findViews(view: View) {
        tvSelectedProject = view.find(R.id.tvTimeReportDetailSelectedProject)
        tvSelectedProjectTask = view.find(R.id.tvTimeReportDetailSelectedTask)
        edDescription = view.find(R.id.edTimeReportDetailDescription)
        tvDescriptionError = view.find(R.id.tvTimeReportDetailDescriptionError)
        btnFifteenMinutes = view.find(R.id.btnTimeReportDetailDefaultTimeFifteenMinutes)
        btnThirtyMinutes = view.find(R.id.btnTimeReportDetailDefaultTimeThirtyMinutes)
        btnOneHour = view.find(R.id.btnTimeReportDetailDefaultTimeOneHour)
        btnEightHours = view.find(R.id.btnTimeReportDetailDefaultTimeEightHours)
        edTime = view.find(R.id.edTimeReportDetailTime)
        btnIncreaseTime = view.find(R.id.btnTimeReportDetailIncreaseTime)
        btnReduceTime = view.find(R.id.btnTimeReportDetailReduceTime)
        btnStartTimer = view.find(R.id.btnTimeReportDetailStartTimer)
        btnSave = view.find(R.id.btnTimeReportDetailSave)
    }

    private fun initClicks() {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDescriptionValid(isValid: Boolean) {

        tvDescriptionError.visibility = if (isValid) View.VISIBLE else View.INVISIBLE
        edDescription.changeRippleShapeStrokeColor(
                R.dimen.activity_time_report_detail_small_border_width,
                if (isValid) R.color.color_time_report_detail_input_border_color else R.color.color_time_report_detail_picker_description_error_color
        )
        btnFifteenMinutes.isEnabled = isValid
        btnThirtyMinutes.isEnabled = isValid
        btnOneHour.isEnabled = isValid
        btnEightHours.isEnabled = isValid
        btnIncreaseTime.isEnabled = isValid
        btnReduceTime.isEnabled = isValid
    }

    override fun showTime(formatedTime: String) {
        edTime.setText(formatedTime)
    }

    companion object {
        fun newInstance() = TimeReportDetailFragment()
    }
}