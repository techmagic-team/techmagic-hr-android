package co.techmagic.hr.domain.interactor.user;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

/**
 * Created by techmagic on 5/29/17.
 */

public class GetCompanies extends EmptyUseCase<List<Company>, IUserRepository> {


    public GetCompanies(IUserRepository iUserRepository) {
        super(iUserRepository);
    }


    @Override
    protected Observable<List<Company>> buildObservable() {
        return repository.getCompanies();
    }
}
