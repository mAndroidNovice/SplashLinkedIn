package com.weishen.splashoflinkedin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


public class ColorTrackView extends View {

    public int mTextStartX;
    public int mTextStartY;

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM;
    }

    private int mDirection = DIRECTION_LEFT;

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_TOP = 2;
    public static final int DIRECTION_BOTTOM = 3;

    public void setDirection(int direction) {
        mDirection = direction;
        invalidate();
    }

    public int getDirection() {
        return mDirection;
    }

    private String mText = "张鸿洋";
    private Paint textPaint;
    private Paint borderPaint;

    public int getmBorderOriginColor() {
        return mBorderOriginColor;
    }

    public void setmBorderOriginColor(int mBorderOriginColor) {
        this.mBorderOriginColor = mBorderOriginColor;
        invalidate();
    }

    public int getmBorderChangeColor() {
        return mBorderChangeColor;
    }

    public void setmBorderChangeColor(int mBorderChangeColor) {
        this.mBorderChangeColor = mBorderChangeColor;
        invalidate();
    }

    private int mTextSize = sp2px(30);

    private int mTextOriginColor = 0xff000000;
    private int mTextChangeColor = 0xffff0000;
    private int mBgOriginColor = 0xFF857979;
    private int mBgChangeColor = 0xFFAE0505;
    private int mBorderOriginColor = 0xffffffff;
    private int mBorderChangeColor = 0x00000000;

    private Rect mTextBound = new Rect();
    public int mTextWidth;
    public int mTextHeight;

    private float mProgress;
    private float bgProgress;

    public float getBgProgress() {
        return bgProgress;
    }

    public void setBgProgress(float bgProgress) {
        this.bgProgress = bgProgress;
        invalidate();
    }

    public ColorTrackView(Context context) {
        super(context, null);
    }

    public ColorTrackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Style.STROKE);
        borderPaint.setStrokeWidth(3);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ColorTrackView);
        mText = ta.getString(R.styleable.ColorTrackView_text);
        mTextSize = ta.getDimensionPixelSize(
                R.styleable.ColorTrackView_text_size, mTextSize);
        mTextOriginColor = ta.getColor(
                R.styleable.ColorTrackView_text_origin_color, mTextOriginColor);
        mTextChangeColor = ta.getColor(
                R.styleable.ColorTrackView_text_change_color, mTextChangeColor);
        mBgOriginColor = ta.getColor(R.styleable.ColorTrackView_bg_origin_color, mBgOriginColor);
        mBgChangeColor = ta.getColor(R.styleable.ColorTrackView_bg_change_color, mBgChangeColor);
        mBorderOriginColor = ta.getColor(R.styleable.ColorTrackView_border_origin_color, mBorderOriginColor);
        mBorderChangeColor = ta.getColor(R.styleable.ColorTrackView_border_change_color, mBorderChangeColor);

        mProgress = ta.getFloat(R.styleable.ColorTrackView_progress, 0);

        mDirection = ta
                .getInt(R.styleable.ColorTrackView_direction, mDirection);

        ta.recycle();

        textPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureText();

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mTextStartX = getMeasuredWidth() / 2 - mTextWidth / 2;
        mTextStartY = getMeasuredHeight() / 2 - mTextHeight / 2;
    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.height();
                result += getPaddingTop() + getPaddingBottom();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                // result = mTextBound.width();
                result = mTextWidth;
                result += getPaddingLeft() + getPaddingRight();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private void measureText() {
        mTextWidth = (int) textPaint.measureText(mText);
        FontMetrics fm = textPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.top);

        textPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = mTextBound.height();
    }

    public void reverseColor() {
        int tmp = mTextOriginColor;
        mTextOriginColor = mTextChangeColor;
        mTextChangeColor = tmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("ColorTrackView", "mTextStartX:" + mTextStartX);
        int r = (int) (mProgress * mTextWidth + mTextStartX);
        int t = (int) (mProgress * mTextHeight + mTextStartY);
        Log.d("ColorTrackView", "mDirection:" + mDirection);
        if (mDirection == DIRECTION_LEFT) {
            Log.d("ColorTrackView", "left");
            drawChangeLeft(canvas, r);
            drawOriginLeft(canvas, r);
        } else if (mDirection == DIRECTION_RIGHT) {
            Log.d("ColorTrackView", "right");
            drawOriginRight(canvas, r);
            drawChangeRight(canvas, r);
        } else if (mDirection == DIRECTION_TOP) {
            drawOriginTop(canvas, t);
            drawChangeTop(canvas, t);
        } else {
            drawOriginBottom(canvas, t);
            drawChangeBottom(canvas, t);
        }

    }

    private boolean debug = false;

    private void drawBg(Canvas canvas, int bgColor, int borderColor, int startX, int endX) {
        borderPaint.setColor(borderColor);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getHeight());
        canvas.drawColor(bgColor);
        canvas.drawRect(0,0,getWidth(),getHeight(),borderPaint);
        canvas.restore();
    }

    /**
     * @hide
     */
    private void drawBorder(Canvas canvas, int color, int startX, int endX) {
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        borderPaint.setColor(color);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawRect(startX, 0, endX, getMeasuredHeight(), borderPaint);
        canvas.restore();
    }

    private void drawText_h(Canvas canvas, int color, int startX, int endX) {
        textPaint.setColor(color);
        if (debug) {
            textPaint.setStyle(Style.STROKE);
            canvas.drawRect(startX, 0, endX, getMeasuredHeight(), textPaint);
        }
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());// left, top,
        // right, bottom
        canvas.drawText(mText, mTextStartX,
                getMeasuredHeight() / 2
                        - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas.restore();
    }

    private void drawText_v(Canvas canvas, int color, int startY, int endY) {
        textPaint.setColor(color);
        if (debug) {
            textPaint.setStyle(Style.STROKE);
            canvas.drawRect(0, startY, getMeasuredWidth(), endY, textPaint);
        }

        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(0, startY, getMeasuredWidth(), endY);// left, top,
        canvas.drawText(mText, mTextStartX,
                getMeasuredHeight() / 2
                        - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas.restore();
    }

    private void drawChangeLeft(Canvas canvas, int r) {
        Log.d("ColorTrackView", "getWidth():" + getWidth());
        drawBg(canvas,mBgChangeColor,mBorderChangeColor,0, (int) (bgProgress * getWidth()));
        drawText_h(canvas, mTextChangeColor, mTextStartX,
                (int) (mTextStartX + mProgress * mTextWidth));

    }

    private void drawOriginLeft(Canvas canvas, int r) {
        drawBg(canvas,mBgOriginColor,mBorderOriginColor, (int) (bgProgress *getWidth()), getWidth());
        drawText_h(canvas, mTextOriginColor, (int) (mTextStartX + mProgress
                * mTextWidth), mTextStartX + mTextWidth);
    }

    private void drawChangeRight(Canvas canvas, int r) {
        drawBg(canvas,mBgChangeColor,mBorderChangeColor,(int) ((1 - bgProgress) * getWidth()), getWidth());
        drawText_h(canvas,mTextChangeColor,
                (int) (mTextStartX + (1 - mProgress) * mTextWidth), mTextStartX
                        + mTextWidth);
    }

    private void drawOriginRight(Canvas canvas, int r) {
        drawBg(canvas,mBgOriginColor,mBorderOriginColor,0, (int) ((1-bgProgress) * getWidth()));
        drawText_h(canvas, mTextOriginColor, mTextStartX,
                (int) (mTextStartX + (1 - mProgress) * mTextWidth));
    }

    private void drawChangeTop(Canvas canvas, int r) {
        drawText_v(canvas, mTextChangeColor, mTextStartY,
                (int) (mTextStartY + mProgress * mTextHeight));
    }

    private void drawOriginTop(Canvas canvas, int r) {
        drawText_v(canvas, mTextOriginColor, (int) (mTextStartY + mProgress
                * mTextHeight), mTextStartY + mTextHeight);
    }

    private void drawChangeBottom(Canvas canvas, int t) {
        drawText_v(canvas, mTextChangeColor,
                (int) (mTextStartY + (1 - mProgress) * mTextHeight),
                mTextStartY + mTextHeight);
    }

    private void drawOriginBottom(Canvas canvas, int t) {
        drawText_v(canvas, mTextOriginColor, mTextStartY,
                (int) (mTextStartY + (1 - mProgress) * mTextHeight));
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        textPaint.setTextSize(mTextSize);
        requestLayout();
        invalidate();
    }

    public void setText(String text) {
        this.mText = text;
        requestLayout();
        invalidate();
    }

    public int getTextOriginColor() {
        return mTextOriginColor;
    }

    public void setTextOriginColor(int mTextOriginColor) {
        this.mTextOriginColor = mTextOriginColor;
        invalidate();
    }

    public int getTextChangeColor() {
        return mTextChangeColor;
    }

    public int getmBgOriginColor() {
        return mBgOriginColor;
    }

    public void setmBgOriginColor(int mBgOriginColor) {
        this.mBgOriginColor = mBgOriginColor;
        invalidate();
    }

    public int getmBgChangeColor() {
        return mBgChangeColor;
    }

    public void setmBgChangeColor(int mBgChangeColor) {
        this.mBgChangeColor = mBgChangeColor;
        invalidate();
    }

    public void setTextChangeColor(int mTextChangeColor) {
        this.mTextChangeColor = mTextChangeColor;
        invalidate();
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }

    private static final String KEY_STATE_PROGRESS = "key_progress";
    private static final String KEY_DEFAULT_STATE = "key_default_state";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat(KEY_STATE_PROGRESS, mProgress);
        bundle.putParcelable(KEY_DEFAULT_STATE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getFloat(KEY_STATE_PROGRESS);
            super.onRestoreInstanceState(bundle
                    .getParcelable(KEY_DEFAULT_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
