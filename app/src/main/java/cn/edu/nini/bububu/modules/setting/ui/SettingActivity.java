package cn.edu.nini.bububu.modules.setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.ToolbarActivity;

/**
 * Created by nini on 2016/12/18.
 */

public class SettingActivity extends ToolbarActivity {
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setTitle("设置");  //it don't work
        getSupportActionBar().setTitle("设置");//  it work;+
        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout,new SettingFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void beforeSetContent() {
        super.beforeSetContent();
    }

    public  static  void launch(Context context){
        context.startActivity(new Intent(context,SettingActivity.class));
    }
}
