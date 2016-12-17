package cn.edu.nini.bububu.modules.city.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseViewHolder;

/**
 * Created by nini on 2016/12/17.
 */

public class CityViewHolder extends BaseViewHolder<String> {

    @BindView(R.id.item_city)
    TextView mItemCity ;
    @BindView(R.id.cardView)
    CardView mCardView;

    public CityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bind(String s) {
        mItemCity.setText(s);
    }
}
