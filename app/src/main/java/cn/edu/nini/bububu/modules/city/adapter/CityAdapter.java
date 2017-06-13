package cn.edu.nini.bububu.modules.city.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.component.AnimRecyclerViewAdapter;

/**
 * Created by nini on 2016/12/17.
 */

public class CityAdapter extends AnimRecyclerViewAdapter<CityViewHolder> {

    private List<String> mDataList;
    private Context mContext;
    private OnRecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public CityAdapter(Context context, List<String> dataList) {
        mDataList = dataList;
        mContext = context;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.bind(mDataList.get(position));//这里需要省份LIst
        holder.mCardView.setOnClickListener(v->{
            mRecyclerViewOnClickListener.onItemClick(v,position);
        });
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 设置回调
     */
    public interface OnRecyclerViewOnClickListener{
        void onItemClick(View view ,int  position);
    }

    public void setOnItemClickListener(OnRecyclerViewOnClickListener listener){
        mRecyclerViewOnClickListener=listener;
        SQLiteDatabase db=SQLiteDatabase.create(null);

    }
}
