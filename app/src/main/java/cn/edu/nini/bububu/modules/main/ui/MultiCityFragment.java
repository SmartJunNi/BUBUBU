package cn.edu.nini.bububu.modules.main.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;

import com.litesuits.orm.db.assit.WhereBuilder;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.mym.plog.PLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseFragment;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.SnackbarUtil;
import cn.edu.nini.bububu.component.OrmLite;
import cn.edu.nini.bububu.component.RetrofitSingleton;
import cn.edu.nini.bububu.component.RxBus;
import cn.edu.nini.bububu.modules.main.adapter.MultiCityAdapter;
import cn.edu.nini.bububu.modules.main.domain.CityORM;
import cn.edu.nini.bububu.modules.main.domain.MultiUpdateEvent;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
    LinearLayout mLinearNocity;
    View mView;
    private List<Weather> mWeathers;
    private MultiCityAdapter mAdapter;

    public static Fragment newInstance(int type) {
        MultiCityFragment fragment = new MultiCityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        PLog.d("onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            TYPE = arguments.getInt("type");
        }
        RxBus.getInstance()
                .toObservable(MultiUpdateEvent.class)
                .subscribe(new SimpleSubscribe<MultiUpdateEvent>() {
                    @Override
                    public void onNext(MultiUpdateEvent event) {
                        multiLoad();
                        Log.d("MultiCityFragment333333", Thread.currentThread().getName());
                        PLog.d("RxBus onNext   mAdapter =" + mAdapter.getItemCount());
                        PLog.d("接收到了消息MultiUpdate");
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PLog.d("onCreateView");
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_multi_city, container, false);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PLog.d("onViewCreated");
        initView();
        multiLoad();
    }

    private void initView() {
        mWeathers = new ArrayList<>();
        mAdapter = new MultiCityAdapter(mWeathers);//mWeathers还没有解决
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv.setAdapter(mAdapter);
        mAdapter.setOnMultiCityLongClick(new MultiCityAdapter.onMultiCityLongClick() {
            @Override
            public void longClick(String city) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("你真的要删除这个城市么？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PLog.d("MultiCityFragment", " which=" + which);
                                OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name = ?", city));
                                //测试
                                OrmLite.OrmTest(OrmLite.class);
                                multiLoad();
                                PLog.d("已经将城市删掉了Ծ‸ Ծ");
                                PLog.d(Thread.currentThread().getName());
                                SnackbarUtil.LongSnackbar(getView(), "已经将城市删掉了 Ծ‸ Ծ", SnackbarUtil.Info)
                                        .setAction("撤销", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //反悔操作，重新保存到数据库
                                                OrmLite.getInstance().save(new CityORM(city));
                                                multiLoad();
                                            }
                                        }).show();//snackbar结束
                            }
                        }).show();//对话框结束
            }
        });


        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_blue_bright
            );
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            multiLoad();
                            Log.d("MultiCityFragment444444", Thread.currentThread().getName());
                        }
                    }, 1000);
                }
            });
        }
    }

    /**
     * 从数据库里读出存在里面的城市，
     */
    private void multiLoad() {
        Log.d("MultiCityFragment1111", Thread.currentThread().getName());
        mWeathers.clear();                              //from  会把集合里每个数据  依次执行
        Observable.defer(() -> Observable.from(OrmLite.getInstance().query(CityORM.class)))//这步可以获得 数据库的存放的城市的集合
                .doOnRequest(aLong -> {
                    mSwipeRefreshLayout.setRefreshing(true);
                    Log.d("MultiCityFragment22222", Thread.currentThread().getName());
                })
                .map(orm -> {
                    Log.d("MultiCityFragment", Utils.replaceCity(orm.getName()));
                    Log.d("MultiCityFragment22222", Thread.currentThread().getName());
                    return Utils.replaceCity(orm.getName());
                })  //ex. 嘉兴   //这里后面都是单个数据
                .compose(RxUtil.rxSchedulerHelper(AndroidSchedulers.mainThread())) //// TODO: 2016/12/18 就他妈是这里  搞了我一个下午
                .distinct()  //取到单个城市后需要去查询
                .flatMap(city -> RetrofitSingleton.getInstance()
                        .getApiService()
                        .mWeather4(city, C.KEY)
                        .map(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0))
                        .compose(RxUtil.rxSchedulerHelper()))
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .filter(weather -> !weather.getStatus().equals(C.UNKNOW_CITY))
                .doOnTerminate(() -> mSwipeRefreshLayout.setRefreshing(false))
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                        PLog.d("onCompleted   mAdapter =" + mAdapter.getItemCount());
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.isEmpty() && mLinearNocity != null) {
                            mLinearNocity.setVisibility(View.VISIBLE);
                        } else if (!mAdapter.isEmpty() && mLinearNocity != null) {
                            mLinearNocity.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mAdapter.isEmpty() && mLinearNocity != null) {
                            mLinearNocity.setVisibility(View.VISIBLE);
                            PLog.d(mAdapter.getItemCount() + "mAdapter.getItemCount()");
                        }
                        RetrofitSingleton.disposeFailureInfo(e);
                    }

                    @Override
                    public void onNext(Weather weather) {
                        mWeathers.add(weather);  //没有执行到这步
                        PLog.d("onNext  :weather" + weather.getBasic().getCity());
                    }
                });
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    public void lazyLoad() {

    }


}
