package co.techmagic.hr.presentation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.techmagic.hr.R;
import co.techmagic.hr.presentation.ui.view.timetable.EmployeePlanItem;
import co.techmagic.hr.presentation.ui.view.timetable.TimeTable;

public class CalendarActivity extends AppCompatActivity {

    ActionBar actionBar;
    @BindView(R.id.timeTable)
    TimeTable timeTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Calendar");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        timeTable.setItems(generateSamplePlanData());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private static List<EmployeePlanItem> generateSamplePlanData() {
        List<EmployeePlanItem> planItems = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            planItems.add(EmployeePlanItem.generateSample());

        return planItems;
    }
}