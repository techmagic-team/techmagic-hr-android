package co.techmagic.hr.domain.interactor;

import rx.Observable;
import rx.Subscriber;

public abstract class EmptyUseCase<RESPONSE_DATA, REPOSITORY> extends DataUseCase<Void, RESPONSE_DATA, REPOSITORY> {


    public EmptyUseCase(REPOSITORY repository) {
        super(repository);
    }


    @Override
    protected Observable<RESPONSE_DATA> buildObservable(Void aVoid) {
        return buildObservable();
    }


    protected abstract Observable<RESPONSE_DATA> buildObservable();


    public void execute(Subscriber<RESPONSE_DATA> useCaseSubscriber) {
        super.execute(null, useCaseSubscriber);
    }
}
