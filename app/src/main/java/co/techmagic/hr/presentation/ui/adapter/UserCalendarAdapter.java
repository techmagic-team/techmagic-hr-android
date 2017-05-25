package co.techmagic.hr.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import co.techmagic.hr.R;

public class UserCalendarAdapter extends RecyclerView.Adapter<UserCalendarAdapter.UserCalendarViewHolder> {


    @Override
    public UserCalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_calendar_user, parent, false);
        return new UserCalendarViewHolder(view);
    }


    @Override
    public void onBindViewHolder(UserCalendarViewHolder holder, int position) {
        Glide.with(holder.rootView.getContext())
                .load("")
                .placeholder(R.drawable.ic_user_circle_black)
                .into(holder.ivUserCalendarPhoto);
    }


    @Override
    public int getItemCount() {
        return 64;
    }


    static class UserCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View rootView;
        ImageView ivUserCalendarPhoto;

        public UserCalendarViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivUserCalendarPhoto = (ImageView) itemView.findViewById(R.id.ivUserCalendarPhoto);
        }


        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }
        }
    }
}