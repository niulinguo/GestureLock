package com.niles.gesture_lock;

/**
 * Created by Niles
 * Date 2019/4/11 09:03
 * Email niulinguo@163.com
 */
public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        update(x, y);
    }

    public void update(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float distance(float x, float y) {
        return (float) Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }
}
