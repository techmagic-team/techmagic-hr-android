package co.techmagic.hr.presentation.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import butterknife.OnClick
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl
import co.techmagic.hr.presentation.ui.fragment.DatePickerFragment
import co.techmagic.hr.presentation.ui.fragment.RequestTimeOffDatePickerFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffActivity : BaseActivity<RequestTimeOffViewImpl, RequestTimeOffPresenter>(), DatePickerFragment.OnDatePickerFragmentListener {

    private var actionBar: ActionBar? = null
    private lateinit var vgTimeOffType: ViewGroup
    private lateinit var vgFilterFrom: ViewGroup
    private lateinit var vgFilterTo: ViewGroup
    private lateinit var tvTimeOffTypeSelected: TextView
    private lateinit var tvSelectedFrom: TextView
    private lateinit var tvSelectedTo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.title = "Request Time off"
    }

    override fun onStart() {
        super.onStart()

        presenter.loadData()
    }

    override fun initLayout() {
        setContentView(R.layout.activity_request_timeoff)
        vgTimeOffType = find(R.id.vgTimeOffType)
        vgFilterFrom = find(R.id.vgFilterFrom)
        vgFilterTo = find(R.id.vgFilterTo)
        tvTimeOffTypeSelected = find(R.id.tvTimeOffTypeSelected)
        tvSelectedFrom = find(R.id.tvSelectedFrom)
        tvSelectedTo = find(R.id.tvSelectedTo)

        vgTimeOffType.setOnClickListener { toast("clicked") }
        vgFilterFrom.setOnClickListener { presenter.onFromDateClicked() }
        vgFilterTo.setOnClickListener { presenter.onToDateClicked() }
    }

    override fun initView(): RequestTimeOffViewImpl {
        return object : RequestTimeOffViewImpl(this, findViewById(android.R.id.content)) {
            override fun showDatePicker(from: Calendar, to: Calendar, isDateFromPicker: Boolean) {
                val fragment : RequestTimeOffDatePickerFragment = RequestTimeOffDatePickerFragment()

                fragment.listener = object : RequestTimeOffDatePickerFragment.DateSetListener {
                    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                        initFromDate(year, month, dayOfMonth)
                    }
                }

                val bundle : Bundle = Bundle()
                bundle.putSerializable(RequestTimeOffDatePickerFragment.DATE, from)

                fragment.arguments = bundle
                fragment.show(fragmentManager, CalendarFiltersActivity.DIALOG_FRAGMENT_TAG)
            }

            override fun showTimeOffsDialog() {
                toast("showTimeOffsDialog")
            }
        }
    }

    override fun initPresenter(): RequestTimeOffPresenter {
        return RequestTimeOffPresenter()
    }

    override fun addDatePickerFragment(from: Calendar?, to: Calendar?, isDateFromPicker: Boolean) {
        if (isDateFromPicker) {

        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displaySelectedFromDate(date: String, from: Calendar?, to: Calendar?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displaySelectedToDate(date: String, from: Calendar?, to: Calendar?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invalidDateRangeSelected(resId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun initFromDate(year: Int, month: Int, dayOfMonth: Int) {

    }


//    @Override
//    public void addDatePickerFragment(@Nullable Calendar from, @Nullable Calendar to, boolean isDateFromPicker) {
//        DatePickerFragment fragment = DatePickerFragment.newInstance();
//        Bundle b = new Bundle();
//
//        b.putBoolean(SELECTED_DIALOG_KEY, isDateFromPicker);
//        b.putSerializable(CALENDAR_FROM_KEY, from);
//        b.putSerializable(CALENDAR_TO_KEY, to);
//        fragment.setArguments(b);
//        fragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
//    }

//    private <T extends IFilterModel> void showSelectFilterAlertDialog(@Nullable List<T> filters) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        setupDialogViews(filters, builder);
//        dialog = builder.show();
//        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
//        dialog.setCancelable(false);
//        dialog.show();
//    }
//
//
//    private <T extends IFilterModel> void setupDialogViews(@Nullable List<T> filters, AlertDialog.Builder builder) {
//        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_select_filter, null);
//        builder.setView(view);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        setupDialogTitle(tvTitle);
//
//        setupSelectFilterRecyclerView(view, filters);
//    }
}