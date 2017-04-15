package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.ui.adapter.GridCalendarAdapter;
import co.techmagic.hr.presentation.ui.adapter.GridSpacingDecorator;
import co.techmagic.hr.presentation.ui.adapter.UserCalendarAdapter;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.rvUserCalendar)
    RecyclerView rvUserCalendar;
    @BindView(R.id.rvCalendarGrid)
    RecyclerView rvCalendarGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        rvUserCalendar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvUserCalendar.setAdapter(new UserCalendarAdapter());

        rvCalendarGrid.setLayoutManager(new GridLayoutManager(this, 31, GridLayoutManager.VERTICAL, false));
        rvCalendarGrid.addItemDecoration(new GridSpacingDecorator(31, 0, false, 0));
        rvCalendarGrid.setAdapter(new GridCalendarAdapter());
    }
}
