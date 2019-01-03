package hewenle.com.mywidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * 属性动画，顺序弹出标签
 */
public class AnimationFramelayout extends RelativeLayout implements View.OnClickListener {
    private ArrayList<View> mViewLists;
    private ArrayList<String> mAimationLists;
    private boolean isClose = true;
    private AnimatorSet animatorSet;
    private String animation0, animation1;
    private float translantionRange = 150f;
    private int mDuration = 500;
    private int startAlpha = 0;
    private int endAlpha = 1;

    public AnimationFramelayout(Context context) {
        this(context, null);
    }

    public AnimationFramelayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewLists = new ArrayList<>();
        mAimationLists = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimationFramelayout);
        animation0 = typedArray.getString(R.styleable.AnimationFramelayout_animation0);
        animation1 = typedArray.getString(R.styleable.AnimationFramelayout_animation1);

        if (animation0 == null && animation1 == null) {
            animation0 = "translationY";
            animation1 = "alpha";
            mAimationLists.add(animation0);
            mAimationLists.add(animation1);
        } else {
            if (animation0 != null) {
                mAimationLists.add(animation0);
            }
            if (animation1 != null) {
                mAimationLists.add(animation1);
            }
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mViewLists.add(child);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewLists.get(mViewLists.size()-1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mViewLists.get(mViewLists.size() - 1).getId()) {
            //这是最后1个imageview的id
            if (isClose) {
                for (int y = 0; y < mViewLists.size() - 1; y++) {//mIdLists.size()-1是因为最后1个响应动画事件
                    animatorSet = new AnimatorSet();
                    switch (mAimationLists.size()) {//肯定不是空的
                        case 1:
                            if (mAimationLists.get(0).contains("translation")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), y * translantionRange, (y + 1) * translantionRange));
                            }
                            if (mAimationLists.get(0).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), startAlpha, endAlpha));
                            }
                            break;
                        case 2:
                            if (mAimationLists.get(0).contains("translation") && mAimationLists.get(1).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), y * translantionRange, (y + 1) * translantionRange)
                                        , ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(1), startAlpha, endAlpha)
                                );
                            }
                            if (mAimationLists.get(1).contains("translation") && mAimationLists.get(0).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(1), y * translantionRange, (y + 1) * translantionRange)
                                        , ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), startAlpha, endAlpha)
                                );
                            }
                            break;
                    }
                    animatorSet.setDuration(mDuration);
                    animatorSet.setInterpolator(new BounceInterpolator());
                    animatorSet.setStartDelay(mDuration * y);
                    animatorSet.start();
                }
            } else {
                for (int y = mViewLists.size() - 1 - 1; y >= 0; y--) {//mIdLists.size()-1是因为最后1个响应动画事件
                    animatorSet = new AnimatorSet();
                    switch (mAimationLists.size()) {//肯定不是空的
                        case 1:
                            if (mAimationLists.get(0).contains("translation")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), (y + 1) * translantionRange, y * translantionRange));
                            }
                            if (mAimationLists.get(0).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), endAlpha, startAlpha));
                            }
                            break;
                        case 2:
                            if (mAimationLists.get(0).contains("translation") && mAimationLists.get(1).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), (y + 1) * translantionRange, y * translantionRange)
                                        , ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(1), endAlpha, startAlpha)
                                );
                            }
                            if (mAimationLists.get(1).contains("translation") && mAimationLists.get(0).contains("alpha")) {
                                animatorSet.playTogether(ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(1), (y + 1) * translantionRange, y * translantionRange)
                                        , ObjectAnimator.ofFloat(mViewLists.get(y), mAimationLists.get(0), endAlpha, startAlpha)
                                );
                            }
                            break;
                    }
                    animatorSet.setDuration(mDuration);
                    animatorSet.setInterpolator(new BounceInterpolator());
                    animatorSet.setStartDelay(mDuration * (mViewLists.size() - 1 - y));
                    animatorSet.start();
                    final int finalY = y;
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                           ObjectAnimator.ofFloat(mViewLists.get(finalY), "translationX", 0f, 0f).start();
                            ObjectAnimator.ofFloat(mViewLists.get(finalY), "translationY", 0f, 0f).start();
                        }
                    });
                }

            }
            isClose = !isClose;
        }
    }

    /**
     * @param animation0 设置动画0
     */
    public void setAnimation0(String animation0) {
        this.animation0 = animation0;
    }

    /**
     * @param animation1 设置动画1
     */
    public void setAnimation1(String animation1) {
        this.animation1 = animation1;
    }

    /**
     * @param duration 设置动画时长
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * @param range 设置平移动画的距离
     */
    public void setTranslantionRange(float range) {
        translantionRange = range;
    }


    /**
     * @param startAlpha 设置开始的透明度
     */
    public void setStartAlpha(int startAlpha) {
        this.startAlpha = startAlpha;
    }


    /**
     * @param endAlpha 设置结束的透明度
     */
    public void setEndAlpha(int endAlpha) {
        this.endAlpha = endAlpha;
    }
}
