package cn.edu.nini.bububu.base;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by nini on 2016/12/14.
 */

public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
