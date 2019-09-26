package co.techmagic.hr.data.repository.base

import co.techmagic.hr.data.exception.NetworkConnectionException
import co.techmagic.hr.data.manager.NetworkManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

abstract class BaseNetworkRepository(private val networkManager: NetworkManager) : BaseRepository() {
    protected fun <T> setup(observable: Observable<T>): Observable<T> {
        return if (networkManager.isNetworkAvailable) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            Observable.error(NetworkConnectionException())
        }
    }
}