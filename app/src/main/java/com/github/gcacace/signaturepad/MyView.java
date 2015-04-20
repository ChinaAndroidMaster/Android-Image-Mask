package com.github.gcacace.signaturepad;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyView extends ImageView {

    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private BlurMaskFilter mBlur;

    public MyView(Context c) {
        super(c);

        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        mPaint.setMaskFilter(mBlur);
        setBackgroundColor(0x00000000);
        Log.i("DEBUG", "width = " + getWidth() + ",height = " + getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //mBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true), w, h, true);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x00000000);

        //canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);

    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    private boolean isErase;

    public void setErase(boolean erase) {
        if (erase) {
            mPaint.setXfermode(new PorterDuffXfermode(
                    PorterDuff.Mode.CLEAR));
            mPaint.setMaskFilter(null);
            mPaint.setColor(0x00000000);
        } else {
            mPaint.setXfermode(null);
            mPaint.setColor(0xFFFF0000);
            mPaint.setMaskFilter(mBlur);
        }
        isErase = erase;
    }

    private boolean isErase() {
        return isErase;
    }

    boolean scale;
    private Matrix mMatrix = new Matrix();

    public void beginScale(Matrix matrix) {
        scale = true;
        mMatrix.set(matrix);
        //mMatrix.set(matrix);
        //mMatrix.postConcat(matrix);
        //setImageMatrix(matrix);
        //Log.i("DEBUG", "matrix : " + matrix);
        invalidate();
    }

    private RectF mRectF;

    public void beginScale(RectF rectF) {
        scale = true;
        this.mRectF = rectF;
        mMatrix.mapRect(rectF);
        setImageMatrix(mMatrix);
        //mMatrix = matrix;
        //Log.i("DEBUG", "matrix : " + matrix);
        invalidate();
    }

    public void endScale() {
        scale = false;
        invalidate();
    }

    public Bitmap getDrawBitmap() {
        return mBitmap;
    }
}