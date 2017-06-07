package co.techmagic.hr.presentation.mvp.presenter;

import co.techmagic.hr.data.repository.EmployeeRepositoryImpl;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import co.techmagic.hr.presentation.mvp.view.impl.EditProfileViewImpl;


public class EditProfilePresenter extends BasePresenter<EditProfileViewImpl> {


    private IEmployeeRepository employeeRepository;


    public EditProfilePresenter() {
        super();
        employeeRepository = new EmployeeRepositoryImpl();
    }


    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }


    public void onUploadPhotoClick() {

    }


    public void onRoomClick() {

    }


    public void onReasonClick() {

    }


    public void showDatePickerDialog() {
        view.showDatePickerDialog();
    }


    public void onCancelClick() {

    }


    public void onSaveClick() {

    }
}