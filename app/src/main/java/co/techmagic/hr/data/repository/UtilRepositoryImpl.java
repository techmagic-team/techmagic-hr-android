package co.techmagic.hr.data.repository;

import java.io.IOException;

import co.techmagic.hr.domain.repository.IUtilRepository;
import io.github.eterverda.sntp.SNTP;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by roman on 7/9/17
 */

public class UtilRepositoryImpl implements IUtilRepository {

    @Override
    public Observable<Long> getRealTime() {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                try {
                    Long realTime = SNTP.currentTimeMillis();
                    subscriber.onNext(realTime);
                    subscriber.onCompleted();

                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
