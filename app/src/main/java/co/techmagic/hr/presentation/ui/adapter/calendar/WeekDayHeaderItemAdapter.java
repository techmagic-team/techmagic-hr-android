package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.util.DateUtil;

public class WeekDayHeaderItemAdapter extends AbstractItem<WeekDayHeaderItemAdapter, WeekDayHeaderItemAdapter.ViewHolder> implements IWeekDayItem {

    private Calendar time;
    private String month;
    private String dayNumber;


    public WeekDayHeaderItemAdapter(Calendar time) {
        this.time = Calendar.getInstance();
        this.time.setTimeInMillis(time.getTimeInMillis());

        month = getMonthString();
        dayNumber = getDateString();
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.tvMonth.setText(month);
        holder.tvDay.setText(dayNumber);
        holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.item_guide_bg));
    }


    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.tvMonth.setText(null);
        holder.tvDay.setText(null);
    }


    @Override
    public String getMonthString() {
        return DateUtil.getMonthShortName(time);
    }


    @Override
    public String getDateString() {
        return Integer.toString(time.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public int getType() {
        return R.id.llItemX;
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
        return R.layout.item_weekday_header;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonth;
        TextView tvDay;

        public ViewHolder(View view) {
            super(view);
            tvMonth = (TextView) view.findViewById(R.id.tvMonth);
            tvDay = (TextView) view.findViewById(R.id.tvDay);
        }
    }
}