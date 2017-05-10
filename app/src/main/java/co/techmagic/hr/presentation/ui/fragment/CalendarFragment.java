package co.techmagic.hr.presentation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.mvp.presenter.CalendarPresenter;
import co.techmagic.hr.presentation.mvp.view.impl.CalendarViewImpl;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener;
import co.techmagic.hr.presentation.ui.view.calendar.TimeTable;

public class CalendarFragment extends BaseFragment<CalendarViewImpl, CalendarPresenter> implements GridEmployeeItemAdapter.OnEmployeeItemClickListener {

    @BindView(R.id.tvCalendarNoResults)
    TextView tvNoResults;
    @BindView(R.id.timeTable)
    TimeTable timeTable;

    private ActionBarChangeListener actionBarChangeListener;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        actionBarChangeListener = (ActionBarChangeListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        initUi();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        actionBarChangeListener.showBackButton();
        actionBarChangeListener.setActionBarTitle(getString(R.string.tm_hr_calendar_fragment_title));
        inflater.inflate(R.menu.menu_calendar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        actionBarChangeListener = null;
    }


    @Override
    protected CalendarViewImpl initView() {
        return new CalendarViewImpl(this, getActivity().findViewById(android.R.id.content)) {
            @Override
            public <T extends IGridItem> void updateTableWithDateRange(@NonNull T item, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar from, @NonNull Calendar to) {
                tvNoResults.setVisibility(View.GONE);
                timeTable.setVisibility(View.VISIBLE);
                timeTable.setItemsWithDateRange(item, allTimeOffs, from, to, CalendarFragment.this);
            }

            @Override
            public void showNoResults() {
                timeTable.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
            }
        };
    }


    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }


    @Override
    public void onEmployeeItemClick() {
       // fragmentCallback.addDetailsFragment();
    }


    private void initUi() {
        presenter.setupCalendarRange();
        presenter.performRequests();
    }
}