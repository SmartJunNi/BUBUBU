package cn.edu.nini.bububu.base;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nini on 2016/12/13.
 */

public class Utils {
    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 安全的 String 返回
     *  主要判断是否为空和为null
     * @param prefix 默认字段
     * @param obj 拼接字段 (需检查)
     */
    public static String safeText(String prefix, String obj) {
        if (TextUtils.isEmpty(obj)) return "";
        return TextUtils.concat(prefix, obj).toString();
    }

    public static String safeText(String msg) {
        if (null == msg) {
            return "";
        }
        return safeText("", msg);
    }

    /**
     * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
     * @param closeable  所有可以close的对象。
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try{
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String replaceCity(String city) {
        city=safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
        return city;
    }


    public static String replaceInfo(String city) {
        city=safeText(city).replace("API没有","");
        return city;
    }
}
