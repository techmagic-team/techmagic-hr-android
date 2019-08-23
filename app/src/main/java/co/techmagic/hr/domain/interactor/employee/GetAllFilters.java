package co.techmagic.hr.domain.interactor.employee;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.pojo.EditProfileFiltersDto;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;
import rx.functions.Func1;


public class GetAllFilters extends EmptyUseCase<EditProfileFiltersDto, IEmployeeRepository> {


    public GetAllFilters(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<EditProfileFiltersDto> buildObservable() {
        EditProfileFiltersDto allFilters = new EditProfileFiltersDto();

//        Observable<EditProfileFiltersDto> departments = repository.getFilterDepartments()
//                .flatMap(new Func1<List<Filter>, Observable<EditProfileFiltersDto>>() {
//                    @Override
//                    public Observable<EditProfileFiltersDto> call(List<Filter> filters) {
//                        if (filters != null && !filters.isEmpty()) {
//                            allFilters.setDepartments(filters);
//                        }
//
//                        return Observable.just(allFilters);
//                    }
//                });

        Observable<EditProfileFiltersDto> rooms = repository.getRooms()
                .flatMap(new Func1<List<Filter>, Observable<EditProfileFiltersDto>>() {
                    @Override
                    public Observable<EditProfileFiltersDto> call(List<Filter> filters) {
                        if (filters != null && !filters.isEmpty()) {
                            allFilters.setRooms(filters);
                        }

                        return Observable.just(allFilters);
                    }
                });

        Observable<EditProfileFiltersDto> leads = repository.getFilterLeadsWithEmployees()
                .flatMap(new Func1<List<FilterLead>, Observable<EditProfileFiltersDto>>() {
                    @Override
                    public Observable<EditProfileFiltersDto> call(List<FilterLead> filters) {
                        if (filters != null && !filters.isEmpty()) {
                            allFilters.setLeads(filters);
                        }

                        return Observable.just(allFilters);
                    }
                });

        Observable<EditProfileFiltersDto> reasons = repository.getReasons()
                .flatMap(new Func1<List<Filter>, Observable<EditProfileFiltersDto>>() {
                    @Override
                    public Observable<EditProfileFiltersDto> call(List<Filter> filters) {
                        if (filters != null && !filters.isEmpty()) {
                            allFilters.setReasons(filters);
                        }

                        return Observable.just(allFilters);
                    }
                });

        List<Observable<EditProfileFiltersDto>> observables = new ArrayList<>();

//        observables.add(departments);
        observables.add(rooms);
        observables.add(leads);
        observables.add(reasons);

        return Observable.zip(observables, args -> (EditProfileFiltersDto) args[0]);
    }
}