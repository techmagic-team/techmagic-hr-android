package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.presentation.pojo.UserAllTimeOffsMap;

public interface CalendarView extends View {

    void showNoResults();

    void showClearFilters();

    void hideClearFilters();

    void updateTableWithDateRange(UserAllTimeOffsMap userAllTimeOffsMap, List<CalendarInfoDto> calendarInfo, Calendar dateFrom, Calendar dateTo);

    void addDetailsFragment(@NonNull Docs docs);
}
