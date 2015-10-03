package com.andy.music.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.andy.music.R;

/**
 * 自定义本地音乐中的 ViewPager 的指示器
 * Created by Andy on 2015/4/21.
 */
public class IndicatorView extends View {

    private String title = "";
    private RectF iconRect;
    private Rect titleRect;
    private Paint titlePaint;
    private int color;
    private int focusedColor;
    private float titleSize;
    private float alpha;

    private float defaultTitleSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.IndicatorView_color:
                    color = ta.getColor(attr, Color.parseColor("#333333"));
                    break;
                case R.styleable.IndicatorView_focusedColor:
                    focusedColor = ta.getColor(attr, Color.parseColor("#eeeeee"));
                    break;
                case R.styleable.IndicatorView_title:
                    title = ta.getString(attr);
                    break;
                case R.styleable.IndicatorView_titleSize:
                    titleSize = ta.getDimension(attr, defaultTitleSize);
                    break;
                default:
                    break;
            }
        }
        titleRect = new Rect();
        titlePaint = new Paint();
        titlePaint.setTextSize(defaultTitleSize);
        titlePaint.getTextBounds(title, 0, title.length(), titleRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int a = (int) Math.ceil(alpha * 255);
//        drawIcon(canvas, a);
        drawSourceTitle(canvas, a);
        drawColorTitle(canvas, a);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        float iconWidth = width*0.5f;
        float iconHeight  = height*0.8f;
        float left = (width - iconWidth)*0.5f;
        float top = (height - iconHeight)*0.5f;
        iconRect = new RectF(left, top, left + iconWidth, top + iconHeight);
    }

    public void setIconAlpha(float alpha) {
        this.alpha = alpha;
        invalidateView(); // 更新视图
    }

    private void drawTitle(Canvas canvas, int alpha, int color, float titleSize) {
        titlePaint.setColor(color);
        titlePaint.setAlpha(alpha);
        titlePaint.setAntiAlias(true);
        float x = getWidth() / 2 - titleRect.width() / 2;
        float y = getHeight() / 2 + titleRect.height() / 2;
        canvas.drawText(title, x, y, titlePaint);
    }

    private void drawSourceTitle(Canvas canvas, int alpha) {
        drawTitle(canvas, 255 - alpha, color, titleSize);
    }

    private void drawColorTitle(Canvas canvas, int alpha) {
        drawTitle(canvas, alpha, focusedColor, titleSize);
    }

    private void drawIcon(Canvas canvas, int alpha) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ee729939"));
        paint.setAlpha(alpha);
        canvas.drawRoundRect(iconRect, iconRect.height() / 2, iconRect.width() / 2, paint);
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
