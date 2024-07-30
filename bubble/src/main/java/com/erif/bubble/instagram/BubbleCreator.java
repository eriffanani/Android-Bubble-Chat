package com.erif.bubble.instagram;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

class BubbleCreator {

    private int width = 0;
    private int height = 0;
    private float borderSize = 0f;
    private float cornerRadius = 0f;
    private float elevation = 0f;
    private boolean useShadow = false;
    private float strokeWidth = 0f;

    private static final float SMALL_CORNER = 12f;

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
        useShadow = elevation >= 1f;
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    public Incoming incoming(
            Canvas canvas, Paint paintShadow, Paint paintCard, Paint paintStroke
    ) { return new Incoming(canvas, paintShadow, paintCard, paintStroke); }

    public class Drawing {

        private final Canvas canvas;
        private final Paint paintShadow;
        private final Paint paintCard;
        private final Paint paintStroke;

        public Drawing(Canvas canvas, Paint paintShadow, Paint paintCard, Paint paintStroke) {
            this.canvas = canvas;
            this.paintShadow = paintShadow;
            this.paintCard = paintCard;
            this.paintStroke = paintStroke;
            elevation = Math.min(elevation, height / 10f);
        }

        public void draw(float[] corners) {
            if (useShadow) {
                Path pathShadow = new Path();
                RectF rectFShadow = new RectF();
                float leftShadow = elevation * 1.5f;
                float topShadow = 0f + (elevation + borderSize) * 2;
                float rightShadow = width - (elevation * 2f);
                float bottomShadow = height - (elevation * 2f);
                rectFShadow.set(leftShadow, topShadow, rightShadow, bottomShadow);
                pathShadow.addRoundRect(rectFShadow, corners, Path.Direction.CW);
                canvas.drawPath(pathShadow, paintShadow);
            }
            float left = 0f + (elevation * 1.2f) - borderSize;
            float top = 0f + elevation + borderSize;
            float right = width - (elevation * 1.3f) - borderSize;
            float bottom = height - (elevation * 2f) - borderSize;
            if (strokeWidth > 0f) {
                Path pathStroke = new Path();
                RectF rectFStroke = new RectF();
                rectFStroke.set(
                        left + strokeWidth / 2f,
                        top + strokeWidth / 2f,
                        right - strokeWidth / 2f,
                        bottom - strokeWidth / 2f
                );
                pathStroke.addRoundRect(rectFStroke, corners, Path.Direction.CW);
                canvas.drawPath(pathStroke, paintStroke);
            }

            Path path = new Path();
            RectF rectF = new RectF();
            float additional = Math.max(strokeWidth / 1.1f, 0f);
            rectF.set(
                    left + additional,
                    top + additional,
                    right - additional,
                    bottom - additional
            );
            path.addRoundRect(rectF, corners, Path.Direction.CW);
            canvas.drawPath(path, paintCard);

        }
    }

    class Incoming {

        private final Drawing drawing;
        private final float mCorner = Math.min(cornerRadius, (height - (elevation * 2f) - borderSize) / 2f);

        public Incoming(Canvas canvas, Paint paintShadow, Paint paintCard, Paint paintStroke) {
            drawing = new Drawing(canvas, paintShadow, paintCard, paintStroke);
        }

        public void single() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    mCorner, mCorner, // Top Right
                    mCorner, mCorner, // Bottom Right
                    mCorner, mCorner, // Bottom Left
            };
            drawing.draw(corners);
        }

        public void oldest() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    mCorner, mCorner, // Top Right
                    mCorner, mCorner, // Bottom Right
                    SMALL_CORNER, SMALL_CORNER // Bottom Left
            };
            drawing.draw(corners);
        }

        public void inBetween() {
            float[] corners =  new float[] {
                    SMALL_CORNER, SMALL_CORNER, // Top Left
                    mCorner, mCorner, // Top Right
                    mCorner, mCorner, // Bottom Right
                    SMALL_CORNER, SMALL_CORNER // Bottom Left
            };
            drawing.draw(corners);
        }

        public void latest() {
            float[] corners =  new float[] {
                    SMALL_CORNER, SMALL_CORNER, // Top Left
                    mCorner, mCorner, // Top Right
                    mCorner, mCorner, // Bottom Right
                    mCorner, mCorner // Bottom Left
            };
            drawing.draw(corners);
        }

    }

    public Outgoing outgoing(
            Canvas canvas, Paint paintShadow, Paint paintCard, Paint paintStroke
    ) { return new Outgoing(canvas, paintShadow, paintCard, paintStroke); }

    class Outgoing {

        private final Drawing drawing;
        private final float mCorner = Math.min(cornerRadius, (height - (elevation * 2f) - borderSize) / 2f);

        public Outgoing(Canvas canvas, Paint paintShadow, Paint paintCard, Paint paintStroke) {
            drawing = new Drawing(canvas, paintShadow, paintCard, paintStroke);
        }

        public void single() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    mCorner, mCorner, // Top Right
                    mCorner, mCorner, // Bottom Right
                    mCorner, mCorner // Bottom Left
            };
            drawing.draw(corners);
        }

        public void oldest() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    mCorner, mCorner, // Top Right
                    SMALL_CORNER, SMALL_CORNER, // Bottom Right
                    mCorner, mCorner // Bottom Left
            };
            drawing.draw(corners);
        }

        public void inBetween() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    SMALL_CORNER, SMALL_CORNER, // Top Right
                    SMALL_CORNER, SMALL_CORNER, // Bottom Right
                    mCorner, mCorner // Bottom Left
            };
            drawing.draw(corners);
        }

        public void latest() {
            float[] corners =  new float[] {
                    mCorner, mCorner, // Top Left
                    SMALL_CORNER, SMALL_CORNER, // Top Right
                    mCorner, mCorner, // Bottom Right
                    mCorner, mCorner // Bottom Left
            };
            drawing.draw(corners);
        }
    }

}
