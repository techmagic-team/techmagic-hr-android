package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;

public class GridXitem extends AbstractItem<GridXitem, GridXitem.ViewHolder> {

    private final IGuideXItem model;
    private final int row;
    private final int column;
    private boolean isStart, isToday, isWeekend;
    private boolean hasHolidays;
    private boolean vacation;
    private boolean dayOff;
    private boolean illness;
    private boolean requested;


    public GridXitem(int row, int column) {
        // Make a blank item
        model = null;
        this.row = row;
        this.column = column;
    }


    public <T extends IGuideXItem> GridXitem(T model, int row, int column) {
        this.model = model;
        this.row = row;
        this.column = column;
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        if (hasHolidays()) {
            holder.itemView.setBackgroundResource(R.drawable.item_holiday_bg);
        } else if (hasVacation()) {
            holder.itemView.setBackgroundResource(R.drawable.item_vacation_bg);
        } else if (hasDayOff()) {
            holder.itemView.setBackgroundResource(R.drawable.item_day_off_bg);
        } else if (hasIllness()) {
            holder.itemView.setBackgroundResource(R.drawable.item_illness_bg);
        } else if (hasRequested()) {
            holder.itemView.setBackgroundResource(R.drawable.item_requested_bg);
        } else if (isWeekend()) {
            holder.itemView.setBackgroundResource(R.drawable.item_weekend_bg);
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.item_bg);
        }
    }


    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }


    @Override
    public int getType() {
        return R.id.tvItemGrid;
    }


    @Override
    public long getIdentifier() {
        return System.identityHashCode(this);
    }


    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.item_grid;
    }


    public boolean isStart() {
        return isStart;
    }


    public void setStart(boolean start) {
        isStart = start;
    }


    public boolean isToday() {
        return isToday;
    }


    public void setIsToday(boolean today) {
        isToday = today;
    }


    public boolean isWeekend() {
        return isWeekend;
    }


    public void setIsWeekend(boolean weekend) {
        isWeekend = weekend;
    }


    public IGuideXItem getModel() {
        return model;
    }


    public boolean isEmpty() {
        return model == null;
    }


    public boolean hasHolidays() {
        return hasHolidays;
    }


    public boolean hasVacation() {
        return vacation;
    }


    public void setVacation(boolean vacation) {
        this.vacation = vacation;
    }


    public boolean hasDayOff() {
        return dayOff;
    }


    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }


    public boolean hasIllness() {
        return illness;
    }


    public void setIllness(boolean illness) {
        this.illness = illness;
    }


    public boolean hasRequested() {
        return requested;
    }


    public void setRequested(boolean requested) {
        this.requested = requested;
    }


    public void setHasHolidays(boolean hasHolidays) {
        this.hasHolidays = hasHolidays;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemGrid;

        public ViewHolder(View view) {
            super(view);
            this.tvItemGrid = (TextView) view.findViewById(R.id.tvItemGrid);
        }
    }
}