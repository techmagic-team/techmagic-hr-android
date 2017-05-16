package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.presentation.ui.view.calendar.GridItemRow;

public class GridEmployeeItemAdapter extends AbstractItem<GridEmployeeItemAdapter, GridEmployeeItemAdapter.ViewHolder> implements IGuideYItem {

    private final GridItemRow model;


    public GridEmployeeItemAdapter(GridItemRow model) {
        this.model = model;
    }


    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        final Context context = holder.itemView.getContext();

        Glide.with(context)
                .load(getPhotoUrl())
                .placeholder(R.drawable.ic_user_placeholder)
                .into(holder.ivPhotoItemY);

        withTag(this);
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
    public String getId() {
        return model.getId();
    }


    @Override
    public String getName() {
        return model.getPersonName() == null ? null : model.getPersonName();
    }

    @Override
    public String getPhotoUrl() {
        return model.getPhotoUrl() == null ? null : model.getPhotoUrl();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View root;
        ImageView ivPhotoItemY;
        TextView tvItemY;

        public ViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.llItemY);
            ivPhotoItemY = (ImageView) view.findViewById(R.id.ivPhotoItemY);
            tvItemY = (TextView) view.findViewById(R.id.tvItemY);
        }
    }


    public interface OnEmployeeItemClickListener {
        void onEmployeeItemClick(@NonNull String employeeId);
    }
}