package ysn.com.view.checkview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import ysn.com.view.checkview.bean.CheckVewMode;
import ysn.com.view.checkview.utils.AnimUtils;

/**
 * @Author yangsanning
 * @ClassName CheckView
 * @Description 勾选View
 * @Date 2020/4/24
 */
public class CheckView extends View implements Checkable {

    /**
     * {@link CheckVewMode}
     */
    public int mode;

    /**
     * enableBg: 是否启用背景
     */
    private boolean enableBg;
    private int bgCheckedColor, bgNormalColor;

    /**
     * enableBorder: 是否启用边框
     */
    private boolean enableBorder;
    private int borderNormalColor, borderCheckedColor;

    private int tickColor;
    private int strokeWidth;
    private int corner;
    private int animDuration;

    private Paint bgPaint, tickPaint, borderPaint;
    private Point[] tickPoints;
    private Path tickPath;

    private int width, height;
    private int radius;

    /**
     * drawDistance: 绘制距离
     * leftLineDistance: 左边两点距离
     * rightLineDistance: 右边两点距离
     */
    private float drawDistance, leftTwoPointDistance, rightTwoPointDistance;
    private float radiusScale = 1.0f;

    private boolean isChecked;
    private boolean canTickDraw;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);
        initPaint();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CheckView);

        mode = typedArray.getInt(R.styleable.CheckView_cv_mode, CheckVewMode.CIRCLE.mode);

        enableBg = typedArray.getBoolean(R.styleable.CheckView_cv_enable_bg, Boolean.FALSE);
        bgNormalColor = typedArray.getColor(R.styleable.CheckView_cv_bg_normal_color, getColor(R.color.check_view_bg_normal_color));
        bgCheckedColor = typedArray.getColor(R.styleable.CheckView_cv_bg_checked_color, getColor(R.color.check_view_bg_checked_color));

        enableBorder = typedArray.getBoolean(R.styleable.CheckView_cv_enable_border, Boolean.TRUE);
        borderNormalColor = typedArray.getColor(R.styleable.CheckView_cv_border_normal_color, getColor(R.color.check_view_border_normal_color));
        borderCheckedColor = typedArray.getColor(R.styleable.CheckView_cv_border_checked_color, getColor(R.color.check_view_border_checked_color));

        tickColor = typedArray.getColor(R.styleable.CheckView_cv_tick_color, getColor(R.color.check_view_tick_color));
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.CheckView_cv_stroke_width, 5);
        corner = typedArray.getDimensionPixelSize(R.styleable.CheckView_cv_corner, 5);
        animDuration = typedArray.getInt(R.styleable.CheckView_cv_duration, 150);

        typedArray.recycle();
    }

    private int getColor(int colorRes) {
        return getContext().getResources().getColor(colorRes);
    }

    private void initPaint() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
        borderPaint.setStrokeWidth(strokeWidth);

        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);
        tickPaint.setColor(tickColor);
        tickPaint.setStrokeWidth(strokeWidth);
        tickPath = new Path();
        tickPoints = new Point[3];
        tickPoints[0] = new Point();
        tickPoints[1] = new Point();
        tickPoints[2] = new Point();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        // 因圆点在中间, 故这里取最短边/2
        radius = (Math.min(width, height) - strokeWidth * 2) / 2;

        // 看不懂？自己画图吧
        tickPoints[0].x = -Math.round((float) 10 * radius / 16);
        tickPoints[0].y = Math.round((float) 1 * radius / 16);
        tickPoints[1].x = -Math.round((float) 3 * radius / 16);
        tickPoints[1].y = Math.round((float) 7 * radius / 16);
        tickPoints[2].x = Math.round((float) 9 * radius / 16);
        tickPoints[2].y = -Math.round((float) 8 * radius / 16);

        // 左边两点距离
        leftTwoPointDistance = (float) Math.sqrt(
                Math.pow(tickPoints[1].x - tickPoints[0].x, 2) + Math.pow(tickPoints[1].y - tickPoints[0].y, 2));
        // 右边两点距离
        rightTwoPointDistance = (float) Math.sqrt(
                Math.pow(tickPoints[2].x - tickPoints[1].x, 2) + Math.pow(tickPoints[2].y - tickPoints[1].y, 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate((width >> 1), (height >> 1));

        float drawRadius = radius * radiusScale;

        // 绘制背景
        drawBg(canvas, drawRadius);

        // 绘制边框
        drawBorder(canvas, drawRadius);

        // 绘制打勾
        drawTick(canvas);

        canvas.restore();
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas, float drawRadius) {
        if (enableBg) {
            bgPaint.setColor(isChecked() ? bgCheckedColor : bgNormalColor);
            if (CheckVewMode.CIRCLE.isCircle(mode)) {
                canvas.drawCircle(0, 0, drawRadius, bgPaint);
            } else {
                RectF rectf = new RectF(-drawRadius, -drawRadius, drawRadius, drawRadius);
                canvas.drawRoundRect(rectf, strokeWidth, strokeWidth, bgPaint);
            }
        }
    }

    /**
     * 绘制边框
     */
    private void drawBorder(Canvas canvas, float drawRadius) {
        if (enableBorder) {
            borderPaint.setColor(isChecked() ? borderCheckedColor : borderNormalColor);
            if (CheckVewMode.CIRCLE.isCircle(mode)) {
                canvas.drawCircle(0, 0, drawRadius, borderPaint);
            } else {
                RectF rectf = new RectF(-drawRadius, -drawRadius, drawRadius, drawRadius);
                canvas.drawRoundRect(rectf, corner, corner, borderPaint);
            }
        }
    }

    /**
     * 绘制打勾
     */
    private void drawTick(Canvas canvas) {
        if (canTickDraw && isChecked()) {
            tickPath.reset();
            // 绘制打勾的左边
            if (drawDistance < leftTwoPointDistance) {
                float step = (width / 20.0f) < 3 ? 3 : (width / 20.0f);
                drawDistance += step;
                float stopX = tickPoints[0].x + ((tickPoints[1].x - tickPoints[0].x)
                        * drawDistance / leftTwoPointDistance);
                float stopY = tickPoints[0].y + ((tickPoints[1].y - tickPoints[0].y)
                        * drawDistance / leftTwoPointDistance);

                tickPath.moveTo(tickPoints[0].x, tickPoints[0].y);
                tickPath.lineTo(stopX, stopY);
                canvas.drawPath(tickPath, tickPaint);

                if (drawDistance > leftTwoPointDistance) {
                    drawDistance = leftTwoPointDistance;
                }
            } else {
                // 绘制打勾的右边
                tickPath.moveTo(tickPoints[0].x, tickPoints[0].y);
                tickPath.lineTo(tickPoints[1].x, tickPoints[1].y);
                canvas.drawPath(tickPath, tickPaint);

                if (drawDistance < leftTwoPointDistance + rightTwoPointDistance) {
                    float stopX = tickPoints[1].x + (tickPoints[2].x - tickPoints[1].x)
                            * (drawDistance - leftTwoPointDistance) / rightTwoPointDistance;
                    float stopY = tickPoints[1].y - (tickPoints[1].y - tickPoints[2].y)
                            * (drawDistance - leftTwoPointDistance) / rightTwoPointDistance;

                    tickPath.reset();
                    tickPath.moveTo(tickPoints[1].x, tickPoints[1].y);
                    tickPath.lineTo(stopX, stopY);
                    canvas.drawPath(tickPath, tickPaint);

                    float step = Math.max((width / 20), 3);
                    drawDistance += step;
                } else {
                    tickPath.reset();
                    tickPath.moveTo(tickPoints[1].x, tickPoints[1].y);
                    tickPath.lineTo(tickPoints[2].x, tickPoints[2].y);
                    canvas.drawPath(tickPath, tickPaint);
                }
            }

            // 重绘
            if (drawDistance < (leftTwoPointDistance + rightTwoPointDistance)) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postInvalidate();
                    }
                }, 10);
            }
        }
    }

    private void startCheckedAnimation() {
        AnimUtils.ofFloat(animDuration, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radiusScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        }, 1.0f, 0.8f, 1.0f);

        // 延迟绘制打勾
        postDelayed(new Runnable() {
            @Override
            public void run() {
                canTickDraw = true;
                postInvalidate();
            }
        }, animDuration);
    }

    private void startNormalAnimation() {
        if (bgNormalColor == Color.TRANSPARENT) {
            return;
        }
        AnimUtils.ofFloat(animDuration, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radiusScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        }, 1.0f, 0.8f, 1.0f);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        this.setChecked(!isChecked());
    }

    public void toggle(boolean animate) {
        this.setChecked(!isChecked(), animate);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        canTickDraw = true;
        radiusScale = 1.0f;
        drawDistance = isChecked() ? (leftTwoPointDistance + rightTwoPointDistance) : 0;
        invalidate();
    }

    /**
     * @param enableAnimate 开启动画
     */
    public void setChecked(boolean checked, boolean enableAnimate) {
        if (enableAnimate) {
            canTickDraw = false;
            isChecked = checked;
            drawDistance = 0f;
            if (checked) {
                startCheckedAnimation();
            } else {
                startNormalAnimation();
            }
        } else {
            this.setChecked(checked);
        }
    }
}
