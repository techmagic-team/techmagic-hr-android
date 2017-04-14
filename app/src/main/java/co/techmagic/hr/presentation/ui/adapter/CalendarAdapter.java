package co.techmagic.hr.presentation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Random;

import co.techmagic.hr.R;


public class CalendarAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Random rnd = new Random();


    public CalendarAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return 31 * 3;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_calendar, null);
            View vItem = convertView.findViewById(R.id.vItem);
            TextView tvNumber = (TextView) convertView.findViewById(R.id.tvText);

            final ViewHolder viewHolder = new ViewHolder(vItem, tvNumber);
            convertView.setTag(viewHolder);
        }

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.vItem.setBackgroundColor(color);
        viewHolder.tvNumber.setText(String.valueOf(position));

        return convertView;
    }

    static class ViewHolder {
        View vItem;
        TextView tvNumber;

        ViewHolder(View vItem, TextView tvNumber) {
            this.vItem = vItem;
            this.tvNumber = tvNumber;
        }
    }
}