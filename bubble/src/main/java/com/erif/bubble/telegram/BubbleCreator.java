package com.erif.bubble.telegram;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

class BubbleCreator {

    private int width = 0;
    private int height = 0;
    private float elevation = 0f;
    private float cornerRadius = 0f;

    private Paint paintShadow;
    private Paint paintCard;

    private static final float CORNER_SMALL = 22f;
    public static final float CURVED_WIDTH = 20f;

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setPaintShadow(Paint paintShadow) {
        this.paintShadow = paintShadow;
    }

    public void setPaintCard(Paint paintCard) {
        this.paintCard = paintCard;
    }

    public Android android() { return new Android(); }

    class Android {

        private float left = 0f;
        private float leftCard = 0f;
        private float top = 0f;
        private float right = 0f;
        private float rightCard = 0f;
        private float bottom = 0f;
        private float mCorner = 0f;

        public Incoming incoming() { return new Incoming(); }
        class Incoming {

            private void draw(Canvas canvas, Path pathShadow, Path pathCard) {
                if (elevation >= 1f)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(pathCard, paintCard);
            }

            private Path getPath(boolean isShadow) {
                Path path = new Path();
                float mElevation = Math.min(elevation, height / 3f);
                float maxCorner = Math.min(cornerRadius, height / 1.86f);
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                left = isShadow ? 0f : mElevation / 1.5f;
                leftCard = isShadow ? CURVED_WIDTH : CURVED_WIDTH + (mElevation / 1.6f);
                top = isShadow ? 0f : (mElevation / 3f);
                right = isShadow ? width : width - (mElevation / 1.5f);
                bottom = isShadow ? height : height - (mElevation / 1.4f);
                path.moveTo(right / 2f, top);

                path.lineTo(right - mCorner, top); // Top Right
                path.quadTo(right, top, right, top + mCorner); // Corner Top Right

                path.lineTo(right, bottom - mCorner); // Bottom Right
                path.quadTo(right, bottom, right - mCorner, bottom); // Corner Bottom Right
                return path;
            }

            private void bottomLeftCurved(Path path) {
                path.lineTo(left + 2f, bottom); // Bottom Left
                path.quadTo(left, bottom - 2f, left + 2f, bottom - 4f); // Curved Small
                float minMCorner = Math.max(35f, mCorner);
                path.quadTo(leftCard, bottom - 15f, leftCard, bottom - minMCorner); // Curved Top
            }

            private void bottomLeftSmall(Path path) {
                path.lineTo(leftCard + CORNER_SMALL, bottom);
                path.quadTo(leftCard, bottom, leftCard, bottom - CORNER_SMALL);
            }

            private void topLeft(Path path) {
                path.lineTo(leftCard, top + mCorner);
                path.quadTo(leftCard, top, leftCard + mCorner, top);
            }

            private void topLeftSmall(Path path) {
                path.lineTo(leftCard, top + CORNER_SMALL);
                path.quadTo(leftCard, top, leftCard + CORNER_SMALL, top);
            }

            public void single(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftCurved(pathShadow);
                topLeft(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftCurved(pathCard);
                topLeft(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void oldest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftSmall(pathShadow);
                topLeft(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftSmall(pathCard);
                topLeft(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void inBetween(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftSmall(pathShadow);
                topLeftSmall(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftSmall(pathCard);
                topLeftSmall(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void latest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftCurved(pathShadow);
                topLeftSmall(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftCurved(pathCard);
                topLeftSmall(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

        }

        public Outgoing outgoing() { return new Outgoing(); }
        class Outgoing {

            private void draw(Canvas canvas, Path pathShadow, Path pathCard) {
                if (elevation >= 1f)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(pathCard, paintCard);
            }

            private Path getPath(boolean isShadow) {
                Path path = new Path();
                float mElevation = Math.min(elevation, height / 3f);
                float maxCorner = Math.min(cornerRadius, height / 1.86f);
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                left = isShadow ? 0f : mElevation / 1.5f;
                top = isShadow ? 0f : (mElevation / 3f);
                right = isShadow ? width : width - (mElevation / 1.5f);
                rightCard = isShadow ? width - CURVED_WIDTH : width - CURVED_WIDTH - (mElevation / 1.6f);
                bottom = isShadow ? height : height - (mElevation / 1.4f);
                path.moveTo(right / 2f, top);

                path.lineTo(left + mCorner, top); // Top Left
                path.quadTo(left, top, left, top + mCorner); // Corner Top Left

                path.lineTo(left, bottom - mCorner); // Bottom Left
                path.quadTo(left, bottom, left + mCorner, bottom); // Corner Bottom Left
                return path;
            }

            private void bottomRightCurved(Path path) {
                path.lineTo(right - 2f, bottom); // Bottom Right
                path.quadTo(right, bottom - 2f, right, bottom - 4f); // Curved Small
                float minMCorner = Math.max(35f, mCorner);
                path.quadTo(rightCard, bottom - 15f, rightCard, bottom - minMCorner); // Curved Top
            }

            private void bottomRightSmall(Path path) {
                path.lineTo(rightCard - CORNER_SMALL, bottom);
                path.quadTo(rightCard, bottom, rightCard, bottom - CORNER_SMALL);
            }

            private void topRight(Path path) {
                path.lineTo(rightCard, top + mCorner);
                path.quadTo(rightCard, top, rightCard - mCorner, top);
            }

            private void topRightSmall(Path path) {
                path.lineTo(rightCard, top + CORNER_SMALL);
                path.quadTo(rightCard, top, rightCard - CORNER_SMALL, top);
            }

            public void oldest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightSmall(pathShadow);
                topRight(pathShadow);

                Path path = getPath(false);
                bottomRightSmall(path);
                topRight(path);
                draw(canvas, pathShadow, path);
            }

            public void inBetween(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightSmall(pathShadow);
                topRightSmall(pathShadow);

                Path path = getPath(false);
                bottomRightSmall(path);
                topRightSmall(path);
                draw(canvas, pathShadow, path);
            }

            public void latest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightCurved(pathShadow);
                topRightSmall(pathShadow);

                Path path = getPath(false);
                bottomRightCurved(path);
                topRightSmall(path);
                draw(canvas, pathShadow, path);
            }

            public void single(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightCurved(pathShadow);
                topRight(pathShadow);

                Path path = getPath(false);
                bottomRightCurved(path);
                topRight(path);
                draw(canvas, pathShadow, path);
            }

        }

    }


    public IOS iOS() { return new IOS(); }

    class IOS {

        private float left = 0f;
        private float leftCard = 0f;
        private float top = 0f;
        private float right = 0f;
        private float rightCard = 0f;
        private float bottom = 0f;
        private float mCorner = 0f;

        public Incoming incoming() { return new Incoming(); }
        class Incoming {
            private void draw(Canvas canvas, Path pathShadow, Path pathCard) {
                if (elevation >= 1f)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(pathCard, paintCard);
            }

            private Path getPath(boolean isShadow) {
                Path path = new Path();
                float mElevation = Math.min(elevation, height / 3f);
                float maxCorner = Math.min(cornerRadius, height / 1.86f);
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                left = isShadow ? 0f : mElevation / 1.5f;
                leftCard = isShadow ? CURVED_WIDTH : CURVED_WIDTH + (mElevation / 1.6f);
                top = isShadow ? 0f : (mElevation / 3f);
                right = isShadow ? width : width - (mElevation / 1.5f);
                bottom = isShadow ? height : height - (mElevation / 1.4f);
                path.moveTo(right / 2f, top);

                path.lineTo(right - mCorner, top); // Top Right
                path.quadTo(right, top, right, top + mCorner); // Corner Top Right

                path.lineTo(right, bottom - mCorner); // Bottom Right
                path.quadTo(right, bottom, right - mCorner, bottom); // Corner Bottom Right
                return path;
            }

            private void bottomLeftCurved(Path path) {
                float targetCurveRight = leftCard + 22f;
                path.lineTo(targetCurveRight + 30f, bottom);
                path.quadTo(targetCurveRight + 10f, bottom - 2f, targetCurveRight, bottom - 10f);

                path.quadTo(targetCurveRight - 22f, bottom, left, bottom);
                path.quadTo(left + 20, bottom - 15f, leftCard, bottom - 40f);
            }

            private void bottomLeftSmall(Path path) {
                path.lineTo(leftCard + CORNER_SMALL, bottom);
                path.quadTo(leftCard, bottom, leftCard, bottom - CORNER_SMALL);
            }

            private void topLeft(Path path) {
                path.lineTo(leftCard, top + mCorner);
                path.quadTo(leftCard, top, leftCard + mCorner, top);
            }

            private void topLeftSmall(Path path) {
                path.lineTo(leftCard, top + CORNER_SMALL);
                path.quadTo(leftCard, top, leftCard + CORNER_SMALL, top);
            }

            public void single(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftCurved(pathShadow);
                topLeft(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftCurved(pathCard);
                topLeft(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void oldest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftSmall(pathShadow);
                topLeft(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftSmall(pathCard);
                topLeft(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void inBetween(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftSmall(pathShadow);
                topLeftSmall(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftSmall(pathCard);
                topLeftSmall(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

            public void latest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomLeftCurved(pathShadow);
                topLeftSmall(pathShadow);

                Path pathCard = getPath(false);
                bottomLeftCurved(pathCard);
                topLeftSmall(pathCard);

                draw(canvas, pathShadow, pathCard);
            }

        }

        public Outgoing outgoing() { return new Outgoing(); }
        class Outgoing {
            private void draw(Canvas canvas, Path pathShadow, Path pathCard) {
                if (elevation >= 1f)
                    canvas.drawPath(pathShadow, paintShadow);
                canvas.drawPath(pathCard, paintCard);
            }

            private Path getPath(boolean isShadow) {
                Path path = new Path();
                float mElevation = Math.min(elevation, height / 3f);
                float maxCorner = Math.min(cornerRadius, height / 1.86f);
                mCorner = isShadow ? (maxCorner + 10f) : maxCorner;
                left = isShadow ? 0f : mElevation / 1.5f;
                top = isShadow ? 0f : (mElevation / 3f);
                right = isShadow ? width : width - (mElevation / 1.5f);
                rightCard = isShadow ? width - CURVED_WIDTH : width - CURVED_WIDTH - (mElevation / 1.6f);
                bottom = isShadow ? height : height - (mElevation / 1.4f);
                path.moveTo(right / 2f, top);

                path.lineTo(left + mCorner, top); // Top Left
                path.quadTo(left, top, left, top + mCorner); // Corner Top Left

                path.lineTo(left, bottom - mCorner); // Bottom Left
                path.quadTo(left, bottom, left + mCorner, bottom); // Corner Bottom Left
                return path;
            }

            private void bottomRightCurved(Path path) {
                float targetCurveRight = rightCard - 22f;
                path.lineTo(targetCurveRight - 30f, bottom);
                path.quadTo(targetCurveRight - 10f, bottom - 2f, targetCurveRight, bottom - 10f);

                path.quadTo(targetCurveRight + 22f, bottom, right, bottom);
                path.quadTo(right - 20, bottom - 15f, rightCard, bottom - 40f);
            }

            private void bottomRightSmall(Path path) {
                path.lineTo(rightCard - CORNER_SMALL, bottom);
                path.quadTo(rightCard, bottom, rightCard, bottom - CORNER_SMALL);
            }

            private void topRight(Path path) {
                path.lineTo(rightCard, top + mCorner);
                path.quadTo(rightCard, top, rightCard - mCorner, top);
            }

            private void topRightSmall(Path path) {
                path.lineTo(rightCard, top + CORNER_SMALL);
                path.quadTo(rightCard, top, rightCard - CORNER_SMALL, top);
            }

            public void oldest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightSmall(pathShadow);
                topRight(pathShadow);

                Path path = getPath(false);
                bottomRightSmall(path);
                topRight(path);
                draw(canvas, pathShadow, path);
            }

            public void inBetween(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightSmall(pathShadow);
                topRightSmall(pathShadow);

                Path path = getPath(false);
                bottomRightSmall(path);
                topRightSmall(path);
                draw(canvas, pathShadow, path);
            }

            public void latest(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightCurved(pathShadow);
                topRightSmall(pathShadow);

                Path path = getPath(false);
                bottomRightCurved(path);
                topRightSmall(path);
                draw(canvas, pathShadow, path);
            }

            public void single(Canvas canvas) {
                Path pathShadow = getPath(true);
                bottomRightCurved(pathShadow);
                topRight(pathShadow);

                Path path = getPath(false);
                bottomRightCurved(path);
                topRight(path);
                draw(canvas, pathShadow, path);
            }

        }
    }

}
