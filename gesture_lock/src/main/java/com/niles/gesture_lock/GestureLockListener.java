package com.niles.gesture_lock;

/**
 * Created by Niles
 * Date 2019/4/11 11:14
 * Email niulinguo@163.com
 */
public interface GestureLockListener {

    void onGestureStart(GestureLockView view);

    void onGestureFinish(GestureLockView view, Byte[] numberList);
}
