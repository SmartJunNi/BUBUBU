package cn.edu.nini.bububu.component;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by nini on 2016/12/17.
 */

public class RxBus {
    private final Subject<Object, Object> mBus;

    public RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        return singletonLoader.defaultInstance;
    }

    private static class singletonLoader {
        private static final RxBus defaultInstance = new RxBus();
    }

    public void post(Object o) {
        if(hasObserve()) { //严谨一点
            mBus.onNext(o);
        }
    }

    private boolean hasObserve() {
        return mBus != null &&mBus.hasObservers();
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}
