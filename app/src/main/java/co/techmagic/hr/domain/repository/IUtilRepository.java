package co.techmagic.hr.domain.repository;

import rx.Observable;

/**
 * Created by roman on 7/9/17
 */

public interface IUtilRepository {
    Observable<Long> getRealTime();
}
