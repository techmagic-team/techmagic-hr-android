package co.techmagic.hr.presentation;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import co.techmagic.hr.data.exception.NetworkConnectionException;
import co.techmagic.hr.presentation.mvp.view.View;
import retrofit2.HttpException;
import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    private View view;

    /* Used to handle progress manually */

    public DefaultSubscriber() {

    }

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
        if (view == null) {
            return;
        }
        if (e instanceof SocketTimeoutException || e instanceof NetworkConnectionException) {
            view.showConnectionErrorMessage();
        } else if (e instanceof HttpException) {
            handleHttpErrorCodes(((HttpException) e).code());
        }
    }

    @Override
    public void onNext(T requestedTimeOffDto) {

    }

    private void handleHttpErrorCodes(int errorCode) {
        switch (errorCode) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                view.showSnackBarWrongLoginCredentialsError();
                break;

            case HttpURLConnection.HTTP_FORBIDDEN:
                view.logOut();
                break;
        }
    }
}
