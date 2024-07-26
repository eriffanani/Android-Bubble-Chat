package com.erif.bubble.instagram;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.R;

public class BubbleInstagram extends FrameLayout {

    private Paint paintShadow;
    private Paint paintCard;
    private Paint paintStroke;

    private int backgroundColor;
    private int shadowColor;
    private float elevation = 0f;
    private boolean useCompatPadding;
    private float borderSize = 0f;
    private float strokeWidth = 0f;
    private int strokeColor = 0;

    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;
    private int messageType = INCOMING;

    public static final int SINGLE = 0;
    public static final int LATEST = 1;
    public static final int IN_BETWEEN = 2;
    public static final int OLDEST = 3;
    private int bubbleCondition = SINGLE;
    private final BubbleCreator bubble = new BubbleCreator();

    public BubbleInstagram(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleInstagram(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleInstagram(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                    attrs, R.styleable.BubbleInstagram, defStyleAttr, 0
            );
            messageType = a.getInteger(R.styleable.BubbleInstagram_messageType, INCOMING);

            float cornerRadius = a.getDimension(R.styleable.BubbleInstagram_cornerRadius, dp(24));
            bubble.setCornerRadius(cornerRadius);

            elevation = a.getDimension(R.styleable.BubbleInstagram_elevation, dp(2));
            bubble.setElevation(elevation);
            borderSize = dp(1) / 2f;
            bubble.setBorderSize(elevation >= 1f ? borderSize : 0f);

            int colorIncoming = Color.WHITE;
            int colorOutgoing = Color.parseColor("#3F63FE");
            int defaultBackgroundColor = messageType == INCOMING ? colorIncoming : colorOutgoing;
            backgroundColor = a.getColor(R.styleable.BubbleInstagram_backgroundColor, defaultBackgroundColor);
            useCompatPadding = a.getBoolean(R.styleable.BubbleInstagram_useCompatPadding, true);
            int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
            shadowColor = a.getColor(R.styleable.BubbleInstagram_android_shadowColor, defaultColorShadow);
            bubbleCondition = a.getInteger(R.styleable.BubbleInstagram_bubbleCondition, SINGLE);

            strokeWidth = a.getDimension(R.styleable.BubbleInstagram_strokeWidth, 0f);
            bubble.setStrokeWidth(strokeWidth);
            strokeColor = a.getColor(R.styleable.BubbleInstagram_strokeColor, Color.BLACK);

            a.recycle();
        }

        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow.setStyle(Paint.Style.STROKE);
        paintShadow.setColor(shadowColor);
        if (elevation >= 1f) {
            paintShadow.setStrokeWidth(borderSize);
            paintShadow.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            float minBlur = Math.max(1f, elevation);
            float blurSize = Math.min(minBlur, 30f);
            paintShadow.setMaskFilter(new BlurMaskFilter(
                    blurSize, BlurMaskFilter.Blur.NORMAL
            ));
        }

        paintCard = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCard.setStyle(Paint.Style.FILL);
        paintCard.setColor(backgroundColor);

        paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(strokeColor);
        paintStroke.setStrokeWidth(strokeWidth);

        setClipToPadding(false);
        int paddingSide = 40;
        int paddingV = 34;
        if (useCompatPadding)
            setPadding(paddingSide, paddingV, paddingSide, paddingV + (int) (elevation * 1.5f));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        bubble.setSize(getWidth(), getHeight());
        if (messageType == OUTGOING) {
            if (bubbleCondition == OLDEST) {
                bubble.outgoing(canvas, paintShadow, paintCard, paintStroke).oldest();
            } else if (bubbleCondition == IN_BETWEEN) {
                bubble.outgoing(canvas, paintShadow, paintCard, paintStroke).inBetween();
            } else if (bubbleCondition == LATEST) {
                bubble.outgoing(canvas, paintShadow, paintCard, paintStroke).latest();
            } else { // Single
                bubble.outgoing(canvas, paintShadow, paintCard, paintStroke).single();
            }
        } else { // Incoming
            if (bubbleCondition == OLDEST) {
                bubble.incoming(canvas, paintShadow, paintCard, paintStroke).oldest();
            } else if (bubbleCondition == IN_BETWEEN) {
                bubble.incoming(canvas, paintShadow, paintCard, paintStroke).inBetween();
            } else if (bubbleCondition == LATEST) {
                bubble.incoming(canvas, paintShadow, paintCard, paintStroke).latest();
            } else { // Single
                bubble.incoming(canvas, paintShadow, paintCard, paintStroke).single();
            }
        }
    }

    public void setBubbleCondition(int condition) {
        this.bubbleCondition = condition;
        invalidate();
    }

    private float dp(int dp) {
        int density = getContext().getResources().getDisplayMetrics().densityDpi;
        return dp * ((float) density / DisplayMetrics.DENSITY_DEFAULT);
    }

}
