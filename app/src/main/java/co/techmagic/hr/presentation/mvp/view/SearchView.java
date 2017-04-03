package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.List;

import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;

public interface SearchView extends View {

    void showFilterByDepartmentDialog(@NonNull List<FilterDepartment> departments);

    void showEmptyDepartmentFilters(int resId);

    void showFilterByLeadDialog(@NonNull List<FilterLead> leads);

    void showEmptyLeadFilters(int resId);
}
