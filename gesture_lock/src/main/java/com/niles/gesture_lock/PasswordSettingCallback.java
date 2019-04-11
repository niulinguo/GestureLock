package com.niles.gesture_lock;

/**
 * Created by Niles
 * Date 2019/4/11 15:57
 * Email niulinguo@163.com
 */
public abstract class PasswordSettingCallback implements GestureLockListener {

    protected PasswordSettingCallback() {
    }

    @Override
    public void onGestureStart(GestureLockView view) {

    }

    @Override
    public void onGestureFinish(GestureLockView view, Byte[] numberList) {
        if (numberList == null || numberList.length < 4) {
            view.reset();
            onPasswordFailure("至少连接4个点，请重新输入");
        } else {
            byte[] password = new byte[numberList.length];
            for (int i = 0; i < numberList.length; i++) {
                password[i] = numberList[i];
            }
            onPasswordSuccess(password);
        }
    }

    protected abstract void onPasswordFailure(String msg);

    protected abstract void onPasswordSuccess(byte[] password);
}
