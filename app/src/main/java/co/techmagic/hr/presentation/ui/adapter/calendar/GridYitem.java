package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;

public class GridYitem extends AbstractItem<GridYitem, GridYitem.ViewHolder> implements IGuideYItem {

    private final String photoUrl;
    private final String name;


    public GridYitem(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        final Context context = holder.itemView.getContext();

        Glide.with(context)
                .load(getPhotoUrl())
                .placeholder(R.drawable.ic_user_placeholder)
                .into(holder.ivPhotoItemY);

        holder.tvItemY.setText(getName());
        holder.itemView.setBackgroundResource(R.drawable.item_bg);
    }


    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.tvItemY.setText(null);
    }


    @Override
    public int getType() {
        return R.id.flItemY;
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
        return R.layout.item_grid_employee;
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhotoItemY;
        TextView tvItemY;

        public ViewHolder(View view) {
            super(view);
            ivPhotoItemY = (ImageView) view.findViewById(R.id.ivPhotoItemY);
            tvItemY = (TextView) view.findViewById(R.id.tvItemY);
        }
    }
}