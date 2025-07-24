package com.erif.bubble.instagram;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;

class BubbleCreator {

    private Paint paintShadow, paintCard, paintBorder;

    private int width = 0;
    private int height = 0;
    private float shadowSize = 0f;
    private float cornerRadius = 0f;
    private float cornerRadiusGroup = 0f;
    private float elevation = 0f;
    private boolean useShadow = false;
    private float borderWidth = 0f;

    public void setPaintShadow(Paint paintShadow) {
        this.paintShadow = paintShadow;
    }

    public void setPaintCard(Paint paintCard) {
        this.paintCard = paintCard;
    }

    public void setPaintBorder(Paint paintBorder) {
        this.paintBorder = paintBorder;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setShadowSize(float shadowSize) {
        this.shadowSize = shadowSize;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setCornerRadiusGroup(float cornerRadiusGroup) {
        this.cornerRadiusGroup = cornerRadiusGroup;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
        useShadow = elevation >= 1f;
    }

    public void setBorderWidth(float width) {
        this.borderWidth = width;
    }

    public Incoming incoming() { return new Incoming(); }

    public class Drawing {

        public Drawing() {
            elevation = Math.min(elevation, height / 10f);
        }

        public void draw(float[] corners, Canvas canvas) {
            if (useShadow && paintShadow != null) {
                Path pathShadow = new Path();
                RectF rectFShadow = new RectF();
                float leftShadow = elevation * 1.5f;
                float topShadow = 0f + (elevation + shadowSize) * 2;
                float rightShadow = width - (elevation * 2f);
                float bottomShadow = height - (elevation * 2f);
                rectFShadow.set(leftShadow, topShadow, rightShadow, bottomShadow);
                pathShadow.addRoundRect(rectFShadow, corners, Path.Direction.CW);
                canvas.drawPath(pathShadow, paintShadow);
            }
            float left = 0f + (elevation * 1.2f) - shadowSize;
            float top = 0f + elevation + shadowSize;
            float right = width - (elevation * 1.3f) - shadowSize;
            float bottom = height - (elevation * 2f) - shadowSize;
            if (borderWidth > 0f) {
                Path pathStroke = new Path();
                RectF rectFStroke = new RectF();
                rectFStroke.set(
                        left + borderWidth / 2f,
                        top + borderWidth / 2f,
                        right - borderWidth / 2f,
                        bottom - borderWidth / 2f
                );
                pathStroke.addRoundRect(rectFStroke, corners, Path.Direction.CW);
                canvas.drawPath(pathStroke, paintBorder);
            }

            if (paintCard != null) {
                Path path = new Path();
                RectF rectF = new RectF();
                float additional = Math.max(borderWidth / 1.1f, 0f);
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
    }

    class Incoming {

        private final Drawing drawing;
        private final float mCorner = Math.min(cornerRadius, (height - (elevation * 2f) - shadowSize) / 2f);
        private final float smallCorner = Math.min(cornerRadiusGroup, mCorner);

        public Incoming() {
            drawing = new Drawing();
        }

        public void single(Canvas canvas) {
            float[] corners = cornerRadius(mCorner);
            drawing.draw(corners, canvas);
        }

        public void oldest(Canvas canvas) {
            float[] corners = cornerRadius(mCorner, mCorner, mCorner, smallCorner);
            drawing.draw(corners, canvas);
        }

        public void inBetween(Canvas canvas) {
            float[] corners = cornerRadius(smallCorner, mCorner, mCorner, smallCorner);
            drawing.draw(corners, canvas);
        }

        public void latest(Canvas canvas) {
            float[] corners = cornerRadius(smallCorner, mCorner, mCorner, mCorner);
            drawing.draw(corners, canvas);
        }

    }

    public Outgoing outgoing() { return new Outgoing(); }

    class Outgoing {

        private final Drawing drawing;
        private final float mCorner = Math.min(cornerRadius, (height - (elevation * 2f) - shadowSize) / 2f);
        private final float smallCorner = Math.min(cornerRadiusGroup, mCorner);

        public Outgoing() {
            drawing = new Drawing();
        }

        public void single(Canvas canvas) {
            float[] corners = cornerRadius(mCorner);
            drawing.draw(corners, canvas);
        }

        public void oldest(Canvas canvas) {
            float[] corners = cornerRadius(mCorner, mCorner, smallCorner, mCorner);
            //drawing.draw(corners, canvas);
            Path path = new Path();
            float left = 0f;
            float top = 0f;
            float right = width;
            float bottom = height;
            RectF rectF = new RectF();
            rectF.set(left, top, right, bottom);
            path.addRoundRect(rectF, corners, Path.Direction.CW);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);
            //bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //this line moved outside if
            bitmapCanvas.drawPath(path, paintCard);
            canvas.drawBitmap(bitmap, 0, 0, paintCard);
        }

        public void inBetween(Canvas canvas) {
            float[] corners = cornerRadius(mCorner, smallCorner, smallCorner, mCorner);
            drawing.draw(corners, canvas);
        }

        public void latest(Canvas canvas) {
            float[] corners = cornerRadius(mCorner, smallCorner, mCorner, mCorner);
            //drawing.draw(corners, canvas);
            Path path = new Path();
            float left = 0f;
            float top = 0f;
            float right = width;
            float bottom = height;
            RectF rectF = new RectF();
            rectF.set(left, top, right, bottom);
            path.addRoundRect(rectF, corners, Path.Direction.CW);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);
            //bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //this line moved outside if
            bitmapCanvas.drawPath(path, paintCard);
            canvas.drawBitmap(bitmap, 0, 0, paintCard);

        }
    }

    private float[] cornerRadius(
            float topLeft, float topRight, float bottomRight, float bottomLeft
    ) {
        return new float[]{
                topLeft, topLeft, // Top Left
                topRight, topRight, // Top Right
                bottomRight, bottomRight, // Bottom Right
                bottomLeft, bottomLeft // Bottom Left
        };
    }

    private float[] cornerRadius(float size) {
        return new float[] {
                size, size, // Top Left
                size, size, // Top Right
                size, size, // Bottom Right
                size, size // Bottom Left
        };
    }

}
