package com.erif.bubble.telegram;

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
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.R;

public class BubbleTelegram extends FrameLayout {

    private Paint paint;
    private Paint paintShadow;

    private int backgroundColor;
    private int shadowColor;
    private final float curveWidth = 34f;
    private boolean useCompatPadding = true;
    private float cornerRadius = 0f;
    private float elevation = 0f;

    // Message Type
    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;
    private int messageType = INCOMING;

    public BubbleTelegram(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleTelegram(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleTelegram(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        setWillNotDraw(false);
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(
                    attrs, R.styleable.BubbleTelegram, defStyleAttr, 0
            );
            try {
                messageType = typedArray.getInteger(R.styleable.BubbleTelegram_messageType, INCOMING);

                cornerRadius = typedArray.getDimension(R.styleable.BubbleTelegram_cornerRadius, 0f);
                elevation = typedArray.getDimension(R.styleable.BubbleTelegram_elevation, 6f);
                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#EFFDDE");
                int defaultBackgroundColor = messageType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = typedArray.getColor(R.styleable.BubbleTelegram_backgroundColor, defaultBackgroundColor);
                useCompatPadding = typedArray.getBoolean(R.styleable.BubbleTelegram_useCompatPadding, true);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = typedArray.getColor(R.styleable.BubbleTelegram_android_shadowColor, defaultColorShadow);
            } finally {
                typedArray.recycle();
            }
        }

        // Paint Card
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        paint.setMaskFilter(null);
        // Paint Shadow
        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow.setStyle(Paint.Style.FILL);
        paintShadow.setColor(shadowColor);
        paintShadow.setMaskFilter(new BlurMaskFilter(
                Math.min(Math.max(1f, elevation), 20f), BlurMaskFilter.Blur.NORMAL
        ));
        //paintShadow.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_SOFTWARE, paintShadow);

        setClipToPadding(false);
        int paddingSide = 42;
        int paddingV = 38;
        int paddingFromCurve = paddingSide + (int) curveWidth;
        if (useCompatPadding)
            setPadding(
                    messageType == INCOMING ? paddingFromCurve : paddingSide,
                    paddingV,
                    messageType == OUTGOING ? paddingFromCurve : paddingSide,
                    paddingV
            );

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        boolean useShadow = elevation >= 1f;
        if (messageType == OUTGOING) {
            if (useShadow)
                canvas.drawPath(pathOutgoing(true), paintShadow);
            canvas.drawPath(pathOutgoing(false), paint);
        } else {
            if (useShadow)
                canvas.drawPath(pathIncoming(true), paintShadow);
            canvas.drawPath(pathIncoming(false), paint);
        }
    }

    private Path pathIncoming(boolean isShadow) {
        Path path = new Path();
        int width = getWidth();
        int height = getHeight();
        float mElevation = Math.min(elevation, height / 3f);
        float maxCorner = Math.min(cornerRadius, height / 1.86f);
        float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
        float left = isShadow ? 0f : mElevation / 1.5f;
        float leftCard = isShadow ? curveWidth : curveWidth + (mElevation / 1.6f);
        float top = isShadow ? 0f : (mElevation / 3f);
        float right = isShadow ? width : width - (mElevation / 1.5f);
        float bottom = isShadow ? height : height - (mElevation / 1.4f);
        path.moveTo(right / 2f, top);

        path.lineTo(right - mCorner, top); // Top Right
        path.quadTo(right, top, right, top + mCorner); // Corner Top Right

        path.lineTo(right, bottom - mCorner); // Bottom Right
        path.quadTo(right, bottom, right - mCorner, bottom); // Corner Bottom Right

        path.lineTo(left + 2f, bottom); // Bottom Left
        path.quadTo(left, bottom - 2f, left + 2f, bottom - 4f); // Curved Small

        float minMCorner = Math.max(35f, mCorner);
        path.quadTo(leftCard, bottom - 15f, leftCard, bottom - minMCorner); // Curved Top

        path.lineTo(leftCard, top + mCorner); // Top Left
        path.quadTo(leftCard, top, leftCard + mCorner, top); // Corner Top Left
        return path;
    }

    private Path pathOutgoing(boolean isShadow) {
        Path path = new Path();
        int width = getWidth();
        int height = getHeight();
        float mElevation = Math.min(elevation, height / 3f);
        float maxCorner = Math.min(cornerRadius, height / 1.86f);
        float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
        float left = isShadow ? 0f : mElevation / 1.5f;
        float top = isShadow ? 0f : (mElevation / 3f);
        float right = isShadow ? width : width - (mElevation / 1.5f);
        float rightCard = isShadow ? width - curveWidth : width - curveWidth - (mElevation / 1.6f);
        float bottom = isShadow ? height : height - (mElevation / 1.4f);
        path.moveTo(right / 2f, top);

        path.lineTo(left + mCorner, top); // Top Left
        path.quadTo(left, top, left, top + mCorner); // Corner Top Left

        path.lineTo(left, bottom - mCorner); // Bottom Left
        path.quadTo(left, bottom, left + mCorner, bottom); // Corner Bottom Left

        path.lineTo(right - 2f, bottom); // Bottom Right
        path.quadTo(right, bottom - 2f, right, bottom - 4f); // Curved Small

        float minMCorner = Math.max(35f, mCorner);
        path.quadTo(rightCard, bottom - 15f, rightCard, bottom - minMCorner); // Curved Top

        path.lineTo(rightCard, top + mCorner); // Top Right
        path.quadTo(rightCard, top, rightCard - mCorner, top); // Corner Top Right

        return path;
    }

}
