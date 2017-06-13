package cn.edu.nini.bububu.base;

/**
 * Created by nini on 2016/12/11.
 */

public class C {
    public static final String target = "https://api.heweather.com/x3/weather?" +
            "cityid=CN101010100&key=5f6b4bebfc98499db06709192bcd7283";

    public static final String KEY="5f6b4bebfc98499db06709192bcd7283";
    /**
     * 和风天气
     */
    public static final String baseUrl = "https://api.heweather.com/x3/";

    /**
     * 数据库名字
     */
    public static final String ORM_NAME = "cities.db";

    /**
     * 是否多选模式
     */
    public static final String MULTI_CHECK = "multi_check";
    /**
     * 城市名字不正确返回的结果
     */
    public static final String UNKNOW_CITY = "unknown city";
}
