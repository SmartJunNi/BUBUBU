package cn.edu.nini.bububu.common.utils;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * 目的是用Rx来管理drawer
 * Created by nini on 2016/12/18.
 */

public class RxDrawer {
    /**
     *  阈值  临界值
     */
    public static final float OFFSET_THRESGOLD=0.03f;
    /**
     *     //这里的意思是当等动画即将完成后再执行onNext操作
     * @param drawer
     * @return
     */
    public static Observable close(DrawerLayout drawer){
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                drawer.closeDrawer(GravityCompat.START);
                DrawerLayout.DrawerListener listener=new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset < OFFSET_THRESGOLD) {
                            subscriber.onNext(1);
                            subscriber.onCompleted();
                        }
                    }
                };
                drawer.addDrawerListener(listener);
                subscriber.add(new MainThreadSubscription() {
                    @Override
                    protected void onUnsubscribe() {
                        drawer.removeDrawerListener(listener);
                    }
                });
            }
        });
    }

}
