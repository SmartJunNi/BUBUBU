package cn.edu.nini.bububu.modules.city.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.modules.city.domain.City;
import cn.edu.nini.bububu.modules.city.domain.Province;

/**
 * Created by nini on 2016/12/17.
 */

public class WeatherDB {
    public WeatherDB() {
    }

    /**
     * 查询并返回所有省份
     * @param db
     * @return
     */
    public static  List<Province> loadProvinces(SQLiteDatabase db) {
        List<Province> list=new ArrayList<>();
        Cursor cursor=db.query("T_Province",null,null,null,null,null,null);  //查询所有
        while (cursor.moveToNext()) {
            Province province=new Province();
            province.ProName= cursor.getString(cursor.getColumnIndex("ProName"));
            province.ProSort=cursor.getInt(cursor.getColumnIndex("ProSort"));
            list.add(province);
        }
        Utils.closeQuietly(cursor);
        return list;
    }

    /**
     * 查询指定省份的所有City
     * @param db
     * @param ProID
     * @return
     */
    public static List<City> loadCities(SQLiteDatabase db, int ProID) {
        List<City> list=new ArrayList<>();
        Cursor cursor=db.query("T_City",null,"ProID = ?",new String[]{String.valueOf(ProID)},null,null,null);  //查询所有
        while (cursor.moveToNext()) {
            City city=new City();
            city.CityName= cursor.getString(cursor.getColumnIndex("CityName"));
            city.CitySort=cursor.getInt(cursor.getColumnIndex("CitySort"));
            list.add(city);
        }
        Utils.closeQuietly(cursor);
        return list;
    }
}
