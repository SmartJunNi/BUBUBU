package cn.edu.nini.bububu.modules.main.adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nini on 2016/12/13.
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {
    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public View getView(View rootView,int resid){
        return rootView.findViewById(resid);
    }



    //todo 四种view的抽象holder
}
