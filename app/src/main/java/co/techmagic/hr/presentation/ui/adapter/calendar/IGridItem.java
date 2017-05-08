package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.annotation.Nullable;

import java.util.List;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.ui.view.calendar.TimeRange;


public interface IGridItem {

    void setEmployees(List<Docs> employees);

    List<Docs> getEmployees();

    void setTimeRange(TimeRange timeRange);
    /**
     * If the TimeRange is null, your item will NOT be displayed.
     *
     * @return the time range of this item
     */
    @Nullable
    TimeRange getTimeRange();

    void setPersonName(String personName);

    /**
     * Get the text that is displayed on the Y axis.
     *
     * @return the text that is displayed on the Y axis
     */
    String getPersonName();

    void setPhotoUrl(String photoUrl);

    /**
     * Get the photo url that is displayed on the Y axis.
     *
     * @return the photo url that is displayed on the Y axis
     */
    String getPhotoUrl();
}