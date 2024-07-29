package com.erif.bubble;

public class Bubbles {

    public enum BubbleCondition {
        SINGLE (0),
        OLDEST (1),
        IN_BETWEEN (2),
        LATEST (3);
        public final int value;
        BubbleCondition(int value) {
            this.value = value;
        }
    }

    public enum BubbleType {
        INCOMING (0),
        OUTGOING (1);
        public final int value;
        BubbleType(int value) {
            this.value = value;
        }
    }

}
