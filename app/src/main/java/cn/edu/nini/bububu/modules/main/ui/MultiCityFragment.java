package cn.edu.nini.bububu.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseFragment;

import static cn.edu.nini.bububu.modules.main.ui.FirstFragment.TYPE_NOW_CARD;

/**
 * Created by nini on 2016/12/11.
 */
public class MultiCityFragment extends BaseFragment {
    private int TYPE = TYPE_NOW_CARD;
    @BindView(R.id.recycler_view)
    RecyclerView mRv;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.linear_nocity)
    LinearLayout mLinearLayout;
    View mView;


    public static Fragment newInstance(int type) {
        MultiCityFragment fragment = new MultiCityFragment();
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
        //initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_multi_city, container, false);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load();
    }

   /* private void load() {
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
                        Log.d("FirstFragment", mWeather.getBasic().getCity());
                        safeSetTitle(mWeather.getBasic().getCity());
                    }
                });
    }
*/
    /*private void initView() {
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

    }*/


    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    public void lazyLoad() {

    }
}
