package co.techmagic.hr.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.Lead;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Docs> allDocs = new ArrayList<>();
    private OnEmployeeItemClickListener clickListener;


    public EmployeeAdapter(OnEmployeeItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_employee, parent, false);
        return new EmployeeViewHolder(view, clickListener);
    }


    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        final Docs docs = allDocs.get(position);
        setupEmployeeItem(holder, docs);
    }


    @Override
    public int getItemCount() {
        return allDocs.size();
    }


    private void setupEmployeeItem(EmployeeViewHolder holder, final Docs docs) {
        Glide.with(holder.ivAvatar.getContext())
                .load(docs.getPhoto())
                .placeholder(R.drawable.ic_techmagic_logo)
                .dontAnimate()
                .into(holder.ivAvatar);

        setupEmployeeInfo(holder, docs);
    }


    private void setupEmployeeInfo(EmployeeViewHolder holder, Docs docs) {
        final Lead lead = docs.getLead();
        final Department department = docs.getDepartment();

        holder.tvName.setText(docs.getFirstName() + " " + docs.getLastName());

        if (department == null) {
            holder.tvPosition.setVisibility(View.GONE);
        } else {
            holder.tvPosition.setVisibility(View.VISIBLE);
            holder.tvPosition.setText(department.getName());
        }

        if (lead == null) {
            holder.tvLead.setVisibility(View.GONE);
        } else {
            holder.tvLead.setVisibility(View.VISIBLE);
            holder.tvLead.setText(lead.getFirstName() + " " + lead.getLastName());
        }

        holder.item.setTag(docs);
    }


    public void refresh(List<Docs> docs) {
        allDocs.clear();
        allDocs.addAll(docs);
        notifyDataSetChanged();
    }


    static class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View item;
        ImageView ivAvatar;
        TextView tvName;
        TextView tvPosition;
        TextView tvLead;
        OnEmployeeItemClickListener clickListener;

        EmployeeViewHolder(View itemView, OnEmployeeItemClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            item = itemView.findViewById(R.id.cvItemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
            tvLead = (TextView) itemView.findViewById(R.id.tvLead);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }

            clickListener.onEmployeeItemClicked((Docs) v.getTag());
        }
    }

    public interface OnEmployeeItemClickListener {

        void onEmployeeItemClicked(@NonNull Docs docs);
    }
}
