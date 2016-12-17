package cn.edu.nini.bububu.modules.main.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.mym.plog.PLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseFragment;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.SnackbarUtil;
import cn.edu.nini.bububu.component.OrmLite;
import cn.edu.nini.bububu.component.RxBus;
import cn.edu.nini.bububu.modules.main.adapter.MultiCityAdapter;
import cn.edu.nini.bububu.modules.main.domain.CityORM;
import cn.edu.nini.bububu.modules.main.domain.MultiUpdateEvent;
import cn.edu.nini.bububu.modules.main.domain.Weather;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            TYPE = arguments.getInt("type");
        }
        RxBus.getInstance()
                .toObservable(MultiUpdateEvent.class)
                .subscribe(new SimpleSubscribe() {
                    @Override
                    public void onNext(Object o) {
                        //multiLoad();
                        PLog.d("接收到了消息MultiUpdate");
                    }
                });
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
//        initView();

    }

    private void initView() {
        mWeathers=new ArrayList<>();
        mAdapter=new MultiCityAdapter(mWeathers);//mWeathers还没有解决
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
                                PLog.d("MultiCityFragment", "执行删除操作");
                                SnackbarUtil.ShortSnackbar(getView(), "已经将城市删掉了 Ծ‸ Ծ",SnackbarUtil.Info).show();
                            }
                        }).show();
            }
        });
    }

    private void multiLoad() {
        mWeathers.clear();
        Observable.defer(new Func0<Observable<CityORM>>() {  //defer可以
            @Override
            public Observable<CityORM> call() {
                return Observable.from(OrmLite.getLiteOrm().query(CityORM.class));
            }
        })
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Action1<CityORM>() {
                    @Override
                    public void call(CityORM orm) {
                        //因为需要先选择城市，再进行获取，所以要先处理choicecityActivity
                    }
                });
                /*.doOnRequest(aLong -> mSwipeRefreshLayout.setRefreshing(true))
                .map(new Func1<CityORM, String>() {
                    @Override
                    public String call(CityORM orm) {
                        return orm.getName();
                    }
                });*/
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    public void lazyLoad() {

    }


}
