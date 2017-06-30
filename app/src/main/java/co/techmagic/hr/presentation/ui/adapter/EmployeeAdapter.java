package co.techmagic.hr.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.Lead;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPLOYEE_ITEM = 1;
    private static final int LOADING_ITEM = 2;

    private List<UserProfile> allDocs = new ArrayList<>();
    private OnEmployeeItemClickListener clickListener;
    private boolean isLoadingItemAdded = false;


    public EmployeeAdapter(OnEmployeeItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case EMPLOYEE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_employee, parent, false);
                return new EmployeeViewHolder(view, clickListener);

            case LOADING_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_employee_loading, parent, false);
                return new LoadingItemHolder(view);

            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case EMPLOYEE_ITEM:
                final UserProfile userProfile = allDocs.get(position);
                setupEmployeeItem((EmployeeViewHolder) holder, userProfile);
                break;

            case LOADING_ITEM:
                // Just shows loading for now.
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && isLoadingItemAdded) ? LOADING_ITEM : EMPLOYEE_ITEM;
    }


    @Override
    public int getItemCount() {
        return allDocs.size();
    }


    private void setupEmployeeItem(EmployeeViewHolder holder, final UserProfile userProfile) {
        Glide.with(holder.ivAvatar.getContext())
                .load(userProfile.getPhotoOrigin() == null ? userProfile.getPhoto() : userProfile.getPhotoOrigin())
                .placeholder(R.drawable.ic_user_placeholder)
                .dontAnimate()
                .into(holder.ivAvatar);

        setupEmployeeInfo(holder, userProfile);
    }


    private void setupEmployeeInfo(EmployeeViewHolder holder, UserProfile userProfile) {
        final Lead lead = userProfile.getLead();
        final Department department = userProfile.getDepartment();

        holder.tvName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());

        if (department == null) {
            holder.llPosition.setVisibility(View.GONE);
        } else {
            holder.llPosition.setVisibility(View.VISIBLE);
            holder.tvPosition.setText(department.getName());
        }

        if (lead == null) {
            holder.llLead.setVisibility(View.GONE);
        } else {
            holder.llLead.setVisibility(View.VISIBLE);
            holder.tvLead.setText(lead.getFirstName() + " " + lead.getLastName());
        }

        holder.item.setTag(userProfile);
    }


    private void add(UserProfile item) {
        allDocs.add(item);
        notifyItemInserted(getItemCount() - 1);
    }


    public void addLoadingProgress() {
        if (!isLoadingItemAdded) {
            isLoadingItemAdded = true;
            add(new UserProfile());
        }
    }


    public void removeLoadingProgress() {
        if (isLoadingItemAdded) {
            isLoadingItemAdded = false;

            int itemCount = getItemCount();
            if (itemCount > 0) {
                int position = itemCount - 1;
                UserProfile item = getItem(position);

                if (item != null) {
                    allDocs.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }


    private UserProfile getItem(int position) {
        if (position >= 0)
            return allDocs.get(position);
        else
            return null;
    }


    public void refresh(List<UserProfile> docs) {
        allDocs.addAll(docs);
        notifyDataSetChanged();
    }


    public void clear() {
        final int size = allDocs.size();
        allDocs.clear();
        notifyItemRangeRemoved(0, size);
    }


    private static class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View item;
        ImageView ivAvatar;
        LinearLayout llPosition;
        LinearLayout llLead;
        TextView tvName;
        TextView tvPosition;
        TextView tvLead;
        OnEmployeeItemClickListener clickListener;

        EmployeeViewHolder(View itemView, OnEmployeeItemClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            item = itemView.findViewById(R.id.cvItemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            llPosition = (LinearLayout) itemView.findViewById(R.id.llPosition);
            llLead = (LinearLayout) itemView.findViewById(R.id.llLead);
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

            clickListener.onEmployeeItemClicked((UserProfile) v.getTag());
        }
    }


    private static class LoadingItemHolder extends RecyclerView.ViewHolder {

        LoadingItemHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnEmployeeItemClickListener {

        void onEmployeeItemClicked(@NonNull UserProfile userProfile);
    }
}
