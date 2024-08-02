package com.erif.bubble.telegram;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;
import com.erif.bubble.Bubbles.*;

public class BubbleTelegram extends FrameLayout {

    private int backgroundColor;
    private int shadowColor;
    private boolean useCompatPadding = true;
    private float elevation = 0f;

    // Message Type
    public static final int INCOMING = BubbleType.INCOMING.value;
    public static final int OUTGOING = BubbleType.OUTGOING.value;
    private int bubbleType = INCOMING;

    private static final int SINGLE = BubbleCondition.SINGLE.value;
    private static final int OLDEST = BubbleCondition.OLDEST.value;
    private static final int IN_BETWEEN = BubbleCondition.IN_BETWEEN.value;
    private static final int LATEST = BubbleCondition.LATEST.value;
    private int bubbleCondition = SINGLE;

    private static final int ANDROID = 0;
    private static final int IOS = 1;
    private int backgroundStyle = ANDROID;

    private final BubbleCreator bubble = new BubbleCreator();

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
            TypedArray a = theme.obtainStyledAttributes(
                    attrs, R.styleable.BubbleTelegram, defStyleAttr, 0
            );
            try {
                bubbleType = a.getInteger(R.styleable.BubbleTelegram_bubbleType, INCOMING);

                float cornerRadius = a.getDimension(R.styleable.BubbleTelegram_cornerRadius, 0f);
                bubble.setCornerRadius(cornerRadius);

                elevation = a.getDimension(R.styleable.BubbleTelegram_elevation, dp(2));
                bubble.setElevation(elevation);

                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#EFFDDE");
                int defaultBackgroundColor = bubbleType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = a.getColor(R.styleable.BubbleTelegram_backgroundColor, defaultBackgroundColor);
                useCompatPadding = a.getBoolean(R.styleable.BubbleTelegram_useCompatPadding, true);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = a.getColor(R.styleable.BubbleTelegram_android_shadowColor, defaultColorShadow);
                bubbleCondition = a.getInteger(R.styleable.BubbleTelegram_bubbleConditions, BubbleCondition.SINGLE.value);
                backgroundStyle = a.getInteger(R.styleable.BubbleTelegram_backgroundStyle, ANDROID);
            } finally {
                a.recycle();
            }
        }

        // Paint Card
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        paint.setMaskFilter(null);
        bubble.setPaintCard(paint);

        // Paint Shadow
        Paint paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow.setStyle(Paint.Style.FILL);
        paintShadow.setColor(shadowColor);
        paintShadow.setMaskFilter(new BlurMaskFilter(
                Math.min(Math.max(1f, elevation), 20f), BlurMaskFilter.Blur.NORMAL
        ));
        bubble.setPaintShadow(paintShadow);

        setClipToPadding(false);
        int paddingSide = 42;
        int paddingV = 38;
        float curveWidth = BubbleCreator.CURVED_WIDTH;
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
        bubble.setSize(getWidth(), getHeight());
        if (backgroundStyle == IOS) {
            if (bubbleType == OUTGOING) {
                if (bubbleCondition == OLDEST) {
                    bubble.iOS().outgoing().oldest(canvas);
                } else if (bubbleCondition == IN_BETWEEN) {
                    bubble.iOS().outgoing().inBetween(canvas);
                } else if (bubbleCondition == LATEST) {
                    bubble.iOS().outgoing().latest(canvas);
                } else {
                    bubble.iOS().outgoing().single(canvas);
                }
            } else {
                if (bubbleCondition == OLDEST) {
                    bubble.iOS().incoming().oldest(canvas);
                } else if (bubbleCondition == IN_BETWEEN) {
                    bubble.iOS().incoming().inBetween(canvas);
                } else if (bubbleCondition == LATEST) {
                    bubble.iOS().incoming().latest(canvas);
                } else {
                    bubble.iOS().incoming().single(canvas);
                }
            }
        } else { // Android
            if (bubbleType == OUTGOING) {
                if (bubbleCondition == OLDEST) {
                    bubble.android().outgoing().oldest(canvas);
                } else if (bubbleCondition == IN_BETWEEN) {
                    bubble.android().outgoing().inBetween(canvas);
                } else if (bubbleCondition == LATEST) {
                    bubble.android().outgoing().latest(canvas);
                } else {
                    bubble.android().outgoing().single(canvas);
                }
            } else {
                if (bubbleCondition == OLDEST) {
                    bubble.android().incoming().oldest(canvas);
                } else if (bubbleCondition == IN_BETWEEN) {
                    bubble.android().incoming().inBetween(canvas);
                } else if (bubbleCondition == LATEST) {
                    bubble.android().incoming().latest(canvas);
                } else {
                    bubble.android().incoming().single(canvas);
                }
            }
        }
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
