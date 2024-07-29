package com.erif.bubble.whatsapp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.erif.bubble.Bubbles;
import com.erif.bubble.R;

public class BubbleWhatsapp extends FrameLayout {

    private Paint paint;
    private Paint paintShadow;

    private int backgroundColor;
    private int shadowColor;
    private float curveWidth = 34f;
    private boolean useCompatPadding = true;
    private float elevation = 0f;

    // Message Type
    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;
    private int messageType = OUTGOING;

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
                messageType = a.getInteger(R.styleable.BubbleWhatsapp_messageType, INCOMING);

                float cornerRadius = a.getDimension(R.styleable.BubbleWhatsapp_cornerRadius, 0f);
                bubble.cornerRadius(cornerRadius);

                elevation = a.getDimension(R.styleable.BubbleWhatsapp_elevation, 6f);
                bubble.elevation(elevation);

                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#E1FFD4");
                int defaultBackgroundColor = messageType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = a.getColor(R.styleable.BubbleWhatsapp_backgroundColor, defaultBackgroundColor);
                useCompatPadding = a.getBoolean(R.styleable.BubbleWhatsapp_useCompatPadding, true);
                backgroundStyle = a.getInteger(R.styleable.BubbleWhatsapp_backgroundStyle, ANDROID);
                int defaultColorShadow = ContextCompat.getColor(context, R.color.bubble_chat_shadow_color);
                shadowColor = a.getColor(R.styleable.BubbleWhatsapp_android_shadowColor, defaultColorShadow);
                int defaultCondition = Bubbles.BubbleCondition.SINGLE.value;
                bubbleCondition = a.getInteger(R.styleable.BubbleWhatsapp_bubbleCondition, defaultCondition);

                curveWidth = backgroundStyle == IOS ? 28f : 34f;
                bubble.curveWidth(curveWidth);
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
        int paddingSide = 40;
        int paddingV = 34;
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
        bubble.size(getWidth(), getHeight());
        int single = Bubbles.BubbleCondition.SINGLE.value;
        int oldest = Bubbles.BubbleCondition.OLDEST.value;
        if (backgroundStyle == IOS) { // IOS
            if (messageType == OUTGOING) { // Outgoing
                bubble.iOS().drawOutgoing(canvas, paintShadow, paint);
            } else { // Incoming
                bubble.iOS().drawIncoming(canvas, paintShadow, paint);
            }
        } else { // Android
            boolean isSingle = bubbleCondition == single || bubbleCondition == oldest;
            if (messageType == OUTGOING) { // Outgoing
                if (isSingle) { // Latest
                    bubble.android().outgoing().latest(canvas, paintShadow, paint);
                } else { // Oldest
                    bubble.android().outgoing().oldest(canvas, paintShadow, paint);
                }
            } else { // Incoming
                if (isSingle) { // Latest
                    bubble.android().incoming().latest(canvas, paintShadow, paint);
                } else { // Oldest
                    bubble.android().incoming().oldest(canvas, paintShadow, paint);
                }
            }
        }
    }

    public void setMessageType(int type) {
        this.messageType = type > OUTGOING ? INCOMING : type;
    }
    public void setBubbleCondition(Bubbles.BubbleCondition condition) {
        this.bubbleCondition = condition.value;
        invalidate();
    }

}
