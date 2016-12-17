package cn.edu.nini.bububu.modules.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseViewHolder;
import cn.edu.nini.bububu.base.Utils;
import cn.edu.nini.bububu.common.utils.SharedPreferenceUtil;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/11.
 */

public class ForecastViewHolder extends BaseViewHolder<Weather> {
    Context mContext;
    Weather mWeather;
    LinearLayout mForecastLinear;
    private TextView[] forecastDate;
    private TextView[] forecastTemp;
    private TextView[] forecastTxt;
    private ImageView[] forecastIcon;

    public ForecastViewHolder(View itemView, Context context, Weather weather) {
        super(itemView);
        mForecastLinear = (LinearLayout) itemView.findViewById(R.id.item_forecast_info_linearlayout);
        mWeather = weather;
        mContext = context;

        int days = mWeather.getDaily_forecast().size();
        forecastDate = new TextView[days];
        forecastTemp = new TextView[days];
        forecastTxt = new TextView[days];
        forecastIcon = new ImageView[days];

        for (int i = 0; i < days; i++) {
            View view = View.inflate(mContext, R.layout.item_forecast_line, null);
            forecastDate[i] = (TextView) getView(view, R.id.forecast_date);
            forecastTemp[i] = (TextView) getView(view, R.id.forecast_temp);
            forecastTxt[i] = (TextView) getView(view, R.id.forecast_txt);
            forecastIcon[i] = (ImageView) getView(view, R.id.forecast_icon);
            mForecastLinear.addView(view);
        }
        ButterKnife.bind(this, itemView);
    }

    public void bind(Weather weather) {
        try {
            forecastDate[0].setText("今日");
            forecastDate[1].setText("明日");
            for (int i = 0; i < mWeather.getDaily_forecast().size(); i++) {
                if (i > 1) {
                    forecastDate[i].setText(Utils.dayForWeek(weather.getDaily_forecast().get(i).getDate()));
                }
                forecastIcon[i].setImageResource(SharedPreferenceUtil.getInstance().getInt(
                        weather.getDaily_forecast().get(i).getCond().getTxt_d(),R.mipmap.none
                ));

                forecastTemp[i].setText(String.format("%s℃ - %s℃", weather.getDaily_forecast().get(i).getTmp().getMin(),
                        weather.getDaily_forecast().get(i).getTmp().getMax()));

                forecastTxt[i].setText(String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                        weather.getDaily_forecast().get(i).getCond().getTxt_d(),
                        weather.getDaily_forecast().get(i).getWind().getSc(),
                        weather.getDaily_forecast().get(i).getWind().getDir(),
                        weather.getDaily_forecast().get(i).getWind().getSpd(),
                        weather.getDaily_forecast().get(i).getPop()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
