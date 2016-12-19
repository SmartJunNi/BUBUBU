package cn.edu.nini.bububu.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.edu.nini.bububu.R;

/**
 * Created by nini on 2016/12/17.
 */

public abstract class ToolbarActivity extends BaseActivity {
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;

    /**
     * 子类必须要重写提供布局
     * @return
     */
    abstract protected int provideContentViewId();

    protected void onToolbarClick(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContent();//自定义方法 可以让子类重写。
        setContentView(provideContentViewId());//子类提供的布局
        mAppBar = (AppBarLayout) findViewById(R.id.appbar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("The subclass of ToolbarActivity must contain a toolbar.");
        }
        /**
         *  子类可以有选择得提供此方法
         */
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarClick();
            }
        });

        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {  //actionbar  浮起
            mAppBar.setElevation(10f);
        }
    }

    /**
     * 判断当前是省份选择页面还是城市选择
     * @return
     */
    public boolean canBack() {
        return false;
    }


    public Toolbar getToolbar(){
        return mToolbar;
    }

    protected void beforeSetContent() {

    }
}
