package cn.edu.nini.bububu.modules.main.adapter;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.modules.main.domain.Weather;

/**
 * Created by nini on 2016/12/13.
 */

public class SuggestViewHolder extends CommonViewHolder {

    @BindView(R.id.cloth_brief)
    TextView clothBrief;
    @BindView(R.id.cloth_txt)
    TextView clothTxt;
    @BindView(R.id.sport_brief)
    TextView sportBrief;
    @BindView(R.id.sport_txt)
    TextView sportTxt;
    @BindView(R.id.travel_brief)
    TextView travelBrief;
    @BindView(R.id.travel_txt)
    TextView travelTxt;
    @BindView(R.id.flu_brief)
    TextView fluBrief;
    @BindView(R.id.flu_txt)
    TextView fluTxt;

    public SuggestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Weather weather){
        clothBrief.setText(String.format("穿衣指数---%s", weather.getSuggestion().getDrsg().getBrf()));
        clothTxt.setText(weather.getSuggestion().getDrsg().getTxt());

        sportBrief.setText(String.format("运动指数---%s", weather.getSuggestion().getSport().getBrf()));
        sportTxt.setText(weather.getSuggestion().getSport().getTxt());

        travelBrief.setText(String.format("旅游指数---%s", weather.getSuggestion().getTrav().getBrf()));
        travelTxt.setText(weather.getSuggestion().getTrav().getTxt());

        fluBrief.setText(String.format("感冒指数---%s", weather.getSuggestion().getFlu().getBrf()));
        fluTxt.setText(weather.getSuggestion().getFlu().getTxt());
    }
}
