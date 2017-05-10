package co.techmagic.hr.presentation.ui.view.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.EmployeeGridYitem;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridXitem;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.util.DateUtil;

/**
 * Created by Wiebe Geertsma on 13-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class GridItemRow<T extends IGridItem> {

    private final TimeRange timeRange;
    private final String personName;
    private final String photoUrl;
    private final List<GridXitem> items;


    public GridItemRow(final EmployeeGridYitem employeeGridYitem, final TimeRange timeRange, final List<T> containedItems, final AllTimeOffs allTimeOffs) {
        this.timeRange = timeRange; // We need to keep track of the time range of this row so we can display the current day
        personName = employeeGridYitem.getName();
        photoUrl = employeeGridYitem.getPhotoUrl();
        items = generateGridItems(fitItems(containedItems), timeRange, allTimeOffs);
    }


    /**
     * Convert a list of potentially overlapping items into a list of lists containing IGridItems that don't overlap.
     *
     * @param list the unsorted list of IGridItems
     * @return the list of
     */

    private static <T extends IGridItem> List<List<T>> fitItems(List<T> list) {
        List<List<T>> sortedList = new ArrayList<>();
        sortedList.add(new ArrayList<>()); // Add the initial item

        // Cycle until there are no more items left
        for (T item : list) {
            boolean wasAdded = true;
            for (List<T> currentRowList : sortedList) {
                boolean fitsInCurrentRow = true;
                for (IGridItem rowItem : currentRowList) { // Check if there are overlapping items in this row
                    if (item.getTimeRange() != null && rowItem.getTimeRange() != null && item.getTimeRange().overlaps(rowItem.getTimeRange())) {
                        fitsInCurrentRow = false;
                        break;
                    }
                }

                if (fitsInCurrentRow)
                    currentRowList.add(item);
                else
                    wasAdded = false;
            }
            if (!wasAdded) {
                List<T> newList = new ArrayList<>();
                newList.add(item);
                sortedList.add(newList);
            }
        }

        return sortedList;
    }

    /**
     * Generate the cells. The items list should have already been sorted.
     * The items in every list of itemsList should never overlap, or otherwise it will only display the first
     * item it finds that corresponds to the time of the cell.
     *
     * @param itemsList the list containing items for each row.
     * @param timeRange the time range (start to end) of this row.
     * @return the generated list of GridItems ready to display in the RecyclerView.
     */

    private static <T extends IGridItem> List<GridXitem> generateGridItems(final List<List<T>> itemsList, final TimeRange timeRange, final AllTimeOffs allTimeOffs) {
        final int rows = itemsList.size();
        final int columns = timeRange.getColumnCount();
        List<GridXitem> gridItems = new ArrayList<>();

        for (int y = 0; y < rows; y++) {
            Calendar cellTime = Calendar.getInstance();
            cellTime.setTimeInMillis(timeRange.getStart().getTimeInMillis());
            cellTime.add(Calendar.DATE, 1);

            for (int x = 0; x < columns; x++) {
                GridXitem gridXitem = null;
                for (T item : itemsList.get(y)) {
                    if (item.getTimeRange() == null)
                        continue; // Skip any items that have null start or end
                    if (item.getTimeRange().isWithin(cellTime)) {
                        gridXitem = new GridXitem(allTimeOffs, x, y);
                        break;
                    }
                }

                if (gridXitem == null)
                    gridXitem = new GridXitem(x, y);

                /*else if (!gridItems.isEmpty() && gridItems.size() > 0) {
                    GridXitem lastItem = gridItems.get((y * columns) + x - 1);
                    gridXitem.setStart(lastItem.isEmpty() || !gridXitem.getModel().equals(lastItem.getModel()));
                }*/

                if (cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    gridXitem.setIsWeekend(true);

                /* Check for holidays */

                if (allTimeOffs.getCalendarInfo() != null) {
                    for (CalendarInfo c : allTimeOffs.getCalendarInfo()) {
                        if (cellTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US).equals(c.getName())) {
                            for (int h = 0; h < c.getHolidays().size(); h++) {
                                if ((cellTime.get(Calendar.DAY_OF_MONTH)) == c.getHolidays().get(h).getDate()) {
                                    gridXitem.setHasHolidays(true);
                                }
                            }
                        }
                    }
                }

                 /* Check for day off *//*

                /*if (allTimeOffs.getDayOffs() != null) {
                    for (RequestedTimeOff dayOff : allTimeOffs.getDayOffs()) {
                        if (shouldTimeOffBeInCurrentCell(dayOff.getDateFrom(), dayOff.getDateTo(), cellTime.getTime())) {
                            gridXitem.setHasDayOff(true);
                        }
                    }
                }

                *//* Check for vacation *//*

                if (allTimeOffs.getVacations() != null) {
                    for (RequestedTimeOff vacation : allTimeOffs.getVacations()) {
                        if (shouldTimeOffBeInCurrentCell(vacation.getDateFrom(), vacation.getDateTo(), cellTime.getTime())) {
                            gridXitem.setHasVacation(true);
                        }
                    }
                }

                *//* Check for illness *//*

                if (allTimeOffs.getIllnesses() != null) {
                    for (RequestedTimeOff illness : allTimeOffs.getIllnesses()) {
                        if (shouldTimeOffBeInCurrentCell(illness.getDateFrom(), illness.getDateTo(), cellTime.getTime())) {
                            gridXitem.setHasIllness(true);
                        }
                    }
                }*/

                gridItems.add(gridXitem);
                cellTime.add(Calendar.DATE, 1);
            }
        }

        return gridItems;
    }


    private static boolean shouldTimeOffBeInCurrentCell(Date start, Date end, Date inputDate) {
        return DateUtil.isValidDatesRange(start, end, inputDate);
    }


    /**
     * Here we get the final items for display.
     * We determine if we need one or two rows.
     * If we need two rows for this person, this function returns a total of two rows of items.
     *
     * @return all items for all required rows for this person.
     */

    public List<GridXitem> getItems() {
        return items;
    }


    public String getPersonName() {
        return personName;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }
}