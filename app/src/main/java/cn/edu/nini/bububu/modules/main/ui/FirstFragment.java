package cn.edu.nini.bububu.modules.main.ui;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.modules.base.BaseFragment;
import cn.edu.nini.bububu.modules.base.C;
import cn.edu.nini.bububu.modules.common.utils.ToastUtil;
import cn.edu.nini.bububu.modules.main.adapter.WeatherAdapter;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import kr.co.namee.permissiongen.PermissionGen;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nini on 2016/12/11.
 */
public class FirstFragment extends BaseFragment {
    public static final int TYPE_NOW_CARD = 1;
    public static final int TYPE_FUTURE_CARD = 2;
    public static final int TYPE_MORE_INFO = 3;
    public static final int REQUEST_PERMISSION = 100;
    public static final int COMPLETE_FETCH = 10;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int TYPE = TYPE_NOW_CARD;
    @BindView(R.id.recycler_view)
    RecyclerView mRv;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private Weather mWeather;
    private StringBuilder mWeatherJson;
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
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_first, container, false);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(initData());
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        Action0 onCompletedAction =new Action0() {
            @Override
            public void call() {
                initView();
            }
        };

        Action1<String> onNextAction =new Action1<String>() {
            @Override
            public void call(String s) {
                doParse(s);
            }
        };
        Action1<Throwable> onErrorAction=new Action1<Throwable>() {
            @Override
            public void call(Throwable o) {
                Log.d("FirstFragment", "onError");
            }
        };

        observable.subscribe(onNextAction,onErrorAction,onCompletedAction);*/

        load();
    }

    private void load() {
        mSwipeRefreshLayout.setRefreshing(true);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //在这里执行耗时操作
                initData();
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() { //被订阅
                    @Override
                    public void onCompleted() {
                        //处理主线程的逻辑
                        initView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        ToastUtil.showShort(getString(R.string.refresh_complete));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("FirstFragment", "onError");
                    }

                    @Override
                    public void onNext(String s) {
                        //到这里mWeather已经是获取到了
                        Log.d("FirstFragment", s);
                        safeSetTitle(mWeather.getBasic().getCity());
                    }
                });
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

    private void initData() {
        PermissionGen.with(this).addRequestCode(REQUEST_PERMISSION)
                .permissions(Manifest.permission.INTERNET).request();
         FetchWeather();
    }

    private void FetchWeather() {
        try {
            URL url = new URL(C.target);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            mWeatherJson = new StringBuilder();
            String input;
            while ((input = br.readLine()) != null) {
                mWeatherJson.append(input);
            }
            br.close();
            is.close();

            //处理json数据。
            String weatherJson =  mWeatherJson.toString().toString().trim().
                    subSequence(31,  mWeatherJson.toString().toString().trim().length() - 2).toString();

            Gson gson = new Gson();
            Weather weather = gson.fromJson(weatherJson, Weather.class);
            mWeather = weather;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void lazyLoad() {

    }
}
