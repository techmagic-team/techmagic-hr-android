package co.techmagic.hr.presentation.ui.view.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.EmployeeGridYitem;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.presentation.pojo.UserTimeOff;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridCellItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;

/**
 * Created by Wiebe Geertsma on 13-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class GridItemRow<T extends IGridItem> {

    private EmployeeGridYitem employeeGridYitem;
    private List<GridCellItemAdapter> items;

    public GridItemRow(EmployeeGridYitem employeeGridYitem, TimeRange timeRange, List<UserTimeOff> timeOffs, List<CalendarInfoDto> calendarInfo) {
        this.employeeGridYitem = employeeGridYitem;
        items = generateGridItems(fitItems(timeOffs), timeRange, calendarInfo);
    }

//    /**
//     * Convert a list of potentially overlapping items into a list of lists containing IGridItems that don't overlap.
//     *
//     * @param list the unsorted list of IGridItems
//     * @return the list of
//     */

//    private static <T extends IGridItem> List<List<T>> fitItems(List<UserTimeOff> list) {
//        List<List<T>> sortedList = new ArrayList<>();
//        sortedList.add(new ArrayList<>()); // Add the initial item
//
//        // Cycle until there are no more items left
//        for (T item : list) {
//            boolean wasAdded = true;
//            for (List<T> currentRowList : sortedList) {
//                boolean fitsInCurrentRow = true;
//                for (IGridItem rowItem : currentRowList) { // Check if there are overlapping items in this row
//                    if (item.getTimeRange() != null && rowItem.getTimeRange() != null && item.getTimeRange().overlaps(rowItem.getTimeRange())) {
//                        fitsInCurrentRow = false;
//                        break;
//                    }
//                }
//
//                if (fitsInCurrentRow)
//                    currentRowList.add(item);
//                else
//                    wasAdded = false;
//            }
//            if (!wasAdded) {
//                List<T> newList = new ArrayList<>();
//                newList.add(item);
//                sortedList.add(newList);
//            }
//        }
//
//        return sortedList;
//    }

    private static boolean shouldTimeOffBeInCurrentCell(String start, String end, Date inputDate) {
       // return DateUtil.isValidDatesRange(start, end, inputDate);
        return true; // todo
    }

    /**
     * Here we get the final items for display.
     * We determine if we need one or two rows.
     * If we need two rows for this person, this function returns a total of two rows of items.
     *
     * @return all items for all required rows for this person.
     */

    public List<GridCellItemAdapter> getItems() {
        return items;
    }

    public EmployeeGridYitem getEmployeeGridYitem() {
        return employeeGridYitem;
    }

    public String getId() {
        return employeeGridYitem.getId();
    }

    public String getPersonName() {
        return employeeGridYitem.getName();
    }

    public String getPhotoUrl() {
        return employeeGridYitem.getPhotoUrl();
    }

    private List<GridCellItemAdapter> generateGridItems(List<UserTimeOff> timeOffs, TimeRange timeRange, List<CalendarInfoDto> calendarInfoList) {
        final int columns = timeRange.getColumnCount();
        List<GridCellItemAdapter> gridItems = new ArrayList<>();

        Calendar cellTime = Calendar.getInstance();
        cellTime.setTimeInMillis(timeRange.getStart().getTimeInMillis());
        cellTime.add(Calendar.DATE, 1);

        for (int x = 0; x < columns; x++) {
            GridCellItemAdapter gridCellItemAdapter = new GridCellItemAdapter();

            if (cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                gridCellItemAdapter.setIsWeekend(true);
            }

            /* Check for holidays */
            if (calendarInfoList != null) {
                for (CalendarInfoDto calendarInfoDto : calendarInfoList) {
                    if (cellTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US).equals(calendarInfoDto.getName())) {
                        for (int i = 0; i < calendarInfoDto.getHolidays().size(); i++) {
                            if ((cellTime.get(Calendar.DAY_OF_MONTH)) == calendarInfoDto.getHolidays().get(i).getDate()) {
                                gridCellItemAdapter.setHasHolidays(true);
                            }
                        }
                    }
                }
            }

            /* Check for day off */
            List<UserTimeOff> dayOffs = getTimeOff(timeOffs, TimeOffType.DAYOFF);

            if (dayOffs != null) {
                for (UserTimeOff timeOff : dayOffs) {
                    if (shouldTimeOffBeInCurrentCell(timeOff.getDateFrom(), timeOff.getDateTo(), cellTime.getTime())) {
                        gridCellItemAdapter.setHasDayOff(true);
                    }
                }
            }

            /* Check for vacation */
            List<UserTimeOff> vacations = getTimeOff(timeOffs, TimeOffType.VACATION);

            if (vacations != null) {
                for (UserTimeOff vacation : vacations) {
                    if (shouldTimeOffBeInCurrentCell(vacation.getDateFrom(), vacation.getDateTo(), cellTime.getTime())) {
                        gridCellItemAdapter.setHasVacation(true);
                    }
                }
            }

            /* Check for illness */
            List<UserTimeOff> illnesses = getTimeOff(timeOffs, TimeOffType.ILLNESS);

            if (illnesses != null) {
                for (UserTimeOff illness : illnesses) {
                    if (shouldTimeOffBeInCurrentCell(illness.getDateFrom(), illness.getDateTo(), cellTime.getTime())) {
                        gridCellItemAdapter.setHasIllness(true);
                    }
                }
            }

            gridItems.add(gridCellItemAdapter);
            cellTime.add(Calendar.DATE, 1);
        }

        return gridItems;
    }

    private List<UserTimeOff> getTimeOff(List<UserTimeOff> timeOffs, @NonNull TimeOffType timeOffType) {
        List<UserTimeOff> targetTimeOffs = new ArrayList<>(timeOffs.size());
        targetTimeOffs.addAll(timeOffs);

        Iterator<UserTimeOff> iterator = targetTimeOffs.iterator();
        while (iterator.hasNext()) {
            UserTimeOff userTimeOff = iterator.next();
            if (userTimeOff.getTimeOffType() != timeOffType) {
                iterator.remove();
            }
        }

        return targetTimeOffs;
    }

    private List<UserTimeOff> fitItems(List<UserTimeOff> list) {
        // TODO: 5/15/17
        return list;
    }
}