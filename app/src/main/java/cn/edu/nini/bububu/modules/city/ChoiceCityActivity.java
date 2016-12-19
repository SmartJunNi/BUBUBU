package cn.edu.nini.bububu.modules.city;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.mym.plog.PLog;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.base.ToolbarActivity;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.component.OrmLite;
import cn.edu.nini.bububu.component.RxBus;
import cn.edu.nini.bububu.modules.city.adapter.CityAdapter;
import cn.edu.nini.bububu.modules.city.db.DBManager;
import cn.edu.nini.bububu.modules.city.db.WeatherDB;
import cn.edu.nini.bububu.modules.city.domain.City;
import cn.edu.nini.bububu.modules.city.domain.Province;
import cn.edu.nini.bububu.modules.main.domain.ChangeCityEvent;
import cn.edu.nini.bububu.modules.main.domain.CityORM;
import cn.edu.nini.bububu.modules.main.domain.MultiUpdateEvent;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nini on 2016/12/14.
 * info: 适配所有 toolbar 的 activity
 */

public class ChoiceCityActivity extends ToolbarActivity {

    private RecyclerView mRecyclerview;
    private ProgressBar mProgressBar;
    private List<String> mDataList = new ArrayList<>();  //保存省份或者城市的l集合
    private List<Province> mProList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();
    private Province selectedProvince;
    private City selectedCity;

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    private int currentLevel;  //表示现在是Province选择页还是City选择页
    private CityAdapter mAdapter;

    private boolean isChecked = false;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_choice_city;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Observable.defer(() -> {
            DBManager.getInstance().openDatabase();//打开数据库
            return Observable.just(1); //这里传1   没有其他意思
        }).compose(RxUtil.rxSchedulerHelper())
                .compose(this.bindToLifecycle())//绑定声明周期，自动释放
                .subscribe(integer -> {
                    //初始化RecyclerView
                    initRecyclerView();
                    //查询省份√
                    queryProvinces();
                });

        Intent intent = getIntent();
        isChecked = intent.getBooleanExtra(C.MULTI_CHECK, false);
        if(isChecked&&SharedPreferenceUtil.getInstance().getBoolean("Tips",true)){
            showTips();
        }
    }


    private void initRecyclerView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setHasFixedSize(true);  //如果不是瀑布流 最好设置这个

        mAdapter = new CityAdapter(this, mDataList);
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, position) -> {
            if (currentLevel == LEVEL_PROVINCE) { //判断当前是省份还是城市
                selectedProvince = mProList.get(position);//取到选择的城市信息
                mRecyclerview.smoothScrollToPosition(0);
                queryCities();
            } else if (currentLevel == LEVEL_CITY) {
                String city = Utils.replaceCity(mCityList.get(position).CityName);
                Log.d("ChoiceCityActivity", "当前选中城市为："+city);
                if(isChecked){
                    PLog.d("是多城市管理模式");
                    OrmLite.getInstance().save(new CityORM(city));  //// TODO: 2016/12/17  保存这个城市到数据库
                    RxBus.getInstance().post(new MultiUpdateEvent());  //向MultiCityFragment发送一个消息
                }else{
                    SharedPreferenceUtil.getInstance().setCityName(city);
                    RxBus.getInstance().post(new ChangeCityEvent());
                }

                quit();
            }
        });
    }


    private void initView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_city_menu, menu);
        menu.getItem(0).setChecked(isChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.multi_check) {
            item.setChecked(!isChecked);// 取反
            isChecked = item.isChecked();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 首先获取省份数据
     */
    public void queryProvinces() {
        getToolbar().setTitle("选择省份");
        Observable.defer(() -> {
            if (mProList.isEmpty()) {
                mProList.addAll(WeatherDB.loadProvinces(DBManager.getInstance().getDatabase()));//查询所有省份
            }
            mDataList.clear();//   ???
            return Observable.from(mProList);
        })
                .map(province -> province.ProName)//城市实体转换为城市名字
                .toList()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> mProgressBar.setVisibility(View.GONE))
                .subscribe(new SimpleSubscribe<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        mDataList.addAll(strings);
                    }

                    @Override
                    public void onCompleted() {
                        currentLevel = LEVEL_PROVINCE;
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void queryCities() {
        getToolbar().setTitle("选择城市");
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        Observable.defer(() -> {
            //            if (mCityList.isEmpty()) {  //这里不能加这个判断了，不然用户点了一个省份返回，
            //再点另外一个，这时mCityList是不为空的，那就会显示上一次点击的省份
            mCityList.addAll(WeatherDB.loadCities(DBManager.getInstance().getDatabase(), selectedProvince.ProSort));//查询所有省份
            //            }
            mDataList.clear();//   ???
            return Observable.from(mCityList);
        })
                .map(city -> city.CityName)//城市实体转换为城市名字
                .toList()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(this.bindToLifecycle())
                .doOnTerminate(() -> mProgressBar.setVisibility(View.GONE))
                .subscribe(new SimpleSubscribe<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        mDataList.addAll(strings);
                    }

                    @Override
                    public void onCompleted() {
                        currentLevel = LEVEL_CITY;
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 处理在city选择时按back键直接返回主页
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_PROVINCE) {
            quit();
        } else {
            queryProvinces();
            mRecyclerview.smoothScrollToPosition(0);//因为城市和省份显示都在一个activity里  所以需要这样做
        }
    }

    private void quit() {
        ChoiceCityActivity.this.finish();
    }

    private void showTips() {
        new AlertDialog.Builder(this).setTitle("多城市管理模式").setMessage("您现在是多城市管理模式,直接点击即可新增城市.如果暂时不需要添加,"
                + "在右上选项中关闭即可像往常一样操作.\n因为 api 次数限制的影响,多城市列表最多三个城市.(๑′ᴗ‵๑)"
        ).setPositiveButton("明白", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtil.getInstance().putBoolean("Tips", false);
            }
        }).show();
    }

    /**
     * 这里return true之后   就可以显示向右的箭头了
     * @return
     */
    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void beforeSetContent() {
        super.beforeSetContent();
    }

    public  static  void launch(Context context){
        context.startActivity(new Intent(context,ChoiceCityActivity.class));
    }
}
