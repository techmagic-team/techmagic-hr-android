package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.annotation.Nullable;

import co.techmagic.hr.presentation.ui.view.calendar.TimeRange;


public interface IGridItem {
    /**
     * If the TimeRange is null, your item will NOT be displayed.
     *
     * @return the time range of this item
     */
    @Nullable
    TimeRange getTimeRange();

    /**
     * Get the text that is displayed in the tile.
     *
     * @return the text that is displayed in the tile
     */
    String getName();

    /**
     * Get the text that is displayed on the Y axis.
     *
     * @return the text that is displayed on the Y axis
     */
    String getPersonName();

    /**
     * Get the photo url that is displayed on the Y axis.
     *
     * @return the photo url that is displayed on the Y axis
     */
    String getPhotoUrl();
}
