package com.zhuyong.doublelinechatview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by zhuyong on 2017/10/12.
 */

public class DoubleLineChatView extends View {

    private static final String TAG = "DoubleLineChatView";
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 左边柱状图画笔
     */
    private Paint mPaintLeft;
    /**
     * 右边柱状图画笔
     */
    private Paint mPaintRight;
    /**
     * 左边柱状图文字画笔
     */
    private Paint mPaintTextLeft;
    /**
     * 右边柱状图文字画笔
     */
    private Paint mPaintTextRight;
    /**
     * XY轴画笔
     */
    private Paint mPaintTextXY;

    /**
     * 左边柱状图数据
     */
    private int[] mDataLeft = {0, 0, 0, 0};
    /**
     * 右边柱状图数据
     */
    private int[] mDataRight = {0, 0, 0, 0};
    /**
     * X轴底部文字
     */
    private String[] mDataTextX = {"", "", "", ""};

    /**
     * Y轴文字宽度
     */
    private float mYDistance;
    /**
     * X轴底部高度
     */
    private float mXDistance;
    /**
     * 柱状图宽度
     */
    private float mLineWidth;
    /**
     * 颜色字体等变量
     */
    private int mLeftLineBackGroundColor = Color.RED;
    private int mRightLineBackGroundColor = Color.BLUE;
    private int mLeftLineTextColor = Color.RED;
    private int mRightLineTextColor = Color.BLUE;
    private float mLineTextSize;
    private int mLineXYColor = Color.BLACK;
    private float mLineXYSize;
    /**
     * 是否显示XY轴箭头
     */
    private boolean mIsShowArrow = true;
    /**
     * 是否显示Y轴间隔标记
     */
    private boolean mIsShowArrowYInterval = true;
    /**
     * 动画时长,默认1秒
     */
    private int mAnimationDuration = 1000;
    /**
     * View宽度
     */
    private int mViewWidth;
    /**
     * View高度
     */
    private int mViewHeight;
    /**
     * 柱状图的Y轴最大数据，等于或者大于两个数组中的最大值
     */
    private int mMaxData;
    /**
     * 两组柱状图之间的距离（偏大距离）
     */
    private float mBigDistance;
    /**
     * 两个柱状图之间的距离（偏小距离）
     */
    private float mSmallDistance;

    public DoubleLineChatView(Context context) {
        this(context, null);
    }

    public DoubleLineChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleLineChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DoubleLineChatView);
        //柱状图的宽度
        mLineWidth = array.getDimension(R.styleable.DoubleLineChatView_chatview_line_width, dip2px(mContext, 30));
        //左边柱状图的背景色
        mLeftLineBackGroundColor = array.getColor(R.styleable.DoubleLineChatView_chatview_left_background_color, mLeftLineBackGroundColor);
        //右边柱状图的背景色
        mRightLineBackGroundColor = array.getColor(R.styleable.DoubleLineChatView_chatview_right_background_color, mRightLineBackGroundColor);
        //左边柱状图的数据字体颜色
        mLeftLineTextColor = array.getColor(R.styleable.DoubleLineChatView_chatview_left_text_data_color, mLeftLineTextColor);
        //右边柱状图的数据字体颜色
        mRightLineTextColor = array.getColor(R.styleable.DoubleLineChatView_chatview_right_text_data_color, mRightLineTextColor);
        //柱状图的数据字体大小
        mLineTextSize = array.getDimension(R.styleable.DoubleLineChatView_chatview_text_data_size, sp2px(mContext, 14));
        //XY轴的颜色以及字体颜色
        mLineXYColor = array.getColor(R.styleable.DoubleLineChatView_chatview_text_xy_color, mLineXYColor);
        //XY轴的字体大小
        mLineXYSize = array.getDimension(R.styleable.DoubleLineChatView_chatview_text_xy_size, sp2px(mContext, 14));
        //两组柱状图之间的距离（偏大距离）
        mBigDistance = array.getDimension(R.styleable.DoubleLineChatView_chatview_line_big_distance, dip2px(mContext, 20));
        //两个柱状图之间的距离（偏小距离）
        mSmallDistance = array.getDimension(R.styleable.DoubleLineChatView_chatview_line_small_distance, dip2px(mContext, 5));
        //是否显示XY轴的箭头
        mIsShowArrow = array.getBoolean(R.styleable.DoubleLineChatView_chatview_show_arrow, true);
        //是否显示Y轴的数据间隔标志
        mIsShowArrowYInterval = array.getBoolean(R.styleable.DoubleLineChatView_chatview_show_y_interval, true);
        //柱状图生长动画时间，默认1秒
        mAnimationDuration = array.getInteger(R.styleable.DoubleLineChatView_chatview_animation_duration, 1000);
        //Y轴数据的宽度，也就是Y轴距离左边的宽度，这个要根据数据字符串的长度进行调整
        mYDistance = array.getDimension(R.styleable.DoubleLineChatView_chatview_y_distance, dip2px(mContext, 40));
        //X轴数据的高度，也就是X轴距离底部的距离
        mXDistance = array.getDimension(R.styleable.DoubleLineChatView_chatview_x_distance, dip2px(mContext, 40));
        array.recycle();
        initView();
    }

    /**
     * 初始化画笔
     */
    private void initView() {
        //左边柱状图
        mPaintLeft = new Paint();
        mPaintLeft.setColor(mLeftLineBackGroundColor);
        mPaintLeft.setStrokeWidth(mLineWidth);
        mPaintLeft.setAntiAlias(true);

        //右边柱状图
        mPaintRight = new Paint();
        mPaintRight.setColor(mRightLineBackGroundColor);
        mPaintRight.setStrokeWidth(mLineWidth);
        mPaintRight.setAntiAlias(true);

        //左边柱状图字体数据
        mPaintTextLeft = new Paint();
        mPaintTextLeft.setColor(mLeftLineTextColor);
        mPaintTextLeft.setTextSize(mLineTextSize);
        mPaintTextLeft.setAntiAlias(true);

        //右边柱状图字体数据
        mPaintTextRight = new Paint();
        mPaintTextRight.setColor(mRightLineTextColor);
        mPaintTextRight.setTextSize(mLineTextSize);
        mPaintTextRight.setAntiAlias(true);

        //XY轴
        mPaintTextXY = new Paint();
        mPaintTextXY.setStrokeWidth(3);
        mPaintTextXY.setColor(mLineXYColor);
        mPaintTextXY.setTextSize(mLineXYSize);
        mPaintTextXY.setAntiAlias(true);
    }

    /**
     * 数据
     *
     * @param dataLeft  左边柱状图数据
     * @param dataRight 右边柱状图数据
     * @param dataTextX X轴文字数组
     */
    public void setData(int[] dataLeft, int[] dataRight, String[] dataTextX) {
        this.mDataLeft = dataLeft;
        this.mDataRight = dataRight;
        this.mDataTextX = dataTextX;
        //找出两个数组中的最大值
        int maxLeft = getMax(mDataLeft);
        int maxRight = getMax(mDataRight);
        mMaxData = maxLeft > maxRight ? maxLeft : maxRight;
        Log.i(TAG, "mMaxCount=" + mMaxData);
        //计算Y轴坐标最大值，根据当前最大数据并且是5的整倍数计算
        while (mMaxData % 5 != 0) {
            mMaxData++;
        }
        Log.i(TAG, "mMaxCount=" + mMaxData);
    }

    /**
     * 找出数组中的最大值
     *
     * @param arr 目标数组
     * @return 最大值
     */
    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setDimension(heightMeasureSpec);
    }

    /**
     * 重新赋予宽高
     */
    private void setDimension(int heightMeasureSpec) {
        //
        mViewWidth = (int) (mYDistance + mBigDistance + (mDataLeft.length * (mLineWidth * 2 + mBigDistance + mSmallDistance)));
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "mViewWidth=" + mViewWidth + "px");
        Log.i(TAG, "mViewHeight=" + mViewHeight + "px");
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制柱状图
         */
        drawLineData(canvas);
        /**
         * 绘制坐标轴
         */
        drawLineXY(canvas);
        /**
         * 绘制X坐标值
         */
        drawLineX(canvas);
        /**
         * 绘制Y坐标值
         */
        drawLineY(canvas);
    }

    /**
     * 绘制X坐标值
     */
    private void drawLineX(Canvas canvas) {
        for (int i = 0; i < mDataTextX.length; i++) {
            //绘制进度数字
            String text = mDataTextX[i];
            //获取文字宽度
            float textWidth = mPaintTextXY.measureText(text, 0, text.length());
            float dx = (mYDistance + mBigDistance + mSmallDistance / 2 + mLineWidth + (i * (mBigDistance + mSmallDistance + 2 * mLineWidth))) - textWidth / 2;
            Paint.FontMetricsInt fontMetricsInt = mPaintTextXY.getFontMetricsInt();
            float dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            float baseLine = mViewHeight - mXDistance / 2 + dy;
            canvas.drawText(text, dx, baseLine, mPaintTextXY);
        }
    }

    /**
     * 绘制Y坐标值
     * 这里的坐标值是根据最大值计算出来对应的间隔，然后从0显示出6个数据
     */
    private void drawLineY(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            //绘制进度数字
            String text = (mMaxData / 5 * i) + "";
            //获取文字宽度
            float textWidth = mPaintTextXY.measureText(text, 0, text.length());
            float dx = mYDistance / 2 - textWidth / 2;
            Paint.FontMetricsInt fontMetricsInt = mPaintTextXY.getFontMetricsInt();
            float dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            float baseLine = (mViewHeight - mXDistance) - (i * (mViewHeight - 2 * mXDistance) / 5) + dy;
            canvas.drawText(text, dx, baseLine, mPaintTextXY);
            if (mIsShowArrowYInterval) canvas.drawLine(mYDistance, (mViewHeight - mXDistance)
                            - (i * (mViewHeight - 2 * mXDistance) / 5), mYDistance + 10,
                    (mViewHeight - mXDistance) - (i * (mViewHeight - 2 * mXDistance) / 5), mPaintTextXY);
        }
    }

    /**
     * 绘制坐标轴
     */
    private void drawLineXY(Canvas canvas) {
        canvas.drawLine(mYDistance, mViewHeight - mXDistance, mYDistance, 15, mPaintTextXY);
        canvas.drawLine(mYDistance, mViewHeight - mXDistance, mViewWidth - 15, mViewHeight - mXDistance, mPaintTextXY);

        if (mIsShowArrow) {
            //Y轴箭头
            canvas.drawLine(mYDistance, 15, mYDistance - 10, 25, mPaintTextXY);
            canvas.drawLine(mYDistance, 15, mYDistance + 10, 25, mPaintTextXY);
            //X轴箭头
            canvas.drawLine(mViewWidth - 15, mViewHeight - mXDistance, mViewWidth - 25, mViewHeight - mXDistance - 10, mPaintTextXY);
            canvas.drawLine(mViewWidth - 15, mViewHeight - mXDistance, mViewWidth - 25, mViewHeight - mXDistance + 10, mPaintTextXY);

        }
    }

    /**
     * 绘制柱状图
     */
    private void drawLineData(Canvas canvas) {
        for (int i = 0; i < mDataLeft.length; i++) {
            float startX = mYDistance + mBigDistance + mLineWidth / 2 + (i * (mBigDistance + mSmallDistance + 2 * mLineWidth));
            float endY = (mViewHeight - mXDistance) - (mDataLeft[i] * (mViewHeight - 2 * mXDistance)) / mMaxData;
            canvas.drawLine(startX, mViewHeight - mXDistance, startX, endY, mPaintLeft);
            String text = mDataLeft[i] + "";
            float textWidth = mPaintTextLeft.measureText(text, 0, text.length());
            canvas.drawText(text, startX - textWidth / 2, endY - 15, mPaintTextLeft);
        }

        for (int i = 0; i < mDataRight.length; i++) {
            float startX = mYDistance + mBigDistance + mSmallDistance + mLineWidth + mLineWidth / 2 + (i * (mBigDistance + mSmallDistance + 2 * mLineWidth));
            float endY = ((mViewHeight - mXDistance)) - (mDataRight[i] * (mViewHeight - 2 * mXDistance)) / mMaxData;
            canvas.drawLine(startX, mViewHeight - mXDistance, startX, endY, mPaintRight);
            String text = mDataRight[i] + "";
            float textWidth = mPaintTextRight.measureText(text, 0, text.length());
            canvas.drawText(text, startX - textWidth / 2, endY - 15, mPaintTextRight);
        }
    }

    /**
     * 开启动画，并且绘制图表
     */
    public void start() {
        AnimatorSet set = new AnimatorSet();
        for (int i = 0; i < mDataLeft.length; i++) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mDataLeft[i]);
            final int finalI = i;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mDataLeft[finalI] = (int) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            set.playTogether(animator);
        }

        for (int i = 0; i < mDataRight.length; i++) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mDataRight[i]);
            final int finalI = i;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mDataRight[finalI] = (int) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            set.playTogether(animator);
        }
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.start();
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * dp转px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
