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
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EmployeeGridYitem;
import co.techmagic.hr.presentation.ui.adapter.calendar.AllTimeOffs;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridCellItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGridItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.IGuideYItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.IWeekDayItem;
import co.techmagic.hr.presentation.ui.adapter.calendar.WeekDayHeaderItemAdapter;
import co.techmagic.hr.presentation.ui.view.OnCalendarViewReadyListener;

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
     *
     * @param data the items to be displayed.
     */

    public <T extends IGridItem> void setItemsWithDateRange(@NonNull T data, @NonNull AllTimeOffs allTimeOffs, @NonNull Calendar calFrom, @NonNull Calendar calTo,
                                                            @NonNull GridEmployeeItemAdapter.OnEmployeeItemClickListener onEmployeeItemClickListener,
                                                            @NonNull OnCalendarViewReadyListener onCalendarViewReadyListener) {

        /* Hide progress listener */

        guideY.setTag(guideY.getVisibility());
        ViewTreeObserver observer = guideY.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            int visibility = guideY.getVisibility();

            if (visibility == VISIBLE) {
                onCalendarViewReadyListener.onCalendarVisible();
            }

            guideY.setTag(visibility);
        });

        setTimeRange(calFrom, calTo);
        left.setTimeInMillis(calendarToMidnightMillis(left));
        right.setTimeInMillis(calendarToMidnightMillis(right));

        // Generate items spanning from start(left) to end(right)
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(calendarToMidnightMillis(left));
        List<WeekDayHeaderItemAdapter> headerItems = new ArrayList<>();

        while (current.getTimeInMillis() <= right.getTimeInMillis()) {
            headerItems.add(new WeekDayHeaderItemAdapter(current));
            current.add(Calendar.DATE, 1);
        }

        setHeaderItems(headerItems);
        columns = timeRange.getColumnCount();
        construct(columns);

        List<Pair<EmployeeGridYitem, List<IGridItem>>> pairs = new ArrayList<>();

        for (int i = 0; i < data.getEmployees().size(); i++) {
            Docs item = data.getEmployees().get(i);
            Pair<EmployeeGridYitem, List<IGridItem>> pair = null;
            for (Pair<EmployeeGridYitem, List<IGridItem>> p : pairs) {
                if (p.first.getName() != null && p.first.getName().equals(item.getPersonName())) {
                    pair = p;
                    break;
                }
            }

            if (pair == null)
                pair = new Pair<>(new EmployeeGridYitem(item.getId(), item.getPersonName(), item.getPhotoUrl()), new ArrayList<IGridItem>());

            pair.second.add(item);

            if (!pairs.contains(pair))
                pairs.add(pair);
        }

        List<GridItemRow> rows = new ArrayList<>();
        for (Pair<EmployeeGridYitem, List<IGridItem>> pair : pairs) {
            GridItemRow gridRow = new GridItemRow(pair.first, new TimeRange(left, right), pair.second, allTimeOffs);
            rows.add(gridRow);
        }


        List<GridCellItemAdapter> allGridItems = new ArrayList<>();
        List<GridEmployeeItemAdapter> employeeItems = new ArrayList<>();
        for (GridItemRow r : rows) {
            List<GridCellItemAdapter> l = r.getItems();
            allGridItems.addAll(l);

            for (int i = 0; i < l.size() / columns; i++)
                employeeItems.add(new GridEmployeeItemAdapter(r));
        }

        setGridItems(allGridItems);
        setEmployeeItems(employeeItems, onEmployeeItemClickListener);
        requestLayout();
        // center(); // todo scroll to current month
    }


    private void construct(final int itemCount) {
        final RecyclerView.OnItemTouchListener itemTouchListener = new RecyclerView.OnItemTouchListener() {
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
        guideX.setHasFixedSize(true);
        guideX.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // Do not allow scrolling with the X or Y for now because we do not have a scrollToPositionWithOffset yet in our FixedGridLayout
        guideX.addOnItemTouchListener(itemTouchListener);

        guideY.setHasFixedSize(true);
        guideY.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // Do not allow scrolling with the X or Y for now because we do not have a scrollToPositionWithOffset yet in our FixedGridLayout
        guideY.addOnItemTouchListener(itemTouchListener);

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
            gridAdapter.withSelectable(false);
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


    public <T extends IGuideYItem> void setEmployeeItems(List<T> items, GridEmployeeItemAdapter.OnEmployeeItemClickListener onEmployeeItemClickListener) {
        if (guideYadapter == null) {
            guideYadapter = new FastItemAdapter();
            guideYadapter.setHasStableIds(true);
            guideYadapter.withSelectable(false);
            guideY.setAdapter(guideYadapter);
        }

        guideYadapter.clear();
        guideYadapter.set(items);
    }


    public Calendar getTimeLeft() {
        return left;
    }


    public Calendar getTimeRight() {
        return right;
    }


    public void center() {
        if (recyclerView != null && gridAdapter != null && gridAdapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(((gridAdapter.getItemCount() / columns) / 2));
        }
    }


    private static long calendarToMidnightMillis(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTimeInMillis();
    }
}