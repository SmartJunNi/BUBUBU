package cn.edu.nini.bububu.modules.main.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.mym.plog.PLog;

import butterknife.BindView;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseViewHolder;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/17.
 */

public class MultiCirtViewHolder extends BaseViewHolder<Weather> {

    @BindView(R.id.dialog_city)
    TextView mDialogCity;
    @BindView(R.id.dialog_icon)
    ImageView mDialogIcon;
    @BindView(R.id.dialog_temp)
    TextView mDialogTemp;
    @BindView(R.id.cardView)
    CardView mCardView;

    public MultiCirtViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(Weather weather) {
        //设置城市和温度
        try {
            mDialogCity.setText(Utils.safeText(weather.getBasic().getCity()));
            mDialogTemp.setText(String.format("%s℃",weather.getNow().getTmp()));
        } catch (Exception e) {
            PLog.e(e.getMessage());
        }
        mDialogIcon.setImageResource(
                SharedPreferenceUtil.getInstance().getInt(weather.getNow().getCond().getTxt(),R.mipmap.none));
        mDialogIcon.setColorFilter(Color.WHITE);// ?

        PLog.d(weather.getNow().getCond().getTxt() + " " + weather.getNow().getCond().getCode());
    }
}
