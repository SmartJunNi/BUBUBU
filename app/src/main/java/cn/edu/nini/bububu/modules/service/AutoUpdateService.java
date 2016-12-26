package cn.edu.nini.bububu.modules.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import org.mym.plog.PLog;

import java.util.concurrent.TimeUnit;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.component.RetrofitSingleton;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import cn.edu.nini.bububu.modules.main.ui.MainActivity;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by nini on 2016/12/24.
 */

public class AutoUpdateService extends Service {

    public static final int AUTO_UPDATA_SERVICE = 0;
    private CompositeSubscription mCompositeSubscription;
    private Subscription mSubscription;
    private SharedPreferenceUtil mSharedPreferenceUtil;
    private boolean mIsUnscribed;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mCompositeSubscription=new CompositeSubscription();
        mSharedPreferenceUtil=SharedPreferenceUtil.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        unSubscribed();
        if (mIsUnscribed) {
            if(mSharedPreferenceUtil.getAutoUpdate()!=0){
                PLog.i(TAG, SystemClock.elapsedRealtime() + " 当前设置" + mSharedPreferenceUtil.getAutoUpdate());
                mSubscription = Observable.interval(mSharedPreferenceUtil.getAutoUpdate(), TimeUnit.HOURS)
                        .subscribe(aLong -> {
                            mIsUnscribed = false;
                            PLog.i(TAG, SystemClock.elapsedRealtime() + " 当前设置" + mSharedPreferenceUtil.getAutoUpdate());
                            fetchDataByNetwork();
                        });
                mCompositeSubscription.add(mSubscription);
            }
        }
        return super.onStartCommand(intent,flags,startId);
    }

    private void fetchDataByNetwork() {
        String cityName=mSharedPreferenceUtil.getCityName();
        if (cityName != null) {
            Utils.replaceCity(cityName);
        }
        RetrofitSingleton.getInstance().fetchWeather(cityName)
                .subscribe(weather->{
                    nomalStyleNotification(weather);
                });
    }

    private void nomalStyleNotification(Weather weather) {
        Intent intent=new Intent(AutoUpdateService.this, MainActivity.class);

        PendingIntent pi=PendingIntent.getActivity(AutoUpdateService.this,
                AUTO_UPDATA_SERVICE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder=new Notification.Builder(AutoUpdateService.this);
        Notification notification = builder.setContentIntent(pi)
                .setContentTitle(weather.getBasic().getCity())
                .setContentText(weather.getNow().getCond().getTxt() + "  当前温度：" + weather.getNow().getTmp() + "℃")
                .setSmallIcon(mSharedPreferenceUtil.getInt(weather.getNow().getCond().getTxt(), R.mipmap.none))
                .build();
        notification.flags=mSharedPreferenceUtil.getNotificationModel();
        //有了通知以后 要有管理者
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    private void unSubscribed(){
        mIsUnscribed=true;
        mCompositeSubscription.remove(mSubscription);
    }
}
