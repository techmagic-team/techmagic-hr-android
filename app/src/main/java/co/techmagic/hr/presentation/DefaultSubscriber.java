package co.techmagic.hr.presentation;

import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    // TODO: 3/24/17 uncomment this, when MVP will be implemented
//    private View view;

    public DefaultSubscriber(/*View view*/) {
        /*this.view = view;*/
    }

    @Override
    public void onCompleted() {
        /*if (view != null) {
            view.hideProgress();
        }*/
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
