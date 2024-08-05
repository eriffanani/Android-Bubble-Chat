package com.erif.bubble.whatsapp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;

public class BubbleWhatsapp extends FrameLayout {

    private Paint paintCard;

    private int pressedColor = 0;
    private int backgroundColor;
    private int shadowColor;
    private float curveWidth = 34f;
    private boolean useCompatPadding = true;
    private float elevation = 0f;
    private float borderWidth = 0f;
    private int borderColor = Color.BLACK;

    // Message Type
    private static final int INCOMING = Bubbles.BubbleType.INCOMING.value;
    private static final int OUTGOING = Bubbles.BubbleType.OUTGOING.value;
    private int bubbleType = INCOMING;

    // Bubble Condition
    private int bubbleCondition = Bubbles.BubbleCondition.SINGLE.value;

    // Background Type
    public static final int ANDROID = 0;
    public static final int IOS = 1;
    private int backgroundStyle = ANDROID;

    private final BubbleCreator bubble = new BubbleCreator();

    public BubbleWhatsapp(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleWhatsapp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleWhatsapp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                    attrs, R.styleable.BubbleWhatsapp, defStyleAttr, 0
            );
            try {
                bubbleType = a.getInteger(R.styleable.BubbleWhatsapp_bubbleType, INCOMING);

                float cornerRadius = a.getDimension(R.styleable.BubbleWhatsapp_cornerRadius, 0f);
                bubble.cornerRadius(cornerRadius);

                elevation = a.getDimension(R.styleable.BubbleWhatsapp_elevation, dp(2));
                bubble.elevation(elevation);

                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#E1FFD4");
                int defaultBackgroundColor = bubbleType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = a.getColor(R.styleable.BubbleWhatsapp_backgroundColor, defaultBackgroundColor);

                // Pressed Color
                int colorPressIncoming = Color.parseColor("#EEEEEE");
                int colorPressOutgoing = Color.parseColor("#CFEAC4");
                int defaultPressedColor = bubbleType == INCOMING ? colorPressIncoming : colorPressOutgoing;
                pressedColor = a.getColor(R.styleable.BubbleWhatsapp_pressedColor, defaultPressedColor);

                useCompatPadding = a.getBoolean(R.styleable.BubbleWhatsapp_useCompatPadding, true);
                backgroundStyle = a.getInteger(R.styleable.BubbleWhatsapp_backgroundStyle, ANDROID);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = a.getColor(R.styleable.BubbleWhatsapp_android_shadowColor, defaultColorShadow);
                int defaultCondition = Bubbles.BubbleCondition.SINGLE.value;
                bubbleCondition = a.getInteger(R.styleable.BubbleWhatsapp_bubbleCondition, defaultCondition);

                // Border
                float getBorderWidth = a.getDimension(R.styleable.BubbleWhatsapp_strokeWidth, 0f);
                borderWidth = Math.min(getBorderWidth, 14f);
                bubble.setBorderWidth(borderWidth);
                borderColor = a.getColor(R.styleable.BubbleWhatsapp_strokeColor, Color.BLACK);

                curveWidth = backgroundStyle == IOS ? 28f : 34f;
                bubble.curveWidth(curveWidth);
            } finally {
                a.recycle();
            }
        }


        if (backgroundColor != 0) {
            // Paint Card
            paintCard = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintCard.setStyle(Paint.Style.FILL);
            paintCard.setColor(backgroundColor);
            //paintCard.setMaskFilter(null);
            bubble.setPaintCard(paintCard);

            // Paint Shadow
            Paint paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintShadow.setStyle(Paint.Style.FILL);
            paintShadow.setColor(shadowColor);
            paintShadow.setMaskFilter(new BlurMaskFilter(
                    Math.min(Math.max(1f, elevation), 30f), BlurMaskFilter.Blur.NORMAL
            ));
            bubble.setPaintShadow(paintShadow);
        }

        if (borderWidth >= 1f) {
            // Paint Border
            Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintBorder.setStyle(Paint.Style.STROKE);
            paintBorder.setColor(borderColor);
            paintBorder.setStrokeWidth(borderWidth);
            bubble.setPaintBorder(paintBorder);
            setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        }

        setClipToPadding(false);
        int paddingSide = 40;
        int paddingV = 34;
        int paddingFromCurve = paddingSide + (int) curveWidth;
        if (useCompatPadding)
            setPadding(
                    bubbleType == INCOMING ? paddingFromCurve : paddingSide,
                    paddingV,
                    bubbleType == OUTGOING ? paddingFromCurve : paddingSide,
                    paddingV
            );

        setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                paintCard.setColor(pressedColor);
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                paintCard.setColor(backgroundColor);
            }
            invalidate();
            return true;
        });

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        bubble.size(getWidth(), getHeight());
        int single = Bubbles.BubbleCondition.SINGLE.value;
        int oldest = Bubbles.BubbleCondition.OLDEST.value;
        boolean isSingleOrOldest = bubbleCondition == single || bubbleCondition == oldest;
        if (backgroundStyle == IOS) { // IOS
            if (bubbleType == OUTGOING) { // Outgoing
                if (isSingleOrOldest) { // Oldest
                    bubble.iOS().outgoing().oldest(canvas);
                } else { // Latest
                    bubble.iOS().outgoing().latest(canvas);
                }
            } else { // Incoming
                if (isSingleOrOldest) { // Oldest
                    bubble.iOS().incoming().oldest(canvas);
                } else { // Latest
                    bubble.iOS().incoming().latest(canvas);
                }
            }
        } else { // Android
            if (bubbleType == OUTGOING) { // Outgoing
                if (isSingleOrOldest) { // Oldest
                    bubble.android().outgoing().oldest(canvas);
                } else { // Latest
                    bubble.android().outgoing().latest(canvas);
                }
            } else { // Incoming
                if (isSingleOrOldest) { // Oldest
                    bubble.android().incoming().oldest(canvas);
                } else { // Latest
                    bubble.android().incoming().latest(canvas);
                }
            }
        }
    }

    public float curvedWidth() { return curveWidth; }

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
