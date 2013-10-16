package com.njnu.kai.feiphoneinfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 13-10-16
 */
public class FontDimensionView extends View {

    private float mFontSize;
    private String mTextString = "水渡石 钱红凯 Shui Qian 12345 字体尺寸";
    private TextPaint mTextPaint;
    private Typeface mTypefaceNormal;

    public FontDimensionView(Context context) {
        super(context);
        init(context);
    }

    public FontDimensionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontDimensionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
        mTypefaceNormal = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(mTypefaceNormal);
        mTextPaint.setTextSize(mFontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mTextString, 20, 70, mTextPaint);
    }
}
