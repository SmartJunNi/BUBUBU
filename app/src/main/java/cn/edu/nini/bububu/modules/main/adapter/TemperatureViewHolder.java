package cn.edu.nini.bububu.modules.main.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/11.
 */

public class TemperatureViewHolder extends CommonViewHolder {

    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.temp_flu)
    TextView tempFlu;
    @BindView(R.id.temp_max)
    TextView tempMax;
    @BindView(R.id.temp_min)
    TextView tempMin;
    @BindView(R.id.temp_pm)
    TextView tempPm;
    @BindView(R.id.temp_quality)
    TextView tempQuality;

    public TemperatureViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Weather weather){
        tempFlu.setText(String.format("%s℃",weather.getNow().getTmp()));
        tempMax.setText(String.format("↑ %s ℃",weather.getDaily_forecast().get(0).getTmp().getMax()));
        tempMin.setText(String.format("↓ %s ℃",weather.getDaily_forecast().get(0).getTmp().getMin()));
        tempPm.setText(String.format("PM2.5: %s μg/m³",weather.getAqi().getCity().getPm25()));
        tempQuality.setText(String.format("空气质量: %s",weather.getAqi().getCity().getQlty()));
        weatherIcon.setImageResource(SharedPreferenceUtil.getInstance().getInt(weather.getNow().getCond().getTxt(),R.mipmap.none));
    }
}
