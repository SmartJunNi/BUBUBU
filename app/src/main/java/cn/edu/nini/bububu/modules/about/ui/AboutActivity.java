package cn.edu.nini.bububu.modules.about.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseActivity;
import cn.edu.nini.bububu.common.utils.NiniUtil;
import cn.edu.nini.bububu.common.utils.StatusBarUtil;

/**
 * Created by nini on 2016/12/19.
 */

public class AboutActivity extends BaseActivity {


    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        StatusBarUtil.setImmersiveStatusBar(this);

        DisplayMetrics metrics=new DisplayMetrics();
        Log.d("AboutActivity", metrics.toString());
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("AboutActivity", metrics.toString());
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
            mTvVersion.setText(String.format("当前版本:%s (Build %s)", NiniUtil.getVersion(this),NiniUtil.getVersionCode(this)));
//            mCollapsingToolbarLayout.setTitleEnabled(false);
            mCollapsingToolbarLayout.setTitle("BuBuBu");

    }


    public static void launch(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
