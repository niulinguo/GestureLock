package com.niles.gesturelock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Niles
 * Date 2019/4/11 15:36
 * Email niulinguo@163.com
 */
public class PasswordManager {

    private static final String KEY_PASSWORD = "GESTURE_LOCK_PASSWORD";

    private final SharedPreferences mSharedPreferences;

    public PasswordManager(Application app) {
        mSharedPreferences = app.getSharedPreferences("password.xml", Context.MODE_PRIVATE);
    }

    public byte[] getPassword() {
        String string = mSharedPreferences.getString(KEY_PASSWORD, null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string.getBytes();
    }

    public void savePassword(byte[] password) {
        mSharedPreferences.edit()
                .putString(KEY_PASSWORD, new String(password))
                .apply();
    }

    public void clearPassword() {
        mSharedPreferences.edit()
                .remove(KEY_PASSWORD)
                .apply();
    }

    public boolean hasPassword() {
        return getPassword() != null;
    }
}
