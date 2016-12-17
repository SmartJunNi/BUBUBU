package cn.edu.nini.bububu.modules.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseViewHolder;
import cn.edu.nini.bububu.component.AnimRecyclerViewAdapter;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/11.
 */

public class WeatherAdapter extends AnimRecyclerViewAdapter<BaseViewHolder> {
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FORE = 3;
    private Weather mWeather;

    public WeatherAdapter(Weather weather) {
        mWeather = weather;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == WeatherAdapter.TYPE_ONE) {
            return WeatherAdapter.TYPE_ONE;
        }
        if (position == WeatherAdapter.TYPE_TWO) {
            return WeatherAdapter.TYPE_TWO;
        }
        if (position == WeatherAdapter.TYPE_THREE) {
            return WeatherAdapter.TYPE_THREE;
        }
        if (position == WeatherAdapter.TYPE_FORE) {
            return WeatherAdapter.TYPE_FORE;
        }
        return super.getItemViewType(position);//这里会有问题吗
    }

    @Override

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return new TemperatureViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_temperature, parent, false));//TODO难问题出在这里草。
            case TYPE_TWO:
                return new HourViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour_info, parent, false),
                        parent.getContext(),
                        mWeather);//TODO难问题出在这里草。
            case TYPE_THREE:
                return new SuggestViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false));//TODO难问题出在这里草。
            case TYPE_FORE:
                return new ForecastViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast_info, parent, false),
                        parent.getContext(),
                        mWeather);//TODO难问题出在这里草。
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((TemperatureViewHolder) holder).bind(mWeather);
                break;
            case TYPE_TWO:
                ((HourViewHolder) holder).bind(mWeather);
                break;
            case TYPE_THREE:
                ((SuggestViewHolder) holder).bind(mWeather);
                break;
            case TYPE_FORE:
                ((ForecastViewHolder) holder).bind(mWeather);
                break;
        }
        showItemAnim(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return mWeather.getStatus() != null ? 4 : 0;
    }
}
