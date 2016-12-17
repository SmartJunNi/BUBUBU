package cn.edu.nini.bububu.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.mym.plog.PLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.ApiInterface;
import cn.edu.nini.bububu.base.BaseFragment;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.common.utils.ToastUtil;
import cn.edu.nini.bububu.component.RxBus;
import cn.edu.nini.bububu.modules.main.adapter.WeatherAdapter;
import cn.edu.nini.bububu.modules.main.domain.ChangeCityEvent;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import cn.edu.nini.bububu.modules.main.domain.WeatherAPI;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by nini on 2016/12/11.
 */
public class FirstFragment extends BaseFragment {
    public static final int TYPE_NOW_CARD = 1;
    public static final int TYPE_FUTURE_CARD = 2;
    public static final int TYPE_MORE_INFO = 3;
    public static final int REQUEST_PERMISSION = 100;
    public static final int COMPLETE_FETCH = 10;
    private int TYPE = TYPE_NOW_CARD;
    @BindView(R.id.recycler_view)
    RecyclerView mRv;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.iv_erro)
    ImageView mIvError;
    private View mView;
    private static Weather mWeather = new Weather();//这里用了静态
    private WeatherAdapter mAdapter;


    public static Fragment newInstance(int type) {
        FirstFragment fragment = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            TYPE = arguments.getInt("type");
        }
        PLog.d("onCreate");
        //监听城市变化
        RxBus.getInstance()
                .toObservable(ChangeCityEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscribe<ChangeCityEvent>() {//SimpleSubscribe封装了oncomplete和onerror而已
                    @Override
                    public void onNext(ChangeCityEvent changeCityEvent) {
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                        load();
                        PLog.d("MainRxBus");
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_city, container, false);
            ButterKnife.bind(this, mView);
        }
        PLog.d("onCreateView");
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        load(); //// TODO: 2016/12/16 toolbar标题不显示  y因为把toolbar包裹在了CollapsingToolbarLayout里面
    }


    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(
                () -> mSwipeRefreshLayout.postDelayed(this::load, 1000));

        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new WeatherAdapter(mWeather);
        mRv.setAdapter(mAdapter);
    }

    private void load() {
        retrofit()
                .doOnRequest(aLong -> mSwipeRefreshLayout.setRefreshing(true))
                .doOnError(throwable -> {
                    mIvError.setVisibility(View.VISIBLE);
                    mRv.setVisibility(View.GONE);
                    SharedPreferenceUtil.getInstance().setCityName("北京");
                    safeSetTitle("找不到城市啦");
                })
                .doOnNext(weather -> {
                    mIvError.setVisibility(View.GONE);
                    mRv.setVisibility(View.VISIBLE);
                })
                .doOnTerminate(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgressBar.setVisibility(View.GONE);
                })
                .subscribe(new Observer<Weather>() { //被订阅
                    @Override
                    public void onCompleted() {
                        //处理主线程的逻辑
                        initView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        ToastUtil.showShort(getString(R.string.refresh_complete));
                        PLog.d("oncomplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        PLog.d("onError"+e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Weather weather) {  //要么用list<Weather>  要么就这样写
                        mWeather.setAqi(weather.getAqi());   //mWeather=weather 是错的  ，相当于把一个新的对象赋给它是不行的
                        mWeather.setBasic(weather.getBasic());
                        mWeather.setDaily_forecast(weather.getDaily_forecast());
                        mWeather.setHourly_forecast(weather.getHourly_forecast());
                        mWeather.setNow(weather.getNow());
                        mWeather.setStatus(weather.getStatus());
                        mWeather.setSuggestion(weather.getSuggestion());
                        safeSetTitle(mWeather.getBasic().getCity());
                        mAdapter.notifyDataSetChanged();
                        PLog.d("onNext");
                    }
                });
    }

    private Observable<Weather> retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiInterface service = retrofit.create(ApiInterface.class);
        Observable<WeatherAPI> observable = service.mWeather3("CN101010100", "5f6b4bebfc98499db06709192bcd7283");
        return observable
                .map(new Func1<WeatherAPI, Weather>() {  //将WeatherAPI里的List集合map成weather
                    @Override
                    public Weather call(WeatherAPI api) {
                        return api.mHeWeatherDataService30s.get(0);
                    }
                })
                .compose(RxUtil.rxSchedulerHelper());
    }



    @Override
    public void lazyLoad() {

    }
}
