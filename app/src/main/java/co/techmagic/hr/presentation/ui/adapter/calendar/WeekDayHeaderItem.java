package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import co.techmagic.hr.R;

/**
 * Created by Wiebe Geertsma on 8-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */

public class WeekDayHeaderItem extends AbstractItem<WeekDayHeaderItem, WeekDayHeaderItem.ViewHolder> implements IWeekDayItem {

    private Calendar time;
    private String month;
    private String dayAndYear;


    public WeekDayHeaderItem(Calendar time) {
        this.time = Calendar.getInstance();
        this.time.setTimeInMillis(time.getTimeInMillis());

       // boolean isFirstDayOfMonth = time.get(Calendar.DAY_OF_MONTH) == 1;
       // boolean isMonday = this.time.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
       // String weekNumber = (isMonday ? "Wk." + Integer.toString(this.time.get(Calendar.WEEK_OF_YEAR)) : "") + "\n";

        month = getMonthString();
       // month += getDateString();
        dayAndYear = getDayString();
        dayAndYear += " " + getDateString() + "\n";
        dayAndYear += getYearString();
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.tvMonthAndDate.setText(month);
        holder.tvDay.setText(dayAndYear);
        holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), isToday() ? R.drawable.item_today_bg : R.drawable.item_guide_bg));
    }


    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.tvMonthAndDate.setText(null);
        holder.tvDay.setText(null);
    }


    @Override
    public String getMonthString() {
        return String.valueOf(DateFormat.format("MMM", time));
    }


    @Override
    public String getDateString() {
        return Integer.toString(time.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public String getYearString() {
        return Integer.toString(time.get(Calendar.YEAR));
    }


    @Override
    public String getDayString() {
        return time.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()).toUpperCase();
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


    private boolean isToday() {
        Calendar now = Calendar.getInstance();
        if (time.get(Calendar.YEAR) != now.get(Calendar.YEAR))
            return false;
        if (time.get(Calendar.MONTH) != now.get(Calendar.MONTH))
            return false;
        if (time.get(Calendar.DAY_OF_MONTH) != now.get(Calendar.DAY_OF_MONTH))
            return false;

        return true;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonthAndDate;
        TextView tvDay;

        public ViewHolder(View view) {
            super(view);
            tvMonthAndDate = (TextView) view.findViewById(R.id.tvMonthAndDate);
            tvDay = (TextView) view.findViewById(R.id.tvDay);
        }
    }
}