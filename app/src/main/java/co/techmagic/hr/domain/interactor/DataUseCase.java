package co.techmagic.hr.domain.interactor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class DataUseCase<REQUEST_DATA, RESPONSE_DATA, REPOSITORY> {

    protected REPOSITORY repository;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();


    public DataUseCase(REPOSITORY repository) {
        this.repository = repository;
    }


    protected abstract Observable<RESPONSE_DATA> buildObservable(REQUEST_DATA requestData);


    public void execute(REQUEST_DATA requestData, Subscriber<RESPONSE_DATA> useCaseSubscriber) {
        this.compositeSubscription.add(this.buildObservable(requestData).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(useCaseSubscriber));
    }


    public boolean isUnsubscribed() {
        return !compositeSubscription.hasSubscriptions();
    }


    public void unsubscribe() {
        if (!isUnsubscribed()) {
            compositeSubscription.clear();
        }
    }
}
