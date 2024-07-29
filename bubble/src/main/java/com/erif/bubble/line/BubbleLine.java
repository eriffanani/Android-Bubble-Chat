package com.erif.bubble.line;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;
import com.erif.bubble.Bubbles.*;

import dalvik.annotation.optimization.CriticalNative;

public class BubbleLine extends FrameLayout {

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

    private int bubbleCondition = BubbleCondition.SINGLE.value;

    public BubbleLine(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleLine(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleLine(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                    attrs, R.styleable.BubbleLine, defStyleAttr, 0
            );
            try {
                messageType = typedArray.getInteger(R.styleable.BubbleTelegram_messageType, INCOMING);

                cornerRadius = typedArray.getDimension(R.styleable.BubbleTelegram_cornerRadius, 0f);
                elevation = typedArray.getDimension(R.styleable.BubbleTelegram_elevation, 6f);
                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#ABE871");
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
        setLayerType(LAYER_TYPE_SOFTWARE, paintShadow);

        setClipToPadding(false);
        int paddingSide = 50;
        int paddingV = 30;
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
        if (messageType == OUTGOING) {
            if (elevation >= 1f)
                canvas.drawPath(pathOutgoing(true), paintShadow);
            canvas.drawPath(pathOutgoing(false), paint);
        } else {
            if (elevation >= 1f)
                canvas.drawPath(pathIncoming(true), paintShadow);
            canvas.drawPath(pathIncoming(false), paint);
        }
    }

    private Path pathIncoming(boolean isShadow) {
        Path path = new Path();
        int width = getWidth();
        int height = getHeight();
        float mElevation = Math.min(elevation, height / 4f);
        float left = isShadow ? 0f : (mElevation / 1.5f);
        float top = isShadow ? 0f : (mElevation / 3f);
        float right = isShadow ? width : width - (mElevation / 1.5f);
        float bottom = isShadow ? height : height - (mElevation / 1.4f);
        float maxCorner = Math.min(cornerRadius, height / 1.88f);
        float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;

        float leftCard = isShadow ? curveWidth : curveWidth + (mElevation / 1.6f);
        path.moveTo(right / 2f, top); // Center Top (Starting Point)

        path.lineTo(leftCard + mCorner, top); // Top Left
        float curveSmallX = (leftCard + mCorner) - (mCorner / 1.35f);
        float curveSmallY = mCorner / 5f;
        float distanceCornerToCurveX = leftCard + mCorner - curveSmallX;
        path.quadTo( // Corner Top Left
                leftCard + mCorner - (distanceCornerToCurveX / 1.6f), top,
                curveSmallX, top + curveSmallY
        );
        path.quadTo(leftCard, 28f, left + 8f, top + 4f); // Curved Top
        path.lineTo(left + 4f, top + 4f); // Curved Connection
        path.quadTo(leftCard / 2f, 38f, leftCard, top + 44f); // Curved Bottom

        path.lineTo(leftCard, bottom - mCorner); // Bottom Left
        path.quadTo(leftCard, bottom, leftCard + mCorner, bottom); // Corner Bottom Left

        path.lineTo(right - mCorner, bottom); // Bottom Right
        path.quadTo(right, bottom, right, bottom - mCorner); // Corner Bottom Right

        path.lineTo(right, top + mCorner); // Top Right
        path.quadTo(right, top, right - mCorner, top); // Corner Top Right

        return path;
    }

    private Path pathOutgoing(boolean isShadow) {
        Path path = new Path();
        int width = getWidth();
        int height = getHeight();
        float mElevation = Math.min(elevation, height / 4f);
        float left = isShadow ? 0f : (mElevation / 1.5f);
        float top = isShadow ? 0f : (mElevation / 3f);
        float right = isShadow ? width : width - (mElevation / 1.5f);
        float bottom = isShadow ? height : height - (mElevation / 1.4f);
        float maxCorner = Math.min(cornerRadius, height / 1.88f);
        float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;

        float rightCard = isShadow ? right - curveWidth : right - curveWidth + (mElevation / 1.6f);
        path.moveTo(right / 2f, top); // Center Top (Starting Point)

        path.lineTo(rightCard - mCorner, top); // Top Right
        float curveSmallX = (rightCard - mCorner) + (mCorner / 1.35f);
        float curveSmallY = mCorner / 5f;
        float distanceCornerToCurveX = mCorner / 1.35f;
        path.quadTo( // Corner Top Right
                rightCard - mCorner + (distanceCornerToCurveX / 1.6f), top,
                curveSmallX, top + curveSmallY
        );
        path.quadTo(rightCard, 28f, right - 8f, top + 4f); // Curved Top
        path.lineTo(right - 4f, top + 4f); // Curved Connection
        path.quadTo(rightCard + ((right - rightCard) / 2f), 38f, rightCard, top + 44f); // Curved Bottom

        path.lineTo(rightCard, bottom - mCorner); // Bottom Right
        path.quadTo(rightCard, bottom, rightCard - mCorner, bottom); // Corner Bottom Right

        path.lineTo(left + mCorner, bottom); // Bottom Left
        path.quadTo(left, bottom, left, bottom - mCorner); // Corner Bottom Left

        path.lineTo(left, top + mCorner); // Top Left
        path.quadTo(left, top, left + mCorner, top); // Corner Top Left

        return path;
    }

    public void setBubbleCondition(BubbleCondition condition) {
        this.bubbleCondition = condition.value;
        invalidate();
    }

}
