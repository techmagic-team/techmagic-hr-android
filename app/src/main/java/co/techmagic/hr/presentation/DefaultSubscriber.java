package co.techmagic.hr.presentation;

import java.net.SocketTimeoutException;

import co.techmagic.hr.presentation.mvp.view.View;
import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    private View view;

    public DefaultSubscriber(View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
        if (view != null) {
            view.hideProgress();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            if (view != null) {
                view.showConnectionErrorMessage();
            }
        }
    }

    @Override
    public void onNext(T t) {

    }
}
