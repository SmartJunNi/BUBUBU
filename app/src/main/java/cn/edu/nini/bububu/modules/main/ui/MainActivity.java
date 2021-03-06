package cn.edu.nini.bububu.modules.main.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseActivity;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.common.utils.CircularAnimUtil;
import cn.edu.nini.bububu.common.utils.RxDrawer;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.common.utils.SnackbarUtil;
import cn.edu.nini.bububu.common.utils.ToastUtil;
import cn.edu.nini.bububu.modules.about.ui.AboutActivity;
import cn.edu.nini.bububu.modules.city.ChoiceCityActivity;
import cn.edu.nini.bububu.modules.main.adapter.MyViewPageAdapter;
import cn.edu.nini.bububu.modules.service.AutoUpdateService;
import cn.edu.nini.bububu.modules.setting.ui.SettingActivity;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.viewpage)
    ViewPager mViewPager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawlayout)
    DrawerLayout mDrawerLayout;
private  SharedPreferenceUtil mSharedPreferenceUtil=SharedPreferenceUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initIcon();
        initDrawer();
        //启动就开启服务
        startService(new Intent(MainActivity.this, AutoUpdateService.class));
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        MyViewPageAdapter adapter = new MyViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(FirstFragment.newInstance(1), "主页面");
        adapter.addFragment(MultiCityFragment.newInstance(2), "多城市");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 1) {
                    mFab.setImageResource(R.drawable.ic_add_24dp);
                    mFab.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                    );//设置背景色调
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ChoiceCityActivity.class);
                            intent.putExtra(C.MULTI_CHECK, true);
                            //// TODO: 2016/12/15 传递参数到 ChoiceCityActivity
                            CircularAnimUtil.startActivity(MainActivity.this, intent, mFab, R.color.colorPrimary);
                        }
                    });
                    if (!mFab.isShown()) {
                        mFab.show();
                    }
                } else {
                    mFab.setImageResource(R.drawable.ic_favorite);
                    mFab.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorAccent)));

                    mFab.setOnClickListener((v)->showSnackBar());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mFab.setOnClickListener(v->showSnackBar());
    }


    private   void showSnackBar(){
        Snackbar snackBar = SnackbarUtil.ShortSnackbar(mCoordinatorLayout, "Hello~", SnackbarUtil.Info);
        snackBar.setAction("动作", (vi) -> ToastUtil.showShort("你好O(∩_∩)O~"))
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        ToastUtil.showShort("我消失了~");
                    }
                }).show();
    }
    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        //https://segmentfault.com/a/1190000004151222
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
            //navigationView.setItemIconTintList(null);
            mNavView.inflateHeaderView(R.layout.nav_header_main);
            //下面三步是添加actionbar按钮的关键
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        RxDrawer.close(mDrawerLayout)
                .compose(RxUtil.rxSchedulerHelper(AndroidSchedulers.mainThread()))
                .subscribe(new SimpleSubscribe() {
                    @Override
                    public void onNext(Object o) {
                        switch (item.getItemId()) {
                            case R.id.nav_city:
                                ChoiceCityActivity.launch(MainActivity.this);
                                break;
                            case R.id.nav_multi_cities:
                                mViewPager.setCurrentItem(1);
                                break;
                            case R.id.nav_set:
                                SettingActivity.launch(MainActivity.this);
                                break;
                            case R.id.nav_about:
                                AboutActivity.launch(MainActivity.this);
                                break;
                        }
                    }
                });
        return true;
    }

    /**
     * 初始化Icon
     */
    private void initIcon() {
        if (SharedPreferenceUtil.getInstance().getIconType() == 0) {
            SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.none);
            SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.type_one_sunny);
            SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
            SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.type_one_light_rain);
            SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.type_one_light_rain);
            SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.type_one_heavy_rain);
            SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.type_one_thunderstorm);
            SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
            SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.type_one_fog);
            SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.type_one_fog);
        } else {
            SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.none);
            SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.type_two_sunny);
            SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.type_two_cloudytosunny);
            SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.type_two_light_rain);
            SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.type_two_thunderstorm);
            SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.type_two_haze);
            SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.type_two_fog);
            SharedPreferenceUtil.getInstance().putInt("雨夹雪", R.mipmap.type_two_snowrain);
        }
    }


}
