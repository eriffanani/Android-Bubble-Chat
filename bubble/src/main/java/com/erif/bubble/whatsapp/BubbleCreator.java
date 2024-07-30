package com.erif.bubble.whatsapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

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

        public Incoming incoming(){ return new Incoming(); }

        public class Incoming {

            private float left = 0f;
            private float leftCard = 0f;
            private float top = 0;
            private float curvedHeight = 0f;
            private float curveBottom = 0f;
            private float mCorner = 0f;

            private Path getPath(boolean isShadow) {
                Path path = new Path();
                top = isShadow ? 0f : (mElevation / 3f);
                float bottom = isShadow ? height : height - (mElevation / 1.4f);
                float maxCorner = Math.min(cornerRadius, (height / 1.86f));
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                curveBottom = isShadow ? top + 35f : top + 32f;
                curvedHeight = top + 8f;

                left = isShadow ? 0f : (mElevation / 1.5f);
                leftCard = isShadow ? curveWidth : curveWidth + (mElevation / 1.6f);
                float right = isShadow ? width : width - (mElevation / 1.6f);
                path.moveTo(right / 2f, top); // Center Top (Starting Point)

                path.lineTo(right - mCorner, top); // Top Right
                path.quadTo(right, top, right, top + mCorner); // Corner Top Right

                path.lineTo(right, bottom - mCorner); // Bottom Right
                path.quadTo(right, bottom, right - mCorner, bottom); // Corner Bottom Right

                path.lineTo(leftCard + mCorner, bottom); // Bottom Left
                path.quadTo(leftCard, bottom, leftCard, bottom - mCorner); // Corner Bottom Left

                return path;
            }

            private void draw(
                    Canvas canvas, Path pathShadow, Paint paintShadow,
                    Path path, Paint paint
            ) {
                boolean isShadow = elevation >= 1f;
                if (isShadow)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(path, paint);
            }

            public void latest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true);
                pathShadow.lineTo(leftCard, curveBottom); // Curve Bottom Right
                pathShadow.lineTo(left + 5f, curvedHeight); // Curve Bottom Left
                pathShadow.quadTo(left, top, left + 15f, top); // Curve Corner

                Path path = getPath(false);
                path.lineTo(leftCard, curveBottom); // Curve Bottom Right
                path.lineTo(left + 5f, curvedHeight); // Curve Bottom Left
                path.quadTo(left, top, left + 15f, top); // Curve Corner

                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            public void oldest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(false);
                pathShadow.lineTo(leftCard, top + mCorner);
                pathShadow.quadTo(leftCard, top, leftCard + mCorner, top);

                Path path = getPath(false);
                path.lineTo(leftCard, top + mCorner);
                path.quadTo(leftCard, top, leftCard + mCorner, top);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

        }

        public Outgoing outgoing() { return new Outgoing(); }

        public class Outgoing {

            private float right = 0f;
            private float rightCard = 0f;
            private float top = 0;
            private float curvedHeight = 0f;
            private float curveBottom = 0f;
            private float mCorner = 0f;

            private Path getPath(boolean isShadow) {
                top = isShadow ? 0f : (mElevation / 3f);
                float bottom = isShadow ? height : height - (mElevation / 1.4f);
                float maxCorner = Math.min(cornerRadius, (height / 1.86f));
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                curveBottom = isShadow ? top + 35f : top + 32f;
                curvedHeight = top + 8f;

                Path path = new Path();
                float left = isShadow ? 0f : (mElevation / 1.5f);
                right = isShadow ? width : width - (mElevation / 1.6f);
                rightCard = right - curveWidth;
                path.moveTo(right / 2f, top);

                path.lineTo(left + mCorner, top); // Top Left
                path.quadTo(left, top, left, top + mCorner); // Corner Top Left

                path.lineTo(left, bottom - mCorner); // Bottom Left
                path.quadTo(left, bottom, left + mCorner, bottom); // Corner Bottom Left

                path.lineTo(rightCard - mCorner, bottom); // Bottom Right
                path.quadTo(rightCard, bottom, rightCard, bottom - mCorner); // Corner Bottom Right

                return path;
            }

            private void draw(
                    Canvas canvas, Path pathShadow, Paint paintShadow,
                    Path path, Paint paint
            ) {
                boolean isShadow = elevation >= 1f;
                if (isShadow)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(path, paint);
            }

            public void latest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true);
                pathShadow.lineTo(rightCard, curveBottom);
                pathShadow.lineTo(right - 5f, curvedHeight);
                pathShadow.quadTo(right, top, right - 15f, top);

                Path path = getPath(false);
                path.lineTo(rightCard, curveBottom);
                path.lineTo(right - 5f, curvedHeight);
                path.quadTo(right, top, right - 15f, top);

                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            public void oldest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true);
                pathShadow.lineTo(rightCard, top + mCorner);
                pathShadow.quadTo(rightCard, top, rightCard - mCorner, top);

                Path path = getPath(false);
                path.lineTo(rightCard, top + mCorner);
                path.quadTo(rightCard, top, rightCard - mCorner, top);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

        }
    }

    public class IOS {
        private final float mElevation = Math.min(elevation, height / 4f);

        public Incoming incoming() { return new Incoming(); }

        public class Incoming {

            private void draw(
                    Canvas canvas, Path pathShadow, Paint paintShadow,
                    Path path, Paint paint
            ) {
                boolean isShadow = elevation >= 1f;
                if (isShadow)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(path, paint);
            }

            public void latest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true, true);
                Path path = getPath(false, true);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            public void oldest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true, false);
                Path path = getPath(false, false);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            private Path getPath(boolean isShadow, boolean latest) {
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

                if (latest) {
                    path.lineTo(leftCard + mCorner, bottom); // Bottom Left
                    float curveSmallX = (leftCard + mCorner) - (mCorner / 1.5f);
                    float curveSmallY = mCorner / 4f;
                    float distanceCornerToCurveX = leftCard + mCorner - curveSmallX;
                    path.quadTo( // Corner Bottom Left
                            leftCard + mCorner - (distanceCornerToCurveX / 1.6f), bottom - 1f,
                            curveSmallX, bottom - (curveSmallY / 1.8f)
                    );
                    path.quadTo(leftCard, bottom, left + (mElevation / 3f), bottom - (mElevation / 3f)); // Curved Bottom
                    float minCorner = Math.max(35f, mCorner);
                    path.quadTo(leftCard, bottom - 15f, leftCard, bottom - minCorner); // Curved Top
                } else {
                    path.lineTo(leftCard + mCorner, bottom);
                    path.quadTo(leftCard, bottom, leftCard, bottom - mCorner);
                }

                path.lineTo(leftCard, top + mCorner); // Top Left
                path.quadTo(leftCard, top, leftCard + mCorner, top); // Corner Top Left
                return path;
            }
        }

        public Outgoing outgoing() { return new Outgoing(); }

        public class Outgoing {

            private void draw(
                    Canvas canvas, Path pathShadow, Paint paintShadow,
                    Path path, Paint paint
            ) {
                boolean isShadow = elevation >= 1f;
                if (isShadow)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(path, paint);
            }

            public void latest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true, true);
                Path path = getPath(false, true);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            public void oldest(Canvas canvas, Paint paintShadow, Paint paint) {
                Path pathShadow = getPath(true, false);
                Path path = getPath(false, false);
                draw(canvas, pathShadow, paintShadow, path, paint);
            }

            public Path getPath(boolean isShadow, boolean latest) {
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

                if (latest) {
                    path.lineTo(rightCard - mCorner, bottom); // Bottom Right
                    float curveSmallX = (rightCard - mCorner) + (mCorner / 1.5f);
                    float curveSmallY = mCorner / 4f;
                    float distanceCornerToCurveX = curveSmallX - (rightCard - mCorner);
                    path.quadTo( // Corner Bottom Right
                            rightCard - mCorner + (distanceCornerToCurveX / 1.6f), bottom,
                            curveSmallX, bottom - (curveSmallY / 1.8f)
                    );
                    path.quadTo(rightCard, bottom, right, bottom); // Curved Bottom
                    float minCorner = Math.max(35f, mCorner);
                    path.quadTo(rightCard, bottom - 15f, rightCard, bottom - minCorner); // Curved Top
                } else {
                    path.lineTo(rightCard - mCorner, bottom);
                    path.quadTo(rightCard, bottom, rightCard, bottom - mCorner);
                }

                path.lineTo(rightCard, top + mCorner); // Top Right
                path.quadTo(rightCard, top, rightCard - mCorner, top); // Corner Top Right
                return path;
            }

        }
    }

}
