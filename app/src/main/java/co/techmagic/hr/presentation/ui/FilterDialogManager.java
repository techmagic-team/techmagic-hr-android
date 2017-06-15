package co.techmagic.hr.presentation.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import co.techmagic.hr.R;
import co.techmagic.hr.data.entity.IFilterModel;
import co.techmagic.hr.presentation.ui.adapter.FilterAdapter;


public class FilterDialogManager {

    private Context context;
    private AlertDialog dialog;
    private FilterAdapter adapter;
    private FilterAdapter.OnFilterSelectionListener onFilterSelectionListener;
    private FilterTypes filterType = FilterTypes.NONE;


    public FilterDialogManager(@NonNull Context context, @NonNull FilterAdapter.OnFilterSelectionListener onFilterSelectionListener) {
        this.context = context;
        this.onFilterSelectionListener = onFilterSelectionListener;
        adapter = new FilterAdapter(onFilterSelectionListener, false);
    }


    public <T extends IFilterModel> void showSelectFilterAlertDialog(@Nullable List<T> filters, FilterTypes filterType) {
        this.filterType = filterType;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        setupDialogViews(filters, builder);
        dialog = builder.show();
        dialog.findViewById(R.id.btnAlertDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private <T extends IFilterModel> void setupDialogViews(@Nullable List<T> filters, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_select_filter, null);
        builder.setView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        setupDialogTitle(tvTitle);

        setupSelectFilterRecyclerView(view, filters);
    }


    private void setupDialogTitle(@NonNull TextView tvTitle) {
        switch (filterType) {
            case COMPANY:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_company));
                break;

            case DEPARTMENT:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_by_department));
                break;

            case LEAD:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_by_lead));
                break;

            case PROJECT:
                tvTitle.setText(context.getString(R.string.tm_hr_calendar_filters_activity_filter_by_project));
                break;

            case ROOM:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_by_room));
                break;

            case REASON:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_by_reason));
                break;

            case DATE_OF_BIRTH:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_date_of_birth));
                break;

            case FIRST_WORKING_DAY:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_first_working_day));
                break;

            case FIRST_WORKING_DAY_IN_IT:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_first_day_in_it));
                break;

            case TRIAL_PERIOD_ENDS:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_trial_period));
                break;

            case LAST_WORKING_DAY:
                tvTitle.setText(context.getString(R.string.tm_hr_search_activity_text_filter_last_working));
                break;
        }
    }


    private <T extends IFilterModel> void setupSelectFilterRecyclerView(View view, @Nullable List<T> results) {
        RecyclerView rvFilters = (RecyclerView) view.findViewById(R.id.rvFilters);
        rvFilters.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        if (filterType == FilterTypes.COMPANY) {
            adapter = new FilterAdapter(onFilterSelectionListener, true);
        } else {
            adapter = new FilterAdapter(onFilterSelectionListener, false);
        }

        rvFilters.setAdapter(adapter);
        adapter.refresh(results);
    }


    public void dismissDialogIfOpened() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public boolean isDialogActive() {
        return dialog != null && dialog.isShowing();
    }
}