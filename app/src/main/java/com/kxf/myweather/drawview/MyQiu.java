package com.kxf.myweather.drawview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 球
 */
public class MyQiu  extends View {
    private static final int DEFAULT_TEXTCOLOT = 0xFFFFFFFF;

    private static final int DEFAULT_TEXTSIZE = 250;

    private float mPercent;

    private Paint mPaint = new Paint();

    private Bitmap mBitmap;

    private Bitmap mScaledBitmap;

    private float mLeft, mTop;

    private int mSpeed = 15;

    private int mRepeatCount = 0;

//    private Status mFlag = Status.NO;

    private int mTextColor = DEFAULT_TEXTCOLOT;

    private int mTextSize = DEFAULT_TEXTSIZE;

    public MyQiu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyQiu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
