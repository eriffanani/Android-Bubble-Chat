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
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;
import com.erif.bubble.Bubbles.*;

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
    public static final int INCOMING = BubbleType.INCOMING.value;
    public static final int OUTGOING = BubbleType.OUTGOING.value;
    private int bubbleType = INCOMING;

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
            TypedArray a = theme.obtainStyledAttributes(
                    attrs, R.styleable.BubbleLine, defStyleAttr, 0
            );
            try {
                bubbleType = a.getInteger(R.styleable.BubbleLine_bubbleType, INCOMING);

                cornerRadius = a.getDimension(R.styleable.BubbleLine_cornerRadius, 0f);
                elevation = a.getDimension(R.styleable.BubbleLine_elevation, dp(2));
                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#ABE871");
                int defaultBackgroundColor = bubbleType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = a.getColor(R.styleable.BubbleLine_backgroundColor, defaultBackgroundColor);
                useCompatPadding = a.getBoolean(R.styleable.BubbleLine_useCompatPadding, true);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = a.getColor(R.styleable.BubbleLine_android_shadowColor, defaultColorShadow);
                bubbleCondition = a.getInteger(R.styleable.BubbleLine_bubbleCondition, BubbleCondition.SINGLE.value);
            } finally {
                a.recycle();
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
                    bubbleType == INCOMING ? paddingFromCurve : paddingSide,
                    paddingV,
                    bubbleType == OUTGOING ? paddingFromCurve : paddingSide,
                    paddingV
            );

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int isSingle = BubbleCondition.SINGLE.value;
        int isOldest = BubbleCondition.OLDEST.value;
        boolean isSingleOrOldest = bubbleCondition == isSingle || bubbleCondition == isOldest;
        if (bubbleType == OUTGOING) {
            if (elevation >= 1f)
                canvas.drawPath(pathOutgoing(true, isSingleOrOldest), paintShadow);
            canvas.drawPath(pathOutgoing(false, isSingleOrOldest), paint);
        } else {
            if (elevation >= 1f)
                canvas.drawPath(pathIncoming(true, isSingleOrOldest), paintShadow);
            canvas.drawPath(pathIncoming(false, isSingleOrOldest), paint);
        }
    }

    private Path pathIncoming(boolean isShadow, boolean oldest) {
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

        if (oldest) {
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
        } else {
            path.lineTo(leftCard + mCorner, top);
            path.quadTo(leftCard, top, leftCard, top + mCorner);
        }

        path.lineTo(leftCard, bottom - mCorner); // Bottom Left
        path.quadTo(leftCard, bottom, leftCard + mCorner, bottom); // Corner Bottom Left

        path.lineTo(right - mCorner, bottom); // Bottom Right
        path.quadTo(right, bottom, right, bottom - mCorner); // Corner Bottom Right

        path.lineTo(right, top + mCorner); // Top Right
        path.quadTo(right, top, right - mCorner, top); // Corner Top Right

        return path;
    }

    private Path pathOutgoing(boolean isShadow, boolean oldest) {
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

        if (oldest) {
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
        } else {
            path.lineTo(rightCard - mCorner, top);
            path.quadTo(rightCard, top, rightCard, top + mCorner);
        }

        path.lineTo(rightCard, bottom - mCorner); // Bottom Right
        path.quadTo(rightCard, bottom, rightCard - mCorner, bottom); // Corner Bottom Right

        path.lineTo(left + mCorner, bottom); // Bottom Left
        path.quadTo(left, bottom, left, bottom - mCorner); // Corner Bottom Left

        path.lineTo(left, top + mCorner); // Top Left
        path.quadTo(left, top, left + mCorner, top); // Corner Top Left

        return path;
    }

    public void setBubbleType(Bubbles.BubbleType type) {
        if (bubbleType != type.value) {
            this.bubbleType = type.value;
            invalidate();
        }
    }

    public void setBubbleCondition(BubbleCondition condition) {
        if (bubbleCondition != condition.value) {
            this.bubbleCondition = condition.value;
            invalidate();
        }
    }

    private float dp(int dp) {
        int density = getContext().getResources().getDisplayMetrics().densityDpi;
        return dp * ((float) density / DisplayMetrics.DENSITY_DEFAULT);
    }

}
