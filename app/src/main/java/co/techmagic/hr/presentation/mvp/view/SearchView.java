package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.List;

import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;

public interface SearchView extends View {

    void showFilterByDepartmentDialog(@NonNull List<FilterDepartment> departments);

    void showSelectedDepartmentFilter(@NonNull String id, @NonNull String filterName);

    void showEmptyDepartmentFiltersErrorMessage(int resId);

    void showFilterByLeadDialog(@NonNull List<FilterLead> leads);

    void showSelectedLeadFilter(@NonNull String id, @NonNull String filterName);

    void showEmptyLeadFiltersErrorMessage(int resId);

    /**
     * Method should be called only in last response from the server
     * to avoid overlapping of animation by keyboard.
     */

    void requestSearchViewFocus();
}
