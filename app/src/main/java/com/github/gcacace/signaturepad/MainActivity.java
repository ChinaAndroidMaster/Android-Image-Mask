package com.github.gcacace.signaturepad;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import it.gcacace.signaturepad.R;

public class MainActivity extends Activity {

    private MyView mView;
    private FrameLayout mFrameLayout;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (MyView) findViewById(R.id.myView);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mImageView.setImageResource(R.drawable.android_lollipop);
        /*final PhotoViewAttacher attacher = new PhotoViewAttacher(mImageView);
        attacher.setMinimumScale(1f);
        PhotoViewAttacher anotherAttacher = new PhotoViewAttacher(mView);
        attacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                final float scale = attacher.getScale();
                final float v = rect.centerX();
                final float v1 = rect.centerY();
                Log.i("DEBUG", "scale = " + scale + ",centerX = " + v + ",centerY=" + v1 + "\nleft = " + rect.left + ",top = " + rect.top + ",right = " + rect.right + ",bottom = " + rect.bottom);
                mView.beginScale(rect);
                //mView.beginScale(rect);

            }
        });*/


        findViewById(R.id.button_draw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.setErase(false);

            }
        });
        findViewById(R.id.button_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.setErase(true);
            }
        });

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap drawBitmap = mView.getDrawBitmap();
                final Bitmap bitmap = ((BitmapDrawable) mView.getDrawable()).getBitmap();
                Bitmap out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                final Bitmap scaleDrawBitmap = Bitmap.createScaledBitmap(drawBitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Canvas canvas = new Canvas(out);
                canvas.drawBitmap(bitmap, new Matrix(), null);
                canvas.drawBitmap(scaleDrawBitmap, new Matrix(), null);
                ((ImageView) findViewById(R.id.out_image_view)).setImageBitmap(out);
            }
        });

        //TODO 现在情况是,实际的Bitmap为640*361,导致实际绘制的位置在左上角一部分
        //TODO 需要将原实际Bitmap的大小缩放到适配屏幕,此时

        /*mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean drawing = false;
                if (event.getPointerCount() == 1) {
                    drawing = mView.onTouchEvent(event);
                }
                attacher.forbid(drawing);
                attacher.onTouch(mImageView, event);
                final int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
                    mView.endScale();
                }
                return true;
            }
        });*/
    }
}
