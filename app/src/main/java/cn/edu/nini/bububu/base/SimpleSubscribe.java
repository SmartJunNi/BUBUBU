package cn.edu.nini.bububu.base;

import org.mym.plog.PLog;

import rx.Subscriber;

/**
 * Created by nini on 2016/12/17.
 */

public abstract class SimpleSubscribe<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        PLog.e(e.toString());
    }

}
