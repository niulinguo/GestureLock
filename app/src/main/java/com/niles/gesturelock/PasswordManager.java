package com.niles.gesturelock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Niles
 * Date 2019/4/11 15:36
 * Email niulinguo@163.com
 * <p>
 * 手势密码管理器
 */
public class PasswordManager {

    private static final String KEY_PASSWORD = "GESTURE_LOCK_PASSWORD";

    private final SharedPreferences mSharedPreferences;

    public PasswordManager(Application app) {
        mSharedPreferences = app.getSharedPreferences("password.xml", Context.MODE_PRIVATE);
    }

    /**
     * 获取手势密码
     * <p>
     * 如果没有手势密码，返回null
     */
    public byte[] getPassword() {
        String string = mSharedPreferences.getString(KEY_PASSWORD, null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string.getBytes();
    }

    /**
     * 保存/更新手势密码
     */
    public void savePassword(byte[] password) {
        mSharedPreferences.edit()
                .putString(KEY_PASSWORD, new String(password))
                .apply();
    }

    /**
     * 清除手势密码
     */
    public void clearPassword() {
        mSharedPreferences.edit()
                .remove(KEY_PASSWORD)
                .apply();
    }

    /**
     * 判断是否设置了手势密码
     */
    public boolean hasPassword() {
        return getPassword() != null;
    }
}
