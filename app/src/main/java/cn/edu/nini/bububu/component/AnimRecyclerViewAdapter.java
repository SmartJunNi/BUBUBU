package cn.edu.nini.bububu.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by nini on 2016/12/17.
 */

public class AnimRecyclerViewAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    public static final int Delay = 138;
    private int mLastPosition = -1;
    private int mAnim= android.R.anim.slide_in_left;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     *  根据传入的view，依次执行动画。
     * @param view
     * @param position
     */
    public void showItemAnim(final View view, final int position) {

        if(position>mLastPosition){    //这里position会依次增大 ，所以这样是个很好的解决方法
            view.setAlpha(0);
            view.postDelayed(()->{
                Animation animation= AnimationUtils.loadAnimation(view.getContext(), mAnim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }, Delay *position);
            mLastPosition=position;
        }
    }

    public void setAnimation(int animationId){
        this.mAnim=animationId;
    }
}
