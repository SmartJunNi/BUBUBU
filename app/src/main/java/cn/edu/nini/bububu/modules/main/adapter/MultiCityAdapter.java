package cn.edu.nini.bububu.modules.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/17.
 */

public class MultiCityAdapter extends RecyclerView.Adapter <MultiCirtViewHolder>{

private List<Weather> mWeatherList;
    private Context mContext;
    private onMultiCityLongClick mOnMultiCityLongClick;


    public  void setOnMultiCityLongClick(onMultiCityLongClick listener){
        mOnMultiCityLongClick =listener;
    }
    public MultiCityAdapter(List<Weather> weatherList) {
        mWeatherList = weatherList;
    }

    @Override
    public MultiCirtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        return new MultiCirtViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_multi_city,parent,false));
    }

    @Override
    public void onBindViewHolder(MultiCirtViewHolder holder, int position) {
        holder.bind(mWeatherList.get(position));
    }


    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public boolean isEmpty(){
        return 0==mWeatherList.size();
    }

    public interface onMultiCityLongClick {
        void longClick(String city);
    }
}
