package cn.edu.nini.bububu;

import cn.edu.nini.bububu.modules.main.domain.Weather;
import cn.edu.nini.bububu.modules.main.domain.WeatherAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nini on 2016/12/16.
 */

public interface ApiInterface {
    @GET("weather")
    Observable<Weather> mWeather(@Query("cityid") String cityId,
                                 @Query("key") String key);

    @GET("weather")
    Call<Weather> mWeather2(@Query("cityid") String cityId,
                            @Query("key") String key);

    @GET("weather")
    Observable<WeatherAPI> mWeather3(@Query("cityid") String cityId,
                                     @Query("key") String key);
    @GET("weather")
    Observable<WeatherAPI> mWeather4(@Query("city") String cityId,
                                     @Query("key") String key);
}
