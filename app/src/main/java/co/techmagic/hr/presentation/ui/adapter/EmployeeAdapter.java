package co.techmagic.hr.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.techmagic.hr.R;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>{


    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        EmployeeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
