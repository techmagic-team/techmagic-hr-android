package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;

public class GridCellItemAdapter extends AbstractItem<GridCellItemAdapter, GridCellItemAdapter.ViewHolder> {

    private boolean isStart, isWeekend;
    private boolean hasHolidays;
    private boolean hasVacation;
    private boolean hasDayOff;
    private boolean hasIllness;
    private boolean hasRequested;


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.itemView.setBackgroundResource(R.drawable.item_bg);

        if (isWeekend) {
            holder.itemView.setBackgroundResource(R.drawable.item_weekend_bg);
            return;
        }

        if (hasHolidays) {
            holder.itemView.setBackgroundResource(R.drawable.item_holiday_bg);
            return;
        }

        if (hasRequested) {
            holder.itemView.setBackgroundResource(R.drawable.item_requested_bg);
            return;
        }

        if (hasVacation) {
            holder.itemView.setBackgroundResource(R.drawable.item_vacation_bg);
            return;
        }

        if (hasDayOff) {
            holder.itemView.setBackgroundResource(R.drawable.item_day_off_bg);
            return;
        }

        if (hasIllness) {
            holder.itemView.setBackgroundResource(R.drawable.item_illness_bg);
            return;
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


    public boolean isWeekend() {
        return isWeekend;
    }


    public void setIsWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public boolean hasHolidays() {
        return hasHolidays;
    }


    public boolean hasVacation() {
        return hasVacation;
    }


    public void setHasVacation(boolean hasVacation) {
        this.hasVacation = hasVacation;
    }


    public boolean hasDayOff() {
        return hasDayOff;
    }


    public void setHasDayOff(boolean hasDayOff) {
        this.hasDayOff = hasDayOff;
    }


    public boolean hasIllness() {
        return hasIllness;
    }


    public void setHasIllness(boolean hasIllness) {
        this.hasIllness = hasIllness;
    }


    public boolean isAccepted() {
        return hasRequested;
    }


    public void setRequested(boolean hasRequested) {
        this.hasRequested = hasRequested;
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