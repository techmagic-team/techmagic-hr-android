package co.techmagic.hr.presentation.mvp.view;

import java.util.List;

import co.techmagic.hr.data.entity.Docs;

public interface HomeView extends View {

    void showEmployeesList(List<Docs> docs);
}
