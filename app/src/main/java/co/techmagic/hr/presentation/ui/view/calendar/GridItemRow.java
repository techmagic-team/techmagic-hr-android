package co.techmagic.hr.presentation.ui.view.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmployeeGridYitem;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.presentation.pojo.UserAllTimeOffsMap;
import co.techmagic.hr.presentation.pojo.UserTimeOff;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridCellItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.util.DateUtil;

/**
 * Created by Wiebe Geertsma on 13-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class GridItemRow<T extends IGridItem> {

    private EmployeeGridYitem employeeGridYitem;
    private List<GridCellItemAdapter> items;

    public GridItemRow(EmployeeGridYitem employeeGridYitem, TimeRange timeRange, UserAllTimeOffsMap userAllTimeOffsMap, List<CalendarInfoDto> calendarInfo) {
        this.employeeGridYitem = employeeGridYitem;
        items = generateGridItems(fitItems(userAllTimeOffsMap), timeRange, calendarInfo);
    }

    private List<GridCellItemAdapter> generateGridItems(UserAllTimeOffsMap userAllTimeOffsMap, TimeRange timeRange, List<CalendarInfoDto> calendarInfoList) {
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
            List<UserTimeOff> dayOffs = getTimeOff(userAllTimeOffsMap, employeeGridYitem.getId(), TimeOffType.DAYOFF);

            if (dayOffs != null) {
                for (UserTimeOff timeOff : dayOffs) {
                    if (shouldTimeOffBeInCurrentCell(timeOff.getDateFrom(), timeOff.getDateTo(), cellTime.getTime())) {
                        gridCellItemAdapter.setHasDayOff(true);
                    }
                }
            }

            /* Check for vacation */
            List<UserTimeOff> vacations = getTimeOff(userAllTimeOffsMap, employeeGridYitem.getId(), TimeOffType.VACATION);

            if (vacations != null) {
                for (UserTimeOff vacation : vacations) {
                    if (shouldTimeOffBeInCurrentCell(vacation.getDateFrom(), vacation.getDateTo(), cellTime.getTime())) {
                        gridCellItemAdapter.setHasVacation(true);
                    }
                }
            }

            /* Check for illness */
            List<UserTimeOff> illnesses = getTimeOff(userAllTimeOffsMap, employeeGridYitem.getId(), TimeOffType.ILLNESS);

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

    private List<UserTimeOff> getTimeOff(UserAllTimeOffsMap userAllTimeOffsMap, String userId, @NonNull TimeOffType timeOffType) {
        Set<Docs> users = userAllTimeOffsMap.getMap().keySet();
        List<UserTimeOff> timeOffsForUser = new ArrayList<>();

        for (Docs user : users) {
            if (userId.equals(user.getId())) {
                timeOffsForUser.addAll(userAllTimeOffsMap.getMap().get(user));
                break;
            }
        }

        Iterator<UserTimeOff> iterator = timeOffsForUser.iterator();
        while (iterator.hasNext()) {
            UserTimeOff userTimeOff = iterator.next();
            if (userTimeOff.getTimeOffType() != timeOffType) {
                iterator.remove();
            }
        }

        return timeOffsForUser;
    }

    private UserAllTimeOffsMap fitItems(UserAllTimeOffsMap userAllTimeOffsMap) {
        // TODO: 5/15/17
        return userAllTimeOffsMap;
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

    private static <T extends IGridItem> List<GridCellItemAdapter> generateGridItems(final List<List<T>> itemsList, final TimeRange timeRange, final AllTimeOffs allTimeOffs) {
        final int rows = itemsList.size();
        final int columns = timeRange.getColumnCount();
        List<GridCellItemAdapter> gridItems = new ArrayList<>();

        for (int y = 0; y < rows; y++) {
            Calendar cellTime = Calendar.getInstance();
            cellTime.setTimeInMillis(timeRange.getStart().getTimeInMillis());
            cellTime.add(Calendar.DATE, 1);

            for (int x = 0; x < columns; x++) {
                GridCellItemAdapter gridCellItemAdapter = new GridCellItemAdapter();
//                for (T item : itemsList.get(y)) {
//                    if (item.getTimeRange() != null && item.getTimeRange().isWithin(cellTime)) {
//                        gridCellItemAdapter = new GridCellItemAdapter();
//                        break;
//                    }
//                }

//                if (gridCellItemAdapter == null)
//                    gridCellItemAdapter = new GridCellItemAdapter();

                /*else if (!gridItems.isEmpty() && gridItems.size() > 0) {
                    GridXitem lastItem = gridItems.get((y * columns) + x - 1);
                    gridXitem.setStart(lastItem.isEmpty() || !gridXitem.getModel().equals(lastItem.getModel()));
                }*/

                if (cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cellTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    gridCellItemAdapter.setIsWeekend(true);
                }

                /* Check for holidays */

//                Random random = new Random();
//                int randomInt = random.nextInt(10);
//                if (randomInt % 3 == 0) {
//                    gridCellItemAdapter.setHasVacation(true);
//                }

                if (allTimeOffs.getCalendarInfo() != null) {
                    for (CalendarInfo c : allTimeOffs.getCalendarInfo()) {
                        if (cellTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US).equals(c.getName())) {
                            for (int h = 0; h < c.getHolidays().size(); h++) {
                                if ((cellTime.get(Calendar.DAY_OF_MONTH)) == c.getHolidays().get(h).getDate()) {
                                    gridCellItemAdapter.setHasHolidays(true);
                                }
                            }
                        }
                    }
                }

                 /* Check for day off */

                if (allTimeOffs.getDayOffs() != null) {
                    for (RequestedTimeOff dayOff : allTimeOffs.getDayOffs()) {
                        if (shouldTimeOffBeInCurrentCell(dayOff.getDateFrom(), dayOff.getDateTo(), cellTime.getTime())) {
                            gridCellItemAdapter.setHasDayOff(true);
                        }
                    }
                }

                /* Check for vacation */

                if (allTimeOffs.getVacations() != null) {
                    for (RequestedTimeOff vacation : allTimeOffs.getVacations()) {
                        if (shouldTimeOffBeInCurrentCell(vacation.getDateFrom(), vacation.getDateTo(), cellTime.getTime())) {
                            gridCellItemAdapter.setHasVacation(true);
                        }
                    }
                }

                /* Check for illness */

                if (allTimeOffs.getIllnesses() != null) {
                    for (RequestedTimeOff illness : allTimeOffs.getIllnesses()) {
                        if (shouldTimeOffBeInCurrentCell(illness.getDateFrom(), illness.getDateTo(), cellTime.getTime())) {
                            gridCellItemAdapter.setHasIllness(true);
                        }
                    }
                }

                gridItems.add(gridCellItemAdapter);
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
}