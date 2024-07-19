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

import com.erif.bubble.R;

public class BubbleWhatsapp extends FrameLayout {

    private Paint paint;
    private Paint paintShadow;

    private int backgroundColor;
    private int shadowColor;
    private float curveWidth = 34f;
    private boolean useCompatPadding = true;

    // Message Type
    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;
    private int messageType = OUTGOING;

    // Background Type
    public static final int ANDROID = 0;
    public static final int IOS = 1;
    private int backgroundStyle = ANDROID;

    private final BubbleCreator bubbleCreator = new BubbleCreator();

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
            TypedArray typedArray = theme.obtainStyledAttributes(
                    attrs, R.styleable.BubbleWhatsapp, defStyleAttr, 0
            );
            try {
                messageType = typedArray.getInteger(R.styleable.BubbleWhatsapp_messageType, INCOMING);

                float cornerRadius = typedArray.getDimension(R.styleable.BubbleWhatsapp_cornerRadius, 0f);
                bubbleCreator.cornerRadius(cornerRadius);

                float elevation = typedArray.getDimension(R.styleable.BubbleWhatsapp_elevation, 6f);
                bubbleCreator.elevation(elevation);

                int colorIncoming = Color.WHITE;
                int colorOutgoing = Color.parseColor("#C3F8C5");
                backgroundColor = messageType == INCOMING ? colorIncoming : colorOutgoing;
                backgroundColor = typedArray.getColor(R.styleable.BubbleWhatsapp_backgroundColor, Color.WHITE);
                useCompatPadding = typedArray.getBoolean(R.styleable.BubbleWhatsapp_useCompatPadding, true);
                backgroundStyle = typedArray.getInteger(R.styleable.BubbleWhatsapp_backgroundStyle, ANDROID);
                int defaultColorShadow = Color.parseColor("#E6E6E6");
                shadowColor = typedArray.getColor(R.styleable.BubbleWhatsapp_android_shadowColor, defaultColorShadow);

                curveWidth = backgroundStyle == IOS ? 28f : 34f;
                bubbleCreator.curveWidth(curveWidth);
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
                8f, BlurMaskFilter.Blur.NORMAL
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
        bubbleCreator.size(getWidth(), getHeight());
        if (backgroundStyle == IOS) {
            if (messageType == OUTGOING) {
                bubbleCreator.iOS().drawOutgoing(canvas, paintShadow, paint);
            } else {
                bubbleCreator.iOS().drawIncoming(canvas, paintShadow, paint);
            }
        } else {
            if (messageType == OUTGOING) {
                bubbleCreator.android().drawOutgoing(canvas, paintShadow, paint);
            } else {
                bubbleCreator.android().drawIncoming(canvas, paintShadow, paint);
            }
        }
    }

    public void setMessageType(int type) {
        this.messageType = type > OUTGOING ? INCOMING : type;
    }

}
