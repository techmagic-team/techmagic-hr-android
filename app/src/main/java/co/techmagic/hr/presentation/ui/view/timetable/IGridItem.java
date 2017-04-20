package co.techmagic.hr.presentation.ui.view.timetable;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Interface for your plan item.
 * Your class just needs a time range, an item tvItemY (for example "Renovation block A") and the tvItemY of the person
 * that is assigned to this plan.
 * <p>
 * Created by Wiebe Geertsma on 12-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */
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
     * Get the item's color.
     *
     * @return the color of the item
     */
    int getItemColor();

    /**
     * Executed when the user clicked on the item.
     */
    void onClick(View view);
}
