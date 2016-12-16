package cn.edu.nini.bububu.modules.main.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.WeatherService;
import cn.edu.nini.bububu.base.BaseFragment;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.common.utils.ToastUtil;
import cn.edu.nini.bububu.modules.main.adapter.WeatherAdapter;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import cn.edu.nini.bububu.modules.main.domain.WeatherAPI;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
    private int TYPE = TYPE_NOW_CARD;
    @BindView(R.id.recycler_view)
    RecyclerView mRv;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private Weather mWeather=new Weather();
    private StringBuilder mWeatherJson;
    private WeatherAdapter mAdapter;
    @BindView(R.id.image)
    ImageView mImageView;


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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_city, container, false);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         initView();

        load(); //// TODO: 2016/12/16 toolbar标题不显示  y因为把toolbar包裹在了CollapsingToolbarLayout里面
        //getWeather();
        //getImage();
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

    /*private void load() {
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        //到这里mWeather已经是获取到了
                        Log.d("FirstFragment", mWeather.getBasic().getCity());
                        safeSetTitle(mWeather.getBasic().getCity());
                    }
                });
    }*/

    private void load() {
        mSwipeRefreshLayout.setRefreshing(true);
        retrofit()
                .subscribe(new Observer<Weather>() { //被订阅
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Weather weather) {
                        mWeather=weather;
                        safeSetTitle(mWeather.getBasic().getCity());
                        mAdapter.notifyDataSetChanged();
                        Log.d("FirstFragment", weather.toString());
                    }

    });
    }

    private void initData() {
        PermissionGen.with(this).addRequestCode(REQUEST_PERMISSION)
                .permissions(Manifest.permission.INTERNET).request();
        //FetchWeather();
        //        getWeather();
    }

    //https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png
    private void getImage() {
        Request request = new Request.Builder()
                .url("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png")
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Observable.just(bitmap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                mImageView.setImageBitmap(bitmap);
                                Log.d("FirstFragment", "图片获取成功");
                            }
                        });
            }
        });
    }

    private Observable<Weather> retrofit() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(C.baseUrl)
                .addConverterFactory( GsonConverterFactory.create())
                .addCallAdapterFactory( RxJavaCallAdapterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Observable<WeatherAPI> observable = service.mWeather3("CN101010100", "5f6b4bebfc98499db06709192bcd7283");
        return observable
                .map(new Func1<WeatherAPI, Weather>() {
                    @Override
                    public Weather call(WeatherAPI api) {
                        return api.mHeWeatherDataService30s.get(0);
                    }
                })
                .compose(tObservable->tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()));
    }


    private void getWeather() {
        Request request = new Request.Builder().url(C.target).build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Observable.just(res)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.d("FirstFragment111", s);
                                //处理json数据。
                                String weatherJson = s.trim().
                                        subSequence(31, s.trim().length() - 2).toString();
                                Log.d("FirstFragment", weatherJson);
                                Gson gson = new Gson();
                                Weather weather = gson.fromJson(weatherJson, Weather.class);
                                mWeather = weather;
                            }
                        });
            }
        });
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
            String weatherJson = mWeatherJson.toString().toString().trim().
                    subSequence(31, mWeatherJson.toString().toString().trim().length() - 2).toString();

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
