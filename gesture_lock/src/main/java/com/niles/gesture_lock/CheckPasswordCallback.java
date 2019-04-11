package com.niles.gesture_lock;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created by Niles
 * Date 2019/4/11 11:29
 * Email niulinguo@163.com
 */
public abstract class CheckPasswordCallback implements GestureLockListener {

    private final byte[] mPassword;
    private final Handler mHandler = new Handler();

    protected CheckPasswordCallback(byte[] password) {
        mPassword = Arrays.copyOf(password, password.length);
    }

    @Override
    public void onGestureStart(GestureLockView view) {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onGestureFinish(GestureLockView view, Byte[] numberList) {
        if (numberList == null || numberList.length < 2) {
            view.reset();
            onCancel();
        } else if (isSamePassword(numberList)) {
            view.setError(false);
            onPasswordSuccess();
        } else {
            view.setError(true);
            onPasswordFailure();

            final WeakReference<GestureLockView> weakReference = new WeakReference<>(view);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GestureLockView lockView = weakReference.get();
                    if (lockView != null && lockView.isAlive()) {
                        lockView.reset();
                    }
                }
            }, 2000);
        }
    }

    protected abstract void onPasswordSuccess();

    protected abstract void onPasswordFailure();

    protected abstract void onCancel();

    private boolean isSamePassword(Byte[] password) {
        if (password == null) {
            return false;
        }

        if (password.length != mPassword.length) {
            return false;
        }

        for (int i = 0; i < password.length; i++) {
            if (password[i] == null || password[i] != mPassword[i]) {
                return false;
            }
        }

        return true;
    }
}
