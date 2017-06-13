package cn.edu.nini.bububu.base;

import android.app.Application;
import android.content.Context;

import org.mym.plog.PLog;
import org.mym.plog.config.PLogConfig;

import cn.edu.nini.bububu.BuildConfig;
import cn.edu.nini.bububu.common.utils.EasyController;

public class BaseApplication extends Application {

    private static String sCacheDir;
    public static Context sAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();

        initPlog();
/*        CrashHandler.init(new CrashHandler(getApplicationContext()));
        if (!BuildConfig.DEBUG) {
            FIR.init(this);
        }
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        LeakCanary.install(this);*/
        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private void initPlog() {
        PLog.init(PLogConfig.newBuilder()
                .forceConcatGlobalTag(true)
                .useAutoTag(true)
                //保持内部类信息
                .keepInnerClass(true)
                //保持行号信息，点击可以跳转源文件
                .keepLineNumber(true)
                //单行180，超过自动换行
                .maxLengthPerLine(140)
                //release版本自动关闭日志
                .controller(new EasyController(BuildConfig.DEBUG, BuildConfig.DEBUG))//在这里可以控制是否打印log
                .build());
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }
}
