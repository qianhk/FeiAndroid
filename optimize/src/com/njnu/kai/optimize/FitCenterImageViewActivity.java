package com.njnu.kai.optimize;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


public class FitCenterImageViewActivity extends Activity {

    private static final String LOG_TAG = "FitCenterImageViewActivity";
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mImageView4;
    private ImageView mImageView5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fitcenterimage);

        mImageView1 = (ImageView)findViewById(R.id.iv_1);
        mImageView1.setTag("imageview_1");

        mImageView2 = (ImageView)findViewById(R.id.iv_2);
        mImageView2.setTag("imageview_2");

        mImageView3 = (ImageView)findViewById(R.id.iv_3);
        mImageView3.setTag("imageview_3");

        mImageView4 = (ImageView)findViewById(R.id.iv_4);
        mImageView4.setTag("imageview_4");

        mImageView5 = (ImageView)findViewById(R.id.iv_5);
        mImageView5.setTag("imageview_5");

        amendAllImageView();

    }

    private void onViewGlobalLayout(final ImageView imageView) {
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.d(LOG_TAG, "onGlobalLayout tag=" + imageView.getTag());
                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    amendMatrixForCenterCrop(imageView);
                }
            });
        }
    }

    private void amendAllImageView() {
        onViewGlobalLayout(mImageView1);
        onViewGlobalLayout(mImageView2);
        onViewGlobalLayout(mImageView3);
        onViewGlobalLayout(mImageView4);
        onViewGlobalLayout(mImageView5);
    }

    public static void amendMatrixForCenterCrop(ImageView imageView) {
        if (imageView == null) {
            return;
        }

        Drawable drawable = imageView.getDrawable();
        int drawableHeight = drawable != null ? drawable.getIntrinsicHeight() : 0;
        int drawableWidth = drawable != null ? drawable.getIntrinsicWidth() : 0;
        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();
        if (drawableHeight <= 0 || drawableWidth <= 0 || viewWidth <= 0 || viewHeight <= 0) {
            return;
        }
        Log.d(LOG_TAG, String.format("amendMatrixForCenterCrop tag=%s view=%d,%d drawable=%d,%d", imageView.getTag(), viewWidth, viewHeight, drawableWidth, drawableHeight));
        float horizontalScaleRatio = 1.0f * viewWidth / drawableWidth;
        float verticalScaleRatio = 1.0f * viewHeight / drawableHeight;
        if (verticalScaleRatio >= horizontalScaleRatio) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.d(LOG_TAG, String.format("use system center_crop %f %f", horizontalScaleRatio, verticalScaleRatio));
        } else {
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            float scaleRatio = Math.max(horizontalScaleRatio, verticalScaleRatio);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleRatio, scaleRatio);
            imageView.setImageMatrix(matrix);
            Log.d(LOG_TAG, String.format("use my matrix %f %f scale=%f", horizontalScaleRatio, verticalScaleRatio, scaleRatio));
        }
    }
}