package co.techmagic.hr.presentation.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import co.techmagic.hr.R;

public class GridCalendarAdapter extends RecyclerView.Adapter<GridCalendarAdapter.UserCalendarViewHolder> {

    private Random rnd = new Random();


    @Override
    public UserCalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_calendar, parent, false);
        return new UserCalendarViewHolder(view);
    }


    @Override
    public void onBindViewHolder(UserCalendarViewHolder holder, int position) {
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.vItem.setBackgroundColor(color);
    }


    @Override
    public int getItemCount() {
        return 2000;
    }


    static class UserCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View rootView;
        View vItem;

        public UserCalendarViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            vItem = itemView.findViewById(R.id.vItem);
        }


        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }
        }
    }
}