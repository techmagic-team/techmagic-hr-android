package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.techmagic.hr.presentation.mvp.view.CalendarView;
import co.techmagic.hr.presentation.ui.view.timetable.EmployeePlanItem;
import co.techmagic.hr.presentation.util.DateUtil;

public class CalendarPresenter extends BasePresenter<CalendarView> {

    private Calendar dateFrom = null;
    private Calendar dateTo = null;


    public CalendarPresenter() {

    }


    @Override
    protected void onViewAttached() {
        super.onViewAttached();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    public void setupPage() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        showFromDate(from);
        showToDate(to);
        view.updateTableWithDateRange(generateSamplePlanData(), from, to);
    }


    public void updateCalendar(@Nullable Calendar from, @Nullable Calendar to) {
        if (from != null) {
            dateFrom = from;
        }

        if (to != null) {
            dateTo = to;
        }

        Calendar c = Calendar.getInstance();

        /* Date range didn't change earlier
        *  Set current year by default
        */

        if (dateFrom == null) {
            dateFrom = c;
            dateFrom.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        } else if (dateTo == null) {
            dateTo = c;
            dateTo.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        }

        view.updateTableWithDateRange(generateSamplePlanData(), dateFrom, dateTo);
    }


    public void onFromButtonClick() {
        view.showDatePicker(dateFrom, dateTo, true);
    }


    public void onToButtonClick() {
        view.showDatePicker(dateFrom, dateTo, false);
    }


    private void showFromDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
        dateFrom = c;
        Date date = c.getTime();
        view.updateSelectedFromButtonText(DateUtil.getFormattedMonthAndYear(date));
    }


    private void showToDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        dateTo = c;
        Date date = c.getTime();
        view.updateSelectedToButtonText(DateUtil.getFormattedMonthAndYear(date));
    }


    private static List<EmployeePlanItem> generateSamplePlanData() {
        List<EmployeePlanItem> planItems = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            planItems.add(EmployeePlanItem.generateSample());

        return planItems;
    }
}