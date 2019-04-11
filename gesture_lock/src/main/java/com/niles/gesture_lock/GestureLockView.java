package com.niles.gesture_lock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niles
 * Date 2019/4/10 14:31
 * Email niulinguo@163.com
 */
public class GestureLockView extends View {

    // 选中的数字组合
    private final List<Byte> mSelectNumberList = new ArrayList<>();

    // 圆半径
    private int mCircleRadius;
    // 圆边粗细
    private int mCircleBorderSize;
    // 圆边颜色
    private ColorStateList mCircleBorderColor;
    // 圆颜色
    private ColorStateList mCircleColor;

    // 圆心圆半径
    private int mCircleCenterRadius;
    // 圆心圆颜色
    private ColorStateList mCircleCenterColor;

    // 线粗细
    private int mLineSize;
    // 线颜色
    private ColorStateList mLineColor;

    // 两个相邻圆心距离
    private int mCircleDistance;

    private Paint mDrawPaint;
    private Point mFingerPoint;
    private GestureLockListener mGestureLockListener;
    private boolean mIsAlive = true;
    private boolean mTouchable = true;

    public GestureLockView(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GestureLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setGestureLockListener(GestureLockListener gestureLockListener) {
        mGestureLockListener = gestureLockListener;
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        Resources resources = getResources();

        mCircleRadius = resources.getDimensionPixelSize(R.dimen.nlg_gesture_lock_circle_radius);
        mCircleBorderSize = resources.getDimensionPixelSize(R.dimen.nlg_gesture_lock_circle_border_size);
        mCircleBorderColor = resources.getColorStateList(R.color.nlg_gesture_lock_circle_border_color);
        mCircleColor = resources.getColorStateList(R.color.nlg_gesture_lock_circle_color);

        mCircleCenterRadius = mCircleRadius / 4;
        mCircleCenterColor = resources.getColorStateList(R.color.nlg_gesture_lock_circle_center_color);

        mLineSize = resources.getDimensionPixelSize(R.dimen.nlg_gesture_lock_line_size);
        mLineColor = resources.getColorStateList(R.color.nlg_gesture_lock_line_color);

        mCircleDistance = mCircleRadius * 3;

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockView, defStyleAttr, defStyleAttr);

            mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.GestureLockView_nlg_circle_radius, mCircleRadius);
            mCircleBorderSize = typedArray.getDimensionPixelSize(R.styleable.GestureLockView_nlg_circle_border_size, mCircleBorderSize);
            ColorStateList circleBorderColor = typedArray.getColorStateList(R.styleable.GestureLockView_nlg_circle_border_color);
            if (circleBorderColor != null) {
                mCircleBorderColor = circleBorderColor;
            }
            ColorStateList circleColor = typedArray.getColorStateList(R.styleable.GestureLockView_nlg_circle_color);
            if (circleColor != null) {
                mCircleColor = circleColor;
            }

            mCircleCenterRadius = typedArray.getDimensionPixelSize(R.styleable.GestureLockView_nlg_circle_center_radius, mCircleCenterRadius);
            ColorStateList circleCenterColor = typedArray.getColorStateList(R.styleable.GestureLockView_nlg_circle_center_color);
            if (circleCenterColor != null) {
                mCircleCenterColor = circleCenterColor;
            }

            mLineSize = typedArray.getDimensionPixelSize(R.styleable.GestureLockView_nlg_line_size, mLineSize);
            ColorStateList lineColor = typedArray.getColorStateList(R.styleable.GestureLockView_nlg_line_color);
            if (lineColor != null) {
                mLineColor = lineColor;
            }

            mCircleDistance = typedArray.getDimensionPixelSize(R.styleable.GestureLockView_nlg_circle_distance, mCircleDistance);

            typedArray.recycle();
        }

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);

    }

    public void initNumberList(byte[] numbers) {
        mSelectNumberList.clear();
        for (byte number : numbers) {
            mSelectNumberList.add(number);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredWidth, measuredHeight;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            if (getPaddingLeft() == 0 && getPaddingRight() == 0) {
                int padding = widthSize / 10;
                setPadding(padding, getPaddingTop(), padding, getPaddingBottom());
            }

            if (getPaddingTop() == 0 && getPaddingBottom() == 0) {
                int padding = widthSize / 10;
                setPadding(getPaddingLeft(), padding, getPaddingRight(), padding);
            }

            int contentWidth = widthSize - (getPaddingLeft() + getPaddingRight());

            measuredWidth = contentWidth + (getPaddingLeft() + getPaddingRight());
            measuredHeight = contentWidth + (getPaddingTop() + getPaddingBottom());

            mCircleRadius = contentWidth / 8;
            mCircleCenterRadius = mCircleRadius / 4;
            mCircleDistance = mCircleRadius * 3;
        } else {
            measuredWidth = getPaddingLeft() + getPaddingRight() + 2 * mCircleDistance + 2 * mCircleRadius + mCircleBorderSize;
            measuredHeight = getPaddingTop() + getPaddingBottom() + 2 * mCircleDistance + 2 * mCircleRadius + mCircleBorderSize;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 设置是否可触控
     */
    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (byte i = 0; i < 9; i++) {

            int row = i / 3;
            int col = i % 3;

            float cx = getPaddingLeft() + mCircleRadius + mCircleDistance * col;
            float cy = getPaddingTop() + mCircleRadius + mCircleDistance * row;

            {
                // 设置画笔
                mDrawPaint.setStrokeWidth(mCircleBorderSize);
                mDrawPaint.setStyle(Paint.Style.STROKE);
                if (mSelectNumberList.contains(i)) {
                    if (isEnabled()) {
                        mDrawPaint.setColor(mCircleBorderColor.getColorForState(View.ENABLED_SELECTED_STATE_SET, mCircleBorderColor.getDefaultColor()));
                    } else {
                        mDrawPaint.setColor(mCircleBorderColor.getColorForState(View.SELECTED_STATE_SET, mCircleBorderColor.getDefaultColor()));
                    }
                } else {
                    mDrawPaint.setColor(mCircleBorderColor.getDefaultColor());
                }

                // 画外圆环
                canvas.drawCircle(cx, cy, mCircleRadius, mDrawPaint);
            }

            {
                // 设置画笔
                mDrawPaint.setStrokeWidth(0);
                mDrawPaint.setStyle(Paint.Style.FILL);
                if (mSelectNumberList.contains(i)) {
                    if (isEnabled()) {
                        mDrawPaint.setColor(mCircleColor.getColorForState(View.ENABLED_SELECTED_STATE_SET, mCircleColor.getDefaultColor()));
                    } else {
                        mDrawPaint.setColor(mCircleColor.getColorForState(View.SELECTED_STATE_SET, mCircleColor.getDefaultColor()));
                    }
                } else {
                    mDrawPaint.setColor(mCircleColor.getDefaultColor());
                }

                // 填充外圆
                canvas.drawCircle(cx, cy, mCircleRadius, mDrawPaint);
            }

            {
                // 设置画笔
                mDrawPaint.setStrokeWidth(0);
                mDrawPaint.setStyle(Paint.Style.FILL);
                if (mSelectNumberList.contains(i)) {
                    if (isEnabled()) {
                        mDrawPaint.setColor(mCircleCenterColor.getColorForState(View.ENABLED_SELECTED_STATE_SET, mCircleCenterColor.getDefaultColor()));
                    } else {
                        mDrawPaint.setColor(mCircleCenterColor.getColorForState(View.SELECTED_STATE_SET, mCircleCenterColor.getDefaultColor()));
                    }
                } else {
                    mDrawPaint.setColor(mCircleCenterColor.getDefaultColor());
                }

                // 画圆心圆
                canvas.drawCircle(cx, cy, mCircleCenterRadius, mDrawPaint);
            }
        }

        int selectNumberCount = mSelectNumberList.size();
        if (selectNumberCount > 1) {

            for (int i = 1; i < selectNumberCount; i++) {
                int startNumber = mSelectNumberList.get(i - 1);
                int endNumber = mSelectNumberList.get(i);

                int startRow = startNumber / 3;
                int startCol = startNumber % 3;

                int endRow = endNumber / 3;
                int endCol = endNumber % 3;

                int startX = getPaddingLeft() + mCircleRadius + mCircleDistance * startCol;
                int startY = getPaddingTop() + mCircleRadius + mCircleDistance * startRow;

                int endX = getPaddingLeft() + mCircleRadius + mCircleDistance * endCol;
                int endY = getPaddingTop() + mCircleRadius + mCircleDistance * endRow;

                // 设置画笔
                mDrawPaint.setStrokeWidth(mLineSize);
                mDrawPaint.setStyle(Paint.Style.FILL);
                if (isEnabled()) {
                    mDrawPaint.setColor(mLineColor.getColorForState(View.ENABLED_STATE_SET, mLineColor.getDefaultColor()));
                } else {
                    mDrawPaint.setColor(mLineColor.getDefaultColor());
                }

                // 画线
                canvas.drawLine(startX, startY, endX, endY, mDrawPaint);
            }
        }

        if (mFingerPoint != null && selectNumberCount > 0) {

            int number = mSelectNumberList.get(selectNumberCount - 1);

            int row = number / 3;
            int col = number % 3;

            float cx = getPaddingLeft() + mCircleRadius + mCircleDistance * col;
            float cy = getPaddingTop() + mCircleRadius + mCircleDistance * row;

            mDrawPaint.setStrokeWidth(mLineSize);
            mDrawPaint.setStyle(Paint.Style.FILL);
            if (isEnabled()) {
                mDrawPaint.setColor(mLineColor.getColorForState(View.ENABLED_STATE_SET, mLineColor.getDefaultColor()));
            } else {
                mDrawPaint.setColor(mLineColor.getDefaultColor());
            }

            // 最后一条线
            canvas.drawLine(cx, cy, mFingerPoint.getX(), mFingerPoint.getY(), mDrawPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mTouchable) {
            // 不可触控，不相应TouchEvent
            return false;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {

                Byte nearPoint;

                if (mFingerPoint == null) {
                    reset();

                    mFingerPoint = new Point(event.getX(), event.getY());

                    // 计算手指点的位置
                    nearPoint = computerNearPoint(mFingerPoint);

                    if (nearPoint == null) {
                        // 如果手指没有点到圆上，不相应手势
                        return false;
                    } else {
                        // 手势开始
                        if (mGestureLockListener != null) {
                            mGestureLockListener.onGestureStart(this);
                        }
                    }
                } else {
                    mFingerPoint.update(event.getX(), event.getY());

                    // 计算手指是否在某点附近
                    nearPoint = computerNearPoint(mFingerPoint);
                }

                if (nearPoint != null && !mSelectNumberList.contains(nearPoint)) {
                    // 将点添加进路径中
                    addNumberToList(nearPoint);
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                // 手指抬起
                mFingerPoint = null;

                if (mGestureLockListener != null) {
                    // 手势结束，回调结果
                    mGestureLockListener.onGestureFinish(this, mSelectNumberList.toArray(new Byte[]{}));
                }
                break;
            }
        }

        invalidate();
        return true;
    }

    /**
     * 设置是否错误
     * 错误时会显示错误样式
     */
    public void setError(boolean error) {
        setEnabled(!error);
    }

    /**
     * 重置
     */
    public void reset() {
        mSelectNumberList.clear();
        mFingerPoint = null;
        setEnabled(true);
        invalidate();
    }

    /**
     * 将数字加入列表中
     * <p>
     * 如果最后一个数字与要加入的数字之间有个中间数字，那么将中间数字加入列表
     */
    private void addNumberToList(byte currNumber) {
//        if (!mSelectNumberList.isEmpty()) {
//            byte lastNumber = mSelectNumberList.get(mSelectNumberList.size() - 1);
//
//            int lastRow = lastNumber / 3;
//            int lastCol = lastNumber % 3;
//
//            int currRow = currNumber / 3;
//            int currCol = currNumber % 3;
//
//            // 判断是否有中间点
//            if ((lastCol + currCol) % 2 == 0 && (lastRow + currRow) % 2 == 0) {
//                int centerCol = (lastCol + currCol) / 2;
//                int centerRow = (lastRow + currRow) / 2;
//
//                byte centerNumber = (byte) (centerRow * 3 + centerCol);
//
//                // 如果中间点不在路径中，先将中间点加入路径
//                if (!mSelectNumberList.contains(centerNumber)) {
//                    mSelectNumberList.add(centerNumber);
//                }
//            }
//        }

        mSelectNumberList.add(currNumber);
    }

    /**
     * 计算手指点的地方距离哪一个点更近
     */
    private Byte computerNearPoint(Point point) {
        for (byte i = 0; i < 9; i++) {

            int row = i / 3;
            int col = i % 3;

            float cx = getPaddingLeft() + mCircleRadius + mCircleDistance * col;
            float cy = getPaddingTop() + mCircleRadius + mCircleDistance * row;

            // 距离近的标准：圆半径的0.8倍以内
            if (point.distance(cx, cy) < mCircleRadius * 0.8) {
                return i;
            }

        }

        return null;
    }

    /**
     * 用于判断View是否被销毁
     */
    public boolean isAlive() {
        return mIsAlive;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mIsAlive = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mIsAlive = true;
    }
}
