package co.techmagic.hr.domain.interactor;

import co.techmagic.hr.domain.repository.IUtilRepository;
import rx.Observable;

/**
 * Created by roman on 7/9/17
 */

public class GetRealTime extends DataUseCase<Void, Long, IUtilRepository> {

    public GetRealTime(IUtilRepository iUtilRepository) {
        super(iUtilRepository);
    }

    @Override
    protected Observable<Long> buildObservable(Void aVoid) {
        return repository.getRealTime();
    }
}
