package co.techmagic.hr.presentation.mvp.presenter;

import android.support.annotation.NonNull;

import co.techmagic.hr.presentation.mvp.view.View;

@Deprecated
public abstract class BasePresenter<VIEW extends View> {

    protected VIEW view;

    public BasePresenter() {
    }

    public void attachView(@NonNull VIEW view) {
        this.view = view;
        onViewAttached();
    }

    public void detachView() {
        onViewDetached();
    }

    public void resume() {
    }

    public void pause() {
    }

    protected void onViewAttached() {
    }

    protected void onViewDetached() {
    }
}
