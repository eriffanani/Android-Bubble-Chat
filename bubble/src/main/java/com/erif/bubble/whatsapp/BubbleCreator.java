package com.erif.bubble.whatsapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

class BubbleCreator {

    private int width = 0;
    private int height = 0;
    private float elevation = 0f;
    private float cornerRadius = 0f;
    private float curveWidth = 0f;

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void elevation(float elevation) {
        this.elevation = elevation;
    }

    public void cornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void curveWidth(float curveWidth) {
        this.curveWidth = curveWidth;
    }

    public Android android() { return new Android(); }
    public IOS iOS() { return new IOS(); }

    public class Android {
        private final float mElevation = Math.min(elevation, height / 4f);
        public void drawIncoming(Canvas canvas, Paint paintShadow, Paint paint) {
            boolean isShadow = elevation >= 1f;
            if (isShadow) {
                canvas.drawPath(incoming(true), paintShadow);
            }
            canvas.drawPath(incoming(false), paint);
        }

        private Path incoming(boolean isShadow) {
            float top = isShadow ? 0f : (mElevation / 3f);
            float bottom = isShadow ? height : height - (mElevation / 1.4f);
            float maxCorner = Math.min(cornerRadius, (height / 1.86f));
            float mCornerRadius = isShadow ? (maxCorner + 10f) : maxCorner;
            float curveBottom = isShadow ? top + 35f : top + 32f;
            float curvedHeight = top + 8f;

            Path path = new Path();
            float left = isShadow ? 0f : (mElevation / 1.5f);
            float leftCard = isShadow ? curveWidth : curveWidth + (mElevation / 1.6f);
            float right = isShadow ? width : width - (mElevation / 1.6f);
            path.moveTo(right / 2f, top); // Center Top (Starting Point)

            path.lineTo(right - mCornerRadius, top); // Top Right
            path.quadTo(right, top, right, top + mCornerRadius); // Corner Top Right

            path.lineTo(right, bottom - mCornerRadius); // Bottom Right
            path.quadTo(right, bottom, right - mCornerRadius, bottom); // Corner Bottom Right

            path.lineTo(leftCard + mCornerRadius, bottom); // Bottom Left
            path.quadTo(leftCard, bottom, leftCard, bottom - mCornerRadius); // Corner Bottom Left

            path.lineTo(leftCard, curveBottom); // Curve Bottom Right
            path.lineTo(left + 5f, curvedHeight); // Curve Bottom Left
            path.quadTo(left, top, left + 15f, top); // Curve Corner
            return path;
        }

        public void drawOutgoing(Canvas canvas, Paint paintShadow, Paint paint) {
            boolean isShadow = elevation >= 1f;
            if (isShadow)
                canvas.drawPath(outgoing(true), paintShadow);
            canvas.drawPath(outgoing(false), paint);
        }

        public Path outgoing(boolean isShadow) {
            float top = isShadow ? 0f : (mElevation / 3f);
            float bottom = isShadow ? height : height - (mElevation / 1.4f);
            float maxCorner = Math.min(cornerRadius, (height / 1.86f));
            float mCornerRadius = isShadow ? (maxCorner + 10f) : maxCorner;
            float curveBottom = isShadow ? top + 35f : top + 32f;
            float curvedHeight = top + 8f;

            Path path = new Path();
            float left = isShadow ? 0f : (mElevation / 1.5f);
            float right = isShadow ? width : width - (mElevation / 1.6f);
            float rightCard = isShadow ? right - curveWidth : right - curveWidth - (mElevation / 1.6f);
            path.moveTo(right / 2f, top);

            path.lineTo(left + mCornerRadius, top); // Top Left
            path.quadTo(left, top, left, top + mCornerRadius); // Corner Top Left

            path.lineTo(left, bottom - mCornerRadius); // Bottom Left
            path.quadTo(left, bottom, left + mCornerRadius, bottom); // Corner Bottom Left

            path.lineTo(rightCard - mCornerRadius, bottom); // Bottom Right
            path.quadTo(rightCard, bottom, rightCard, bottom - mCornerRadius); // Corner Bottom Right

            path.lineTo(rightCard, curveBottom);
            path.lineTo(right - 5f, curvedHeight);
            path.quadTo(right, top, right - 15f, top);
            return path;
        }
    }

    public class IOS {
        private final float mElevation = Math.min(elevation, height / 4f);
        public void drawIncoming(Canvas canvas, Paint paintShadow, Paint paint) {
            boolean isShadow = elevation >= 1f;
            if (isShadow)
                canvas.drawPath(incoming(true), paintShadow);
            canvas.drawPath(incoming(false), paint);
        }
        public Path incoming(boolean isShadow) {
            Path path = new Path();
            float left = isShadow ? 0f : (mElevation / 1.5f);
            float top = isShadow ? 0f : (mElevation / 3f);
            float right = isShadow ? width : width - (mElevation / 1.5f);
            float bottom = isShadow ? height : height - (mElevation / 1.4f);
            float maxCorner = Math.min(cornerRadius, height / 1.86f);
            float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;

            float leftCard = isShadow ? curveWidth : curveWidth + (mElevation / 1.6f);
            path.moveTo(right / 2f, top); // Center Top (Starting Point)

            path.lineTo(right - mCorner, top); // Top Right
            path.quadTo(right, top, right, top + mCorner); // Corner Top Right

            path.lineTo(right, bottom - mCorner); // Bottom Right
            path.quadTo(right, bottom, right - mCorner, bottom); // Corner Bottom Right

            path.lineTo(leftCard + mCorner, bottom); // Bottom Left
            float curveSmallX = (leftCard + mCorner) - (mCorner / 1.5f);
            float curveSmallY = mCorner / 4f;
            float distanceCornerToCurveX = leftCard + mCorner - curveSmallX;
            path.quadTo( // Corner Bottom Left
                    leftCard + mCorner - (distanceCornerToCurveX / 1.6f), bottom - 1f,
                    curveSmallX, bottom - curveSmallY
            );
            path.quadTo(leftCard, bottom, left + (mElevation / 3f), bottom - (mElevation / 3f)); // Curved Bottom
            float minCorner = Math.max(35f, mCorner);
            path.quadTo(leftCard, bottom - 15f, leftCard, bottom - minCorner); // Curved Top

            path.lineTo(leftCard, top + mCorner); // Top Left
            path.quadTo(leftCard, top, leftCard + mCorner, top); // Corner Top Left
            return path;
        }

        public void drawOutgoing(
                Canvas canvas, Paint paintShadow, Paint paint
        ) {
            boolean isShadow = elevation >= 1f;
            if (isShadow)
                canvas.drawPath(outgoing(true), paintShadow);
            canvas.drawPath(outgoing(false), paint);
        }

        public Path outgoing(boolean isShadow) {
            Path path = new Path();
            float left = isShadow ? 0f : (mElevation / 1.5f);
            float top = isShadow ? 0f : (mElevation / 3f);
            float right = isShadow ? width : width - (mElevation / 1.5f);
            float bottom = isShadow ? height : height - (mElevation / 1.4f);
            float maxCorner = Math.min(cornerRadius, height / 1.86f);
            float mCorner = isShadow ? (maxCorner + 10f) : maxCorner;

            float rightCard = isShadow ? right - curveWidth : right - curveWidth + (mElevation / 1.6f);
            path.moveTo(right / 2f, top);  // Center Top (Starting Point)

            path.lineTo(left + mCorner, top); // Top Left
            path.quadTo(left, top, left, top + mCorner); // Corner Top Left

            path.lineTo(left, bottom - mCorner); // Bottom Left
            path.quadTo(left, bottom, left + mCorner, bottom); // Corner Bottom Left

            path.lineTo(rightCard - mCorner, bottom); // Bottom Right
            float curveSmallX = (rightCard - mCorner) + (mCorner / 1.5f);
            float curveSmallY = mCorner / 4f;
            float distanceCornerToCurveX = curveSmallX - (rightCard - mCorner);
            path.quadTo( // Corner Bottom Right
                    rightCard - mCorner + (distanceCornerToCurveX / 1.6f), bottom,
                    curveSmallX, bottom - curveSmallY
            );
            path.quadTo(rightCard, bottom, right, bottom); // Curved Bottom
            float minCorner = Math.max(35f, mCorner);
            path.quadTo(rightCard, bottom - 15f, rightCard, bottom - minCorner); // Curved Top

            path.lineTo(rightCard, top + mCorner); // Top Right
            path.quadTo(rightCard, top, rightCard - mCorner, top); // Corner Top Right
            return path;
        }
    }

}
