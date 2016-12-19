package cn.edu.nini.bububu.component;

import com.litesuits.orm.db.assit.WhereBuilder;

import org.mym.plog.PLog;

import cn.edu.nini.bububu.ApiInterface;
import cn.edu.nini.bububu.BuildConfig;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.ToastUtil;
import cn.edu.nini.bububu.modules.main.domain.CityORM;
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

    public ApiInterface getApiService(){
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
                .flatMap(weatherAPI->{
                    String status = weatherAPI.mHeWeatherDataService30s.get(0).getStatus();
                    if ("unknown city".equals(status)) {//这样可以在城市找不到的情况下，往onerror里抛出自定义异常
                        return Observable.error(new RuntimeException(String.format("API没有%s", city)));
                    }
                    return Observable.just(weatherAPI);
                })
                .map(new Func1<WeatherAPI, Weather>() {  //将WeatherAPI里的List集合map成weather
                    @Override
                    public Weather call(WeatherAPI api) {
                        return api.mHeWeatherDataService30s.get(0);
                    }
                })
                .compose(RxUtil.rxSchedulerHelper());
    }

    /**
     * 统一处理异常信息
     * @param t
     */
    public static void disposeFailureInfo(Throwable t) {
        if(t.toString().contains("SocketTimeoutException")){
            ToastUtil.showShort("网络问题");
        }else if(t.toString().contains("API没有")){//如果是报的我们自定义的错误
            //因为在选择城市的时候已经把这个错误的城市添加到了数据库，所以要先删除
            OrmLite.getInstance().delete(new WhereBuilder(CityORM.class)
            .where("name=?", Utils.replaceInfo(t.getMessage())));
            PLog.w(Utils.replaceInfo(t.getMessage()));
            ToastUtil.showShort("错误：" +t.getMessage());
        }
        PLog.w(t.getMessage());
    }
}
