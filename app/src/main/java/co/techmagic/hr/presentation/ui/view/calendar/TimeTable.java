package co.techmagic.hr.presentation.ui.view.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.EmployeeGridYitem;
import co.techmagic.hr.domain.pojo.CalendarInfoDto;
import co.techmagic.hr.presentation.pojo.UserAllTimeOffsMap;
import co.techmagic.hr.presentation.pojo.UserTimeOff;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridCellItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridCellItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGuideYItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.IWeekDayItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.WeekDayHeaderItemAdapter;
import co.techmagic.hr.presentation.util.DateUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wiebe Geertsma on 14-11-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class TimeTable extends FrameLayout {

    private final String DTAG = "TimeTable";

    private RecyclerView recyclerView, guideY, guideX;
    private List<RecyclerView> observedList;
    private Calendar left, right;
    private TimeRange timeRange;
    private int columns;

    private FastItemAdapter guideXadapter, guideYadapter, gridAdapter;


    public TimeTable(Context context) {
        super(context);
        init(null);
    }


    public TimeTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public TimeTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.timetable, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvGrid);
        guideY = (RecyclerView) view.findViewById(R.id.rvGuideY);
        guideX = (RecyclerView) view.findViewById(R.id.rvGuideX);

        guideY.setHasFixedSize(true);
        guideY.setItemAnimator(null);
        guideX.setHasFixedSize(true);
        guideX.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);

        addView(view);
        requestLayout();
    }


    /**
     * Sets the items to be displayed.
     */

    public void setItemsWithDateRange(UserAllTimeOffsMap userAllTimeOffsMap, List<CalendarInfoDto> calendarInfo, Calendar dateFrom, Calendar dateTo,
                                      @NonNull OnCalendarViewReadyListener onCalendarViewReadyListener, @NonNull GridEmployeeItemAdapter.OnEmployeeItemClickListener onEmployeeItemClickListener) {
        setTimeRange(dateFrom, dateTo);
        left.setTimeInMillis(DateUtil.calendarToMidnightMillis(left));
        right.setTimeInMillis(DateUtil.calendarToMidnightMillis(right));

        // Generate items spanning from start(left) to end(right)
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(DateUtil.calendarToMidnightMillis(left));
        List<WeekDayHeaderItemAdapter> headerItems = new ArrayList<>();

        while (current.getTimeInMillis() <= right.getTimeInMillis()) {
            headerItems.add(new WeekDayHeaderItemAdapter(current));
            current.add(Calendar.DATE, 1);
        }

        setHeaderItems(headerItems);
        columns = timeRange.getColumnCount();
        construct(columns);

        List<GridItemRow> rows = new ArrayList<>();
        List<GridCellItemAdapter> allGridItems = new ArrayList<>();
        List<GridEmployeeItemAdapter> employeeItems = new ArrayList<>();

        Observable.from(userAllTimeOffsMap.getMap().keySet()).map(docs -> getGridItemRow(userAllTimeOffsMap, calendarInfo, docs))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GridItemRow>() {
                    @Override
                    public void onCompleted() {
                        setGridItems(allGridItems);
                        setEmployeeItems(employeeItems, onEmployeeItemClickListener);
                        requestLayout();

                        scrollToCurrentMonth();

                        onCalendarViewReadyListener.onCalendarVisible();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GridItemRow gridItemRow) {
                        rows.add(gridItemRow);

                        List<GridCellItemAdapter> cells = gridItemRow.getItems();
                        allGridItems.addAll(cells);

                        for (int i = 0; i < cells.size() / columns; i++) {
                            employeeItems.add(new GridEmployeeItemAdapter(gridItemRow));
                        }
                    }
                });
    }


    private GridItemRow getGridItemRow(UserAllTimeOffsMap userAllTimeOffsMap, List<CalendarInfoDto> calendarInfo, UserProfile userProfile) {
        EmployeeGridYitem employeeGridYitem = new EmployeeGridYitem(userProfile.getId(), userProfile.getLastName() + " " + userProfile.getFirstName(), userProfile.getPhoto()); // Last name + first name

        List<UserTimeOff> timeOffsForUser = getTimeOffsForUser(userAllTimeOffsMap, userProfile.getId());
        List<UserTimeOff> requestedOffsForUser = getRequestedTimeOffsForUser(userAllTimeOffsMap, userProfile.getId());

        GridItemRow gridRow = new GridItemRow(employeeGridYitem, new TimeRange(left, right), timeOffsForUser, requestedOffsForUser, calendarInfo);
        return gridRow;
    }


    private void construct(final int itemCount) {
        guideX.setHasFixedSize(true);
        guideX.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // Do not allow scrolling with the X or Y for now because we do not have a scrollToPositionWithOffset yet in our FixedGridLayout
        guideX.addOnItemTouchListener(getOnItemTouchListener());

        guideY.setHasFixedSize(true);
        guideY.setLayoutManager(getScrollLayoutManager());

        // Do not allow scrolling with the X or Y for now because we do not have a scrollToPositionWithOffset yet in our FixedGridLayout
        // guideY.addOnItemTouchListener(itemTouchListener); // !!! Touch listener intercepts click event !!!

        observedList = new ArrayList<RecyclerView>() {{
            add(guideX);
            add(guideY);
        }};

        FixedGridLayoutManager mgr = new FixedGridLayoutManager();
        mgr.setTotalColumnCount(itemCount);
        recyclerView.setLayoutManager(mgr);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int state;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                final LinearLayoutManager managerX = (LinearLayoutManager) observedList.get(0).getLayoutManager();
                final LinearLayoutManager managerY = (LinearLayoutManager) observedList.get(1).getLayoutManager();
                final FixedGridLayoutManager layoutMgr = (FixedGridLayoutManager) recyclerView.getLayoutManager();

                final int firstRow = layoutMgr.getFirstVisibleRow();
                final int firstColumn = layoutMgr.getFirstVisibleColumn();

                View firstVisibleItem = layoutMgr.getChildAt(1);
                if (firstVisibleItem != null) {
                    int decoratedY = layoutMgr.getDecoratedBottom(firstVisibleItem);
                    int decoratedX = layoutMgr.getDecoratedLeft(firstVisibleItem);


                    if (managerX != null)
                        managerX.scrollToPositionWithOffset(firstColumn + 1, decoratedX);
                    if (managerY != null)
                        managerY.scrollToPositionWithOffset(firstRow + 1, decoratedY);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
            }
        });
    }


    /**
     * Initializes the horizontal guide which displays the days, dates.
     *
     * @param left  the time on the left end
     * @param right the time on the right end
     */
    public void setTimeRange(Calendar left, Calendar right) {
        if (left.getTimeInMillis() > right.getTimeInMillis()) {
            Log.e(DTAG, "setTimeRange 'left' cannot be higher than 'right'.");
            return;
        }

        this.left = left;
        this.right = right;
        this.timeRange = new TimeRange(left, right);
    }


    public <T> void setGridItems(List<T> items) {
        if (gridAdapter == null) {
            gridAdapter = new FastItemAdapter<>();
            gridAdapter.setHasStableIds(true);
            gridAdapter.withSelectable(true);
            gridAdapter.withOnClickListener((v, adapter, item, position) -> {
                if (item instanceof IGridCellItem) {
                    ((IGridCellItem) item).onItemClick(v);
                }
                return true;
            });
            recyclerView.setAdapter(gridAdapter);
        }

        gridAdapter.clear();
        gridAdapter.set(items);
    }


    public <T extends IWeekDayItem> void setHeaderItems(List<T> items) {
        if (guideXadapter == null) {
            guideXadapter = new FastItemAdapter();
            guideXadapter.setHasStableIds(true);
            guideXadapter.withSelectable(false);
            guideX.setAdapter(guideXadapter);
        }

        guideXadapter.clear();
        guideXadapter.set(items);
    }


    public void setEmployeeItems(List<GridEmployeeItemAdapter> items, @NonNull GridEmployeeItemAdapter.OnEmployeeItemClickListener onEmployeeItemClickListener) {
        if (guideYadapter == null) {
            guideYadapter = new FastItemAdapter();
            guideYadapter.setHasStableIds(true);
            guideYadapter.withSelectable(true);
            guideYadapter.withOnClickListener((v, adapter, item, position) -> {
                if (item instanceof IGuideYItem) {
                    onEmployeeItemClickListener.onEmployeeItemClick(((IGuideYItem) item).getId());
                }
                return true;
            });
            guideY.setAdapter(guideYadapter);
        }

        guideYadapter.clear();
        guideYadapter.set(items);
    }


    public void scrollToCurrentMonth() {
        if (guideX != null && guideXadapter != null && guideXadapter.getItemCount() > 0) {
            Calendar dateToScroll = Calendar.getInstance();
            dateToScroll.set(Calendar.DAY_OF_MONTH, 1); // From first day of month

            int days = getDifferenceDaysBetween(left, dateToScroll);
            int dayToScroll = days - 1;

            guideX.scrollToPosition(dayToScroll);
            recyclerView.scrollToPosition(dayToScroll);
        }
    }

    /**
     * @param left
     * @param right
     * returns days between right - left
     * */

    private int getDifferenceDaysBetween(Calendar left, Calendar right) {
        long diff = right.getTime().getTime() - left.getTime().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        return (int) diffDays;
    }


    private List<UserTimeOff> getTimeOffsForUser(UserAllTimeOffsMap userAllTimeOffsMap, String userId) {
        Set<UserProfile> users = userAllTimeOffsMap.getMap().keySet();
        List<UserTimeOff> timeOffsForUser = new ArrayList<>();

        for (UserProfile user : users) {
            if (userId.equals(user.getId())) {
                timeOffsForUser.addAll(userAllTimeOffsMap.getMap().get(user));
                break;
            }
        }

        return timeOffsForUser;
    }


    private List<UserTimeOff> getRequestedTimeOffsForUser(UserAllTimeOffsMap userAllTimeOffsMap, String userId) {
        Set<UserProfile> users = userAllTimeOffsMap.getRequestedMap().keySet();
        List<UserTimeOff> requestedTimeOffsForUser = new ArrayList<>();

        for (UserProfile user : users) {
            if (userId.equals(user.getId())) {
                requestedTimeOffsForUser.addAll(userAllTimeOffsMap.getRequestedMap().get(user));
                break;
            }
        }

        return requestedTimeOffsForUser;
    }


    public Calendar getTimeLeft() {
        return left;
    }


    public Calendar getTimeRight() {
        return right;
    }


    private RecyclerView.OnItemTouchListener getOnItemTouchListener() {
        return new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                // Do nothing.
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    }


    private ScrollLayoutManager getScrollLayoutManager() {
        return new ScrollLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }
}