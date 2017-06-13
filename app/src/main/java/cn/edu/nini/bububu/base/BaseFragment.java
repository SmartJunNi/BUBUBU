package cn.edu.nini.bububu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.components.support.RxFragment;

import cn.edu.nini.bububu.R;


/**
 * Created by nini on 2016/12/13.
 */

public abstract class BaseFragment extends RxFragment {

    protected boolean mIsCreateView = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsCreateView){
            lazyLoad();
        }
    }


    public abstract  void lazyLoad();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()){
            lazyLoad();
        }
    }

    /**
     * 设置当前fragment的标题
     * @param title
     */
    protected void safeSetTitle(String title) {
        ActionBar appBarLayout = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
            appBarLayout.setLogo(R.mipmap.dialog_icon_city);
            appBarLayout.setIcon(R.mipmap.ic_launcher);
        }
    }
}
