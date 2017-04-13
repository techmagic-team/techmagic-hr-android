package co.techmagic.hr.data.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.exception.NetworkConnectionException;
import co.techmagic.hr.data.manager.NetworkManager;
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

/**
 * Created by techmagic on 4/3/17.
 */

public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private ApiClient client;
    private NetworkManager networkManager;


    public EmployeeRepositoryImpl() {
        client = ApiClient.getApiClient();
        this.networkManager = NetworkManagerImpl.getNetworkManager();
    }


    @Override
    public Observable<Employee> getEmployees(EmployeeFiltersRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getEmployees(request.getQuery(), request.getDepartmentId(), request.isLastWorkingDay(), request.getLeadId(), request.getOffset(), request.getLimit());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterDepartment>> getFilterDepartments() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterDepartments();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeads() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterLeads();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<RequestedTimeOff[]> getAllVacations(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllVacations(request);
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<RequestedTimeOff[]> getAllIllnesses(GetIllnessRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllIllnesses(request);
        }

        return Observable.error(new NetworkConnectionException());
    }
}