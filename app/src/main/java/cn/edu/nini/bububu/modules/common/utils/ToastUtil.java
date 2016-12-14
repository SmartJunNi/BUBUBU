package cn.edu.nini.bububu.modules.common.utils;

import android.widget.Toast;

import cn.edu.nini.bububu.modules.base.BaseApplication;

/**
 * Created by nini on 2016/12/14.
 */

public class ToastUtil {

    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}