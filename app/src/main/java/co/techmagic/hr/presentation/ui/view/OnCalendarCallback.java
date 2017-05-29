package co.techmagic.hr.presentation.ui.view;


import java.util.List;

import co.techmagic.hr.presentation.ui.adapter.calendar.GridCellItemAdapter;
import co.techmagic.hr.presentation.ui.adapter.calendar.GridEmployeeItemAdapter;

public interface OnCalendarCallback {

    void onCalendarViewReady(List<GridCellItemAdapter> allGridItems, List<GridEmployeeItemAdapter> employeeItems);
}
