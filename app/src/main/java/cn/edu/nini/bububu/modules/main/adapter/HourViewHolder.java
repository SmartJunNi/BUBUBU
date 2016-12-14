package cn.edu.nini.bububu.modules.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/11.
 */

public class HourViewHolder extends CommonViewHolder {
    Context mContext;
    Weather mWeather;
    LinearLayout mLinearLayout;
    TextView[] mClock;
    TextView[] mTemp;
    TextView[] mHumidity;
    TextView[] mWind;
    public HourViewHolder(View itemView, Context context,Weather weather) {
        super(itemView);
        mLinearLayout= (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);
        mWeather=weather;
        mContext=context;

        int hours=mWeather.getHourly_forecast().size();
        mClock=new TextView[hours];
        mTemp=new TextView[hours];
        mHumidity=new TextView[hours];
        mWind=new TextView[hours];

        for (int i = 0; i < hours; i++) {
            View view=View.inflate(mContext,R.layout.item_hour_info_line,null);
            mClock[i]= (TextView) getView(view,R.id.one_clock);
            mTemp[i]= (TextView) getView(view,R.id.one_temp);
            mHumidity[i]= (TextView) getView(view,R.id.one_humidity);
            mWind[i]= (TextView) getView(view,R.id.one_wind);
            mLinearLayout.addView(view);
        }
        ButterKnife.bind(this,itemView);
    }

    public void bind(Weather weather){
        for (int i = 0;i<mWeather.getHourly_forecast().size();i++) {
            String mDate=weather.getHourly_forecast().get(i).getDate();
            mClock[i].setText(
                   mDate.substring(mDate.length()-5,mDate.length()));
            mTemp[i].setText(
                    String.format("%s ℃",weather.getHourly_forecast().get(i).getTmp()));
            mHumidity[i].setText(
                    String.format("%s%%",weather.getHourly_forecast().get(i).getHum()));
            mWind[i] .setText(
                    String.format("%sKm/h",weather.getHourly_forecast().get(i).getWind().getSpd()));
        }
    }
}
