package cn.edu.nini.bububu.component;

import cn.edu.nini.bububu.ApiInterface;
import cn.edu.nini.bububu.BuildConfig;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import cn.edu.nini.bububu.modules.main.domain.WeatherAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by nini on 2016/12/17.
 */

public class RetrofitSingleton { //// TODO: 2016/12/17   根据城市名来查询
    private static Retrofit sRetrofit = null;
    private static ApiInterface sApiService = null;

    private void init() {
        initOkHttp();
        initRetrofit();
        sApiService=sRetrofit.create(ApiInterface.class);
    }

    public RetrofitSingleton() {
        init();
    }

    public static RetrofitSingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public ApiInterface getsApiService(){
        return sApiService;
    }

    private  static class SingletonHolder{
        public static final RetrofitSingleton INSTANCE=new RetrofitSingleton();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
        }
    }

    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(C.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Observable<Weather> fetchWeather(String city){
        return sApiService.mWeather4(city, C.KEY)
                .map(new Func1<WeatherAPI, Weather>() {  //将WeatherAPI里的List集合map成weather
                    @Override
                    public Weather call(WeatherAPI api) {
                        return api.mHeWeatherDataService30s.get(0);
                    }
                })
                .compose(RxUtil.rxSchedulerHelper());
    }
}
