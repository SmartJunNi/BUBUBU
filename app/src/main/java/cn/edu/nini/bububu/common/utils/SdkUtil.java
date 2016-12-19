package cn.edu.nini.bububu.common.utils;

import android.os.Build;

/**
 * Created by nini on 2016/12/19.
 */

public class SdkUtil {

    public int sj;

    /**
     * 大于等于
     *
     * @param version
     * @return
     */
    public static boolean sdkVersionGe(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    /**
     * 等于
     *
     * @param version
     * @return
     */
    public static boolean sdkVersionEq(int version) {
        return Build.VERSION.SDK_INT == version;
    }

    /**
     * 小于
     *
     * @param version
     * @return
     */
    public static boolean sdkVersionLt(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    /**
     * 大于19吗
     *
     * @return
     */
    public static boolean sdkVersionGe19() {
        return sdkVersionGe(19);
    }

    /**
     * 大于21吗
     *
     * @return
     */
    public static boolean sdkVersionGe21() {
        return sdkVersionGe(21);
    }
}
