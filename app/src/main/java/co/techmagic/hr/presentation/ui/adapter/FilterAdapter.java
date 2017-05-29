package co.techmagic.hr.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.IFilterModel;

public class FilterAdapter<T extends IFilterModel> extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private OnFilterSelectionListener filterSelectionListener;

    private List<T> filters = new ArrayList<>();
    private boolean isCompanyFilter;


    public FilterAdapter(OnFilterSelectionListener filterSelectionListener, boolean isCompanyFilter) {
        this.filterSelectionListener = filterSelectionListener;
        this.isCompanyFilter = isCompanyFilter;
    }


    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isCompanyFilter ? R.layout.item_list_select_company_filter : R.layout.item_list_select_filter, parent, false);
        return new FilterViewHolder(view, filterSelectionListener);
    }


    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        final T filterItem = filters.get(position);
        holder.tvFilterName.setText(filterItem.getName());
        holder.rootView.setTag(filterItem);
    }


    @Override
    public int getItemCount() {
        return filters.size();
    }


    public void refresh(List<T> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
        notifyDataSetChanged();
    }


    static class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout rootView;
        TextView tvFilterName;
        OnFilterSelectionListener filterSelectionListener;


        public FilterViewHolder(View itemView, OnFilterSelectionListener filterSelectionListener) {
            super(itemView);
            rootView = (LinearLayout) itemView.findViewById(R.id.rootView);
            tvFilterName = (TextView) itemView.findViewById(R.id.tvFilterName);
            this.filterSelectionListener = filterSelectionListener;
            rootView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }
            final IFilterModel filter = (IFilterModel) v.getTag();
            filterSelectionListener.onFilterSelected(filter.getId(), filter.getName());
        }
    }

    public interface OnFilterSelectionListener {

        void onFilterSelected(@NonNull String id, @NonNull String name);
    }
}