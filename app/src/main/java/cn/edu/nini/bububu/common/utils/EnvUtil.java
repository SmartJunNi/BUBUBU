package cn.edu.nini.bububu.common.utils;

import android.content.Context;
import android.util.TypedValue;

import cn.edu.nini.bububu.base.BaseApplication;

/**
 * Created by nini on 2016/12/20.
 */

public class EnvUtil {
    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return DensityUtil.dp2px(44);

    }

    public static int getStatusBarHeight() {
        if (sStatusBarHeight == 0) {
            //statusBarHeight = getResources().getDimensionPixelSize(com.android.internal.R.dimen.status_bar_height);
            int resourceId =
                    BaseApplication.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = BaseApplication.getAppContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }
}
