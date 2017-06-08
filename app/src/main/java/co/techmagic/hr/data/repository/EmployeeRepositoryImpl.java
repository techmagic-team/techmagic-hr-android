package co.techmagic.hr.data.repository;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.exception.NetworkConnectionException;
import co.techmagic.hr.data.manager.NetworkManager;
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
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
            return client.getEmployeeClient().getEmployees(request.getQuery(), request.getProjectId(), request.getDepartmentId(), request.isLastWorkingDay(), request.getLeadId(), request.getOffset(), request.getLimit());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getFilterDepartments() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterDepartments();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeadsWithEmployees() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterLeadsWithEmployees();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getProjectFilters() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterProjects();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserVacations(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserVacations(request.getUserId(), true, request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserDayOffs(TimeOffRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserDayOffs(request.getUserId(), false, request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getUserIllnesses(GetIllnessRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getUserIllnesses(request.getUserId(), request.getDateFrom().getGte(), request.getDateTo().getLte());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllVacations(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllVacations(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllDayOffs(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllDayOffs(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<RequestedTimeOff>> getAllIllnesses(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllIllnesses(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<Employee> getAllEmployeesByDepartment(EmployeesByDepartmentRequest employeesByDepartmentRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getAllEmployeesByDepartment(employeesByDepartmentRequest.getProjectId(), employeesByDepartmentRequest.getDepartmentId(), employeesByDepartmentRequest.isMyTeam());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<CalendarInfo>> getCalendar(TimeOffAllRequest request) {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getCalendar(request.getDateFrom(), request.getDateTo());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getRooms() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterRooms();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeads() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getLeads();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Filter>> getReasons() {
        if (networkManager.isNetworkAvailable()) {
            return client.getEmployeeClient().getFilterReasons();
        }

        return Observable.error(new NetworkConnectionException());
    }
}