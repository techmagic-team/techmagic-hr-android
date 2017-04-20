package co.techmagic.hr.presentation.ui.view.timetable;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;

/**
 * Created by Wiebe Geertsma on 12-12-2016.
 * E-mail: e.w.geertsma@gmail.com
 */
public class GridYitem extends AbstractItem<GridYitem, GridYitem.ViewHolder> implements IGuideYItem {

    private final String name;


    public GridYitem(String name) {
        this.name = name;
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.tvItemY.setText(getName());
        holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.item_guide_bg));
    }


    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.tvItemY.setText(null);
    }


    @Override
    public int getType() {
        return R.id.llItemY;
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


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemY;

        public ViewHolder(View view) {
            super(view);
            this.tvItemY = (TextView) view.findViewById(R.id.tvItemY);
        }
    }
}