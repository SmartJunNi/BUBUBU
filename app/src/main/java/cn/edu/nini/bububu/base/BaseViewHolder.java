package cn.edu.nini.bububu.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by nini on 2016/12/17.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public View getView(View rootView,int resid){
        return rootView.findViewById(resid);
    }

    protected  abstract void bind(T t);
}
