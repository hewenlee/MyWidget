package hewenle.com.mywidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

/**
 * Created by hewenle on 2018/12/12 14:52
 * 倒计时控件
 */
public class CountDownView extends AppCompatImageView {

    private RectF mArcRectF;
    private RectF mTextRectF;
    private Integer animatedValue = 0;//这个是设置的倒计时时间
    private Paint mArcPaint;//画圆弧的画笔
    private Paint mTextPaint;//字体的画笔
    private Paint mBackPaint;//背景色的画笔
    private int mTextSize;
    private String defaultText = "发送验证码";
    private int time = 40;
    private String timeText = time + "";
    private Context mContext;
    private ValueAnimator valueAnimator;

    /**
     * @param time 倒计时时间 单位秒
     */
    public void setTime(int time) {
        this.time = time;
    }


    /**
     * @param defaultText 倒计时开始和结束后显示的文字
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
        mArcRectF = new RectF(0, 0, 0, 0);
        mTextRectF = new RectF(0, 0, 0, 0);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatedValue != 0) {
                    Toast.makeText(context, "请倒计时结束后再执行", Toast.LENGTH_SHORT).show();
                    return;
                }
                valueAnimator = ValueAnimator.ofInt(0, time);
                valueAnimator.setDuration(time * 1000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (animatedValue != animation.getAnimatedValue()) {
                            animatedValue = (Integer) animation.getAnimatedValue();
                            invalidate();
                        }


                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animatedValue = 0;
                        invalidate();
                    }
                });
                valueAnimator.start();
                if (mInterface != null) {
                    mInterface.setListener();
                }
            }
        });
    }


    /**
     * 初始化画笔 字体画笔  花园画笔 背景色画笔
     */
    private void initPaint() {
        mArcPaint = new Paint();//画弧形的画笔
        mArcPaint.setColor(Color.parseColor("#60CEEA"));
        mArcPaint.setStrokeWidth(dip2px(2));
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG | FAKE_BOLD_TEXT_FLAG);//抗锯齿，高亮显示字体

        mTextPaint = new Paint();//画text的画笔
        mTextSize = dip2px(16);
        mTextPaint.setColor(Color.parseColor("#D9534F"));
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG | FAKE_BOLD_TEXT_FLAG);//抗锯齿，高亮显示字体
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFakeBoldText(false);//默认是加粗的，设置为false是普通字体

        mBackPaint = new Paint();//圆弧背景色的画笔
        mBackPaint.setColor(Color.parseColor("#E7E7E7"));
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeWidth(dip2px(2));
        mBackPaint.setFlags(Paint.ANTI_ALIAS_FLAG | FAKE_BOLD_TEXT_FLAG);//抗锯齿，高亮显示字体

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //这里获取的值是控件在windows上的位置
        left += 4;
        right -= 4;
        top += 4;
        bottom -= 4;
        if ((right - left) > (bottom - top)) {//横向长方形
            mArcRectF.left = (right + left - bottom + top) / 2;
            mArcRectF.right = (right + left - bottom + top) / 2 + (bottom - top);
            mArcRectF.top = top;
            mArcRectF.bottom = bottom;

            mTextRectF.left = left;
            mTextRectF.right = right;
            mTextRectF.top = top;
            mTextRectF.bottom = bottom;
        } else {//竖向长方形
            mArcRectF.left = left;
            mArcRectF.right = right;
            mArcRectF.top = (bottom + top - right + left) / 2;
            mArcRectF.bottom = (bottom + top - right + left) / 2 + (right - left);

            mTextRectF.left = mArcRectF.left;
            mTextRectF.right = mArcRectF.right;
            mTextRectF.top = mArcRectF.top;
            mTextRectF.bottom = mArcRectF.bottom;
        }


    }

    /**
     * canvas.drawArc(mArcRectF, -90, 360, false, mBackPaint);
     * 这里的第二个参数startAngle是指起始角度，sweepAngle是跨越的角度
     * 注意DrawText时候的点是指字体左下角位置
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animatedValue == 0 || animatedValue == time) {
            canvas.drawText(defaultText, mTextRectF.centerX() - (mTextSize * defaultText.length() / 2), mTextRectF.centerY() + (mTextSize / 4), mTextPaint);
        } else {
            timeText = (time - animatedValue) + "";
            canvas.drawArc(mArcRectF, -90, 360, false, mBackPaint);
            canvas.drawText(timeText, mArcRectF.centerX() - (mTextSize * timeText.length() / 4), mArcRectF.centerY() + (mTextSize / 3), mTextPaint);
//            canvas.drawArc(mArcRectF, -90, 360 - time * animatedValue, false, mArcPaint);
            canvas.drawArc(mArcRectF, -90, 360 - 360 / time * animatedValue, false, mArcPaint);
        }

    }

    private int dip2px(int mvalue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (mvalue * density + 0.5f);

    }


    private TimeViewInterface mInterface;

    public void setOnTimeViewListener(TimeViewInterface face) {
        mInterface = face;
    }

    public interface TimeViewInterface {
        void setListener();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.AT_MOST == MeasureSpec.getMode(widthMeasureSpec) ||
                MeasureSpec.AT_MOST == MeasureSpec.getMode(heightMeasureSpec)
                ) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}


