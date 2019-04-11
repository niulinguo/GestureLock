package com.niles.gesture_lock;

/**
 * Created by Niles
 * Date 2019/4/11 09:03
 * Email niulinguo@163.com
 */
class Point {

    private float x;
    private float y;

    Point(float x, float y) {
        update(x, y);
    }

    void update(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    float distance(float x, float y) {
        return (float) Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }
}
