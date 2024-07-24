package com.erif.bubble.sms;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.R;

import java.lang.reflect.Type;

public class BubbleSms extends FrameLayout {

    private Paint paintShadow;
    private Paint paintCard;

    private float elevation = 8f;
    private float cornerRadius = 40f;

    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;
    private int messageType = 0;

    private int backgroundColor;
    private boolean useCompatPadding;
    private int shadowColor;
    private float borderSize = 0f;

    public BubbleSms(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleSms(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleSms(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        setWillNotDraw(false);
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray a = theme.obtainStyledAttributes(
                    attrs, R.styleable.BubbleSms, defStyleAttr, 0
            );
            try {
                messageType = a.getInteger(R.styleable.BubbleSms_messageType, INCOMING);

                cornerRadius = a.getDimension(R.styleable.BubbleSms_cornerRadius, 0f);
                elevation = a.getDimension(R.styleable.BubbleSms_elevation, toDp(1));

                borderSize = toDp(1) / 2f;
                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#dddddd");
                int defaultBackgroundColor = messageType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = a.getColor(R.styleable.BubbleSms_backgroundColor, defaultBackgroundColor);
                useCompatPadding = a.getBoolean(R.styleable.BubbleSms_useCompatPadding, true);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = a.getColor(R.styleable.BubbleSms_android_shadowColor, defaultColorShadow);
            } finally {
                a.recycle();
            }
        }

        paintShadow = new Paint();
        paintShadow.setStyle(Paint.Style.STROKE);
        paintShadow.setColor(shadowColor);
        paintShadow.setAntiAlias(true);
        if (elevation >= 1) {
            paintShadow.setStrokeWidth(borderSize);
            paintShadow.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            float minBlur = Math.max(1f, elevation);
            float blurSize = Math.min(minBlur, 30f);
            paintShadow.setMaskFilter(new BlurMaskFilter(
                    blurSize, BlurMaskFilter.Blur.NORMAL
            ));
        }
        paintCard = new Paint();
        paintCard.setAntiAlias(true);
        paintCard.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (elevation >= 1f)
            canvas.drawPath(pathShadow(), paintShadow);
        canvas.drawPath(pathCard(), paintCard);
        super.onDraw(canvas);
    }

    private Path pathShadow() {
        Path path = new Path();
        float mElevation = Math.min(elevation, getHeight() / 10f);
        float left = mElevation * 1.5f;
        float top = 0f + (mElevation + borderSize) * 2;
        float right = getWidth() - (mElevation * 2f);
        float bottom = getHeight() - (mElevation * 2f);
        float mCorner = Math.min(cornerRadius, (getHeight() - (mElevation * 2f) - borderSize) / 2f);

        path.moveTo(right / 2f, top);

        path.lineTo(right - mCorner, top);
        path.quadTo(right, top, right, top + mCorner);

        path.lineTo(right, bottom - mCorner);
        path.quadTo(right, bottom, right - mCorner, bottom);

        path.lineTo(left + mCorner, bottom);
        path.quadTo(left, bottom, left, bottom - mCorner);

        path.lineTo(left, top + mCorner);
        path.quadTo(left, top, left + mCorner, top);

        path.lineTo(right / 2f, top);

        return path;
    }

    private Path pathCard() {
        Path path = new Path();
        float mElevation = Math.min(elevation, getHeight() / 10f);
        float left = 0f + (mElevation * 1.2f) - borderSize;
        float top = 0f + mElevation + borderSize;
        float right = getWidth() - (mElevation * 1.3f) - borderSize;
        float bottom = getHeight() - (mElevation * 2f) - borderSize;
        float mCorner = Math.min(cornerRadius, (getHeight() - (mElevation * 2f) - borderSize) / 2f);

        path.moveTo(right / 2f, top);

        path.lineTo(right - mCorner, top);
        path.quadTo(right, top, right, top + mCorner);

        path.lineTo(right, bottom - mCorner);
        path.quadTo(right, bottom, right - mCorner, bottom);

        path.lineTo(left + mCorner, bottom);
        path.quadTo(left, bottom, left, bottom - mCorner);

        path.lineTo(left, top + mCorner);
        path.quadTo(left, top, left + mCorner, top);
        return path;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float toDp(int dp) {
        int density = getContext().getResources().getDisplayMetrics().densityDpi;
        return dp * ((float) density / DisplayMetrics.DENSITY_DEFAULT);
    }

}
