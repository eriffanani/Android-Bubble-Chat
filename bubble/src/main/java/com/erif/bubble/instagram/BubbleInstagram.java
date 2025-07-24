package com.erif.bubble.instagram;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;
import com.erif.bubble.Bubbles.*;

public class BubbleInstagram extends FrameLayout {

    private Paint paintCard;
    private int backgroundColor = 0;
    private int backgroundPressedColor = 0;
    private int shadowColor = 0;
    private float elevation = 0f;
    private boolean useCompatPadding = true;
    private float shadowSize = 0f;
    private float borderWidth = 0f;
    private int borderColor = 0;
    private boolean backgroundPressedEnabled = true;

    public static final int INCOMING = BubbleType.INCOMING.value;
    public static final int OUTGOING = BubbleType.OUTGOING.value;
    private int bubbleType = INCOMING;

    private int bubbleCondition = BubbleCondition.SINGLE.value;
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
            bubbleType = a.getInteger(R.styleable.BubbleInstagram_bubbleType, INCOMING);

            float cornerRadius = a.getDimension(R.styleable.BubbleInstagram_cornerRadius, dp(24));
            float cornerRadiusGroup = a.getDimension(R.styleable.BubbleInstagram_cornerRadiusGroup, dp(4));
            bubble.setCornerRadius(cornerRadius);
            bubble.setCornerRadiusGroup(cornerRadiusGroup);

            elevation = a.getDimension(R.styleable.BubbleInstagram_elevation, dp(2));
            shadowSize = dp(1) / 2f;

            int colorIncoming = Color.parseColor("#F2F4F5");
            int colorOutgoing = Color.parseColor("#3F63FE");
            int defaultBackgroundColor = bubbleType == INCOMING ? colorIncoming : colorOutgoing;
            backgroundColor = a.getColor(R.styleable.BubbleInstagram_backgroundColor, defaultBackgroundColor);
            elevation = backgroundColor != 0 ? elevation : 0f;
            bubble.setShadowSize(elevation >= 1f ? shadowSize : 0f);
            bubble.setElevation(backgroundColor != 0 ? elevation : 0f);

            // Pressed Color
            int colorIncomingPressed = Color.parseColor("#E7E8E8");
            int colorOutgoingPressed = Color.parseColor("#3A5BE8");
            int defaultBackgroundPressedColor = bubbleType == INCOMING ? colorIncomingPressed : colorOutgoingPressed;
            backgroundPressedColor = a.getColor(R.styleable.BubbleInstagram_backgroundPressedColor, defaultBackgroundPressedColor);
            backgroundPressedEnabled = a.getBoolean(R.styleable.BubbleInstagram_backgroundPressedEnabled, true);

            useCompatPadding = a.getBoolean(R.styleable.BubbleInstagram_useCompatPadding, true);
            int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
            shadowColor = a.getColor(R.styleable.BubbleInstagram_android_shadowColor, defaultColorShadow);
            int defaultCondition = BubbleCondition.SINGLE.value;
            bubbleCondition = a.getInteger(R.styleable.BubbleInstagram_bubbleConditions, defaultCondition);

            borderWidth = a.getDimension(R.styleable.BubbleInstagram_strokeWidth, 0f);
            bubble.setBorderWidth(borderWidth);
            borderColor = a.getColor(R.styleable.BubbleInstagram_strokeColor, Color.BLACK);

            a.recycle();
        }

        if (backgroundColor != 0) {
            paintCard = new Paint();
            paintCard.setAntiAlias(true);
            paintCard.setStyle(Paint.Style.FILL);
            paintCard.setColor(backgroundColor);
            bubble.setPaintCard(paintCard);

            if (elevation >= 1f) {
                Paint paintShadow = new Paint();
                paintShadow.setAntiAlias(true);
                paintShadow.setStyle(Paint.Style.STROKE);
                paintShadow.setColor(shadowColor);
                if (elevation >= 1f) {
                    paintShadow.setStrokeWidth(shadowSize);
                    paintShadow.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                    float minBlur = Math.max(1f, elevation);
                    float blurSize = Math.min(minBlur, 30f);
                    paintShadow.setMaskFilter(new BlurMaskFilter(
                            blurSize, BlurMaskFilter.Blur.NORMAL
                    ));
                }
                bubble.setPaintShadow(paintShadow);
            }
        }

        if (borderWidth >= 1f) {
            Paint paintBorder = new Paint();
            paintBorder.setAntiAlias(true);
            paintBorder.setStyle(Paint.Style.STROKE);
            paintBorder.setColor(borderColor);
            paintBorder.setStrokeWidth(borderWidth);
            bubble.setPaintBorder(paintBorder);
        }

        setClipToPadding(false);
        int paddingSide = 40;
        int paddingV = 34;
        if (useCompatPadding)
            setPadding(paddingSide, paddingV, paddingSide, paddingV + (int) (elevation * 1.5f));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (paintCard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    paintCard.setColor(backgroundPressedColor);
                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    paintCard.setColor(backgroundColor);
                    invalidate();
                    break;
            }
        }
        return backgroundPressedEnabled | super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        bubble.setSize(getWidth(), getHeight());
        int oldest = BubbleCondition.OLDEST.value;
        int inBetween = BubbleCondition.IN_BETWEEN.value;
        int latest = BubbleCondition.LATEST.value;
        if (bubbleType == OUTGOING) {
            if (bubbleCondition == oldest) {
                bubble.outgoing().oldest(canvas);
            } else if (bubbleCondition == inBetween) {
                bubble.outgoing().inBetween(canvas);
            } else if (bubbleCondition == latest) {
                bubble.outgoing().latest(canvas);
            } else { // Single
                bubble.outgoing().single(canvas);
            }
        } else { // Incoming
            if (bubbleCondition == oldest) {
                bubble.incoming().oldest(canvas);
            } else if (bubbleCondition == inBetween) {
                bubble.incoming().inBetween(canvas);
            } else if (bubbleCondition == latest) {
                bubble.incoming().latest(canvas);
            } else { // Single
                bubble.incoming().single(canvas);
            }
        }
    }

    public void setBubbleType(Bubbles.BubbleType type) {
        if (bubbleType != type.value) {
            this.bubbleType = type.value;
            invalidate();
        }
    }

    public void setBubbleCondition(Bubbles.BubbleCondition condition) {
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
