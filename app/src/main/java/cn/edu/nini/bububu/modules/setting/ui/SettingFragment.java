package cn.edu.nini.bububu.modules.setting.ui;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.mym.plog.PLog;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseApplication;
import cn.edu.nini.bububu.common.utils.FileSizeUtil;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.common.utils.SnackbarUtil;
import cn.edu.nini.bububu.component.RxBus;
import cn.edu.nini.bububu.modules.main.domain.ChangeCityEvent;
import cn.edu.nini.bububu.modules.main.ui.MainActivity;

/**
 * Created by nini on 2016/12/18.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private Preference mChangeIcons;
    private Preference mChangeUpdate;
    private CheckBoxPreference mAnimationOnOff;
    private CheckBoxPreference mNotificationType;
    private Preference mClearCache;
    private SharedPreferenceUtil mSharedPreferenceUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这步最关键
        addPreferencesFromResource(R.xml.setting);
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();

        mChangeIcons = findPreference(SharedPreferenceUtil.CHANGE_ICONS);
        mChangeUpdate = findPreference(SharedPreferenceUtil.AUTO_UPDATE);
        mClearCache = findPreference(SharedPreferenceUtil.CLEAR_CACHE);
        mAnimationOnOff = (CheckBoxPreference) findPreference(SharedPreferenceUtil.ANIM_START);
        mNotificationType = (CheckBoxPreference) findPreference(SharedPreferenceUtil.NOTIFICATION_MODEL);

        //设置通知栏常驻
        if (SharedPreferenceUtil.getInstance().getNotificationModel() != Notification.FLAG_ONGOING_EVENT) {
            mNotificationType.setChecked(false);
        } else {
            mNotificationType.setChecked(true);
        }
        //设置首页动画是否开启
        mAnimationOnOff.setChecked(SharedPreferenceUtil.getInstance().getMainAnim());
        //设置图标样式  以及item下方的summary 副标题
        mChangeIcons.setSummary(
                getResources().getStringArray(R.array.icons)
                        [SharedPreferenceUtil.getInstance().getIconType()]);
        //设置自动更新
        mChangeUpdate.setSummary(
                mSharedPreferenceUtil.getAutoUpdate() == 0 ? "禁止刷新" : "每" +
                        mSharedPreferenceUtil.getAutoUpdate() + "小时更新"
        );
        //设置缓存
        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFolderSize(BaseApplication.getAppCacheDir()));
        PLog.d("系统缓存路径" + BaseApplication.getAppCacheDir());

        mChangeIcons.setOnPreferenceClickListener(this);
        mChangeUpdate.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
        mAnimationOnOff.setOnPreferenceChangeListener(this);
        mNotificationType.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mChangeIcons == preference) {
            showIconDialog();
        }
        if (mChangeUpdate == preference) {
            showUpdateDialog();
        }
        if (mClearCache == preference) {
            SnackbarUtil.LongSnackbar(getView(), "缓存已清除", SnackbarUtil.Info).show();
        }
        return true;
    }

    /**
     * 选择更新间隔的对话框
     */
    private void showUpdateDialog() {
        //网对话框中放入我们自定义的layout
        LinearLayout layoutRoot = (LinearLayout) getActivity().findViewById(R.id.ll_root);
        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.update_dialog, layoutRoot, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialog);
        AlertDialog alertDialog = builder.show();

        SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seekbar);
        TextView tv_hour = (TextView) dialog.findViewById(R.id.tv_hour);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        seekbar.setMax(24);
        seekbar.setProgress(mSharedPreferenceUtil.getAutoUpdate());
        tv_hour.setText("每" + mSharedPreferenceUtil.getAutoUpdate() + "小时刷新");
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_hour.setText("每" + progress + "小时刷新");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv_ok.setOnClickListener((v) -> {
            mSharedPreferenceUtil.setAutoUpdate(seekbar.getProgress());
            mChangeUpdate.setSummary(
                    mSharedPreferenceUtil.getAutoUpdate() == 0 ? "禁止刷新" : "每" +
                            mSharedPreferenceUtil.getAutoUpdate() + "小时更新");
            alertDialog.dismiss();
        });

    }

    /**
     * 选择天气图标的对话框
     */
    private void showIconDialog() {
        //创建自定义的AlertDialog
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.icon_dialog, (ViewGroup) getActivity().findViewById(R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        AlertDialog alertDialog = builder.create();

        LinearLayout layoutTypeOne = (LinearLayout) dialogLayout.findViewById(R.id.layout_one);
        layoutTypeOne.setClickable(true);
        RadioButton radioTypeOne = (RadioButton) dialogLayout.findViewById(R.id.radio_one);
        LinearLayout layoutTypeTwo = (LinearLayout) dialogLayout.findViewById(R.id.layout_two);
        layoutTypeTwo.setClickable(true);
        RadioButton radioTypeTwo = (RadioButton) dialogLayout.findViewById(R.id.radio_two);
        TextView done = (TextView) dialogLayout.findViewById(R.id.done);

        radioTypeOne.setClickable(false);
        radioTypeTwo.setClickable(false);


        alertDialog.show();

        //读取上次保存的选项
        switch (mSharedPreferenceUtil.getIconType()) {
            case 0:
                radioTypeOne.setChecked(true);
                radioTypeTwo.setChecked(false);
                break;
            case 1:
                radioTypeOne.setChecked(false);
                radioTypeTwo.setChecked(true);
                break;
        }

        layoutTypeOne.setOnClickListener(v -> {
            radioTypeOne.setChecked(true);
            radioTypeTwo.setChecked(false);
        });
        layoutTypeTwo.setOnClickListener(v -> {
            radioTypeOne.setChecked(false);
            radioTypeTwo.setChecked(true);
        });


        done.setOnClickListener(v -> {
            //保存到sharedpreference
            mSharedPreferenceUtil.setIconType(radioTypeOne.isChecked() ? 0 : 1);
            mChangeIcons.setSummary(
                    getResources().getStringArray(R.array.icons)
                            [SharedPreferenceUtil.getInstance().getIconType()]);
            alertDialog.dismiss();

            SnackbarUtil.IndefiniteSnackbar(getView(), "切换成功，重启应用生效",
                    Snackbar.LENGTH_INDEFINITE, Color.YELLOW, getResources().getColor(R.color.sunnyDark)).setAction("重启", v1 -> {
                //如何重启呢
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().finish();
                //发送一个消息，让主页重新查询并刷新
                RxBus.getInstance().post(new ChangeCityEvent());
            }).show();
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAnimationOnOff) {
            mSharedPreferenceUtil.setMainAnim(((boolean) newValue) );

        }
        if (preference == mNotificationType) {
            mSharedPreferenceUtil.setNotificationModel(((boolean) newValue)
                    ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL);
        }
        return true;
    }
}
