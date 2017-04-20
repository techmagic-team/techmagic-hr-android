package co.techmagic.hr.presentation.ui.view.timetable;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;

/**
 * Created by Wiebe Geertsma on 8-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class GridXitem extends AbstractItem<GridXitem, GridXitem.ViewHolder> {

    private final IGridItem model;
    private final int row;
    private final int column;
    private boolean isStart, isToday, isWeekend;


    public GridXitem(int row, int column) {
        // Make a blank item
        model = null;
        this.row = row;
        this.column = column;
    }


    public <T extends IGridItem> GridXitem(T model, int row, int column) {
        this.model = model;
        this.row = row;
        this.column = column;
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.itemView.setBackgroundResource(isToday ? R.drawable.item_today_bg : isWeekend ? R.drawable.item_weekend_bg : R.drawable.item_bg);
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


    public boolean getIsWeekend() {
        return isWeekend;
    }


    public void setIsWeekend(boolean weekend) {
        isWeekend = weekend;
    }


    public IGridItem getModel() {
        return model;
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    public boolean isEmpty() {
        return model == null;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemGrid;

        public ViewHolder(View view) {
            super(view);
            this.tvItemGrid = (TextView) view.findViewById(R.id.tvItemGrid);
        }
    }
}