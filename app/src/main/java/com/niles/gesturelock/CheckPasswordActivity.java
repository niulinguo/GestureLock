package com.niles.gesturelock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.niles.gesture_lock.CheckPasswordCallback;
import com.niles.gesture_lock.GestureLockView;

import java.util.Locale;

/**
 * 校验手势密码页面
 * <p>
 * 1、打开此页面必须保证已经设置了手势密码
 * 2、如果校验成功，返回上个页面，并回调 {@link android.app.Activity#RESULT_OK}
 * 3、如果直接关闭页面，返回上个页面，并回调 {@link android.app.Activity#RESULT_CANCELED}
 * 4、如果检验失败，返回上个页面，并回调 {@link CheckPasswordActivity#RESULT_ERROR}
 */
public class CheckPasswordActivity extends AppCompatActivity {

    /**
     * 错误回调
     * <p>
     * 出错{@link #mMaxAllowFailureCount}次后回调改错误码
     */
    public static final int RESULT_ERROR = 1;

    /**
     * 最大允许错误数
     */
    private int mMaxAllowFailureCount = 5;

    /**
     * 当前错误数
     */
    private int mFailureCount = 0;

    private TextView mTipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);

        mTipView = findViewById(R.id.tv_tip);

        PasswordManager passwordManager = new PasswordManager(getApplication());

        if (!passwordManager.hasPassword()) {
            finish();
            return;
        }

        GestureLockView gestureLockView = findViewById(R.id.glv_password);
        gestureLockView.setGestureLockListener(new CheckPasswordCallback(passwordManager.getPassword()) {
            @Override
            protected void onPasswordSuccess() {
                mTipView.setText("校验成功");

                success();
            }

            @Override
            protected void onPasswordFailure() {
                mFailureCount += 1;
                mTipView.setText(String.format(Locale.getDefault(), "密码错误，还可以再输入%d次", mMaxAllowFailureCount - mFailureCount));

                if (mFailureCount >= mMaxAllowFailureCount) {
                    failure();
                }
            }

            @Override
            protected void onCancel() {

            }
        });
    }

    private void success() {
        setResult(RESULT_OK);

        finish();
    }

    private void failure() {
        setResult(RESULT_ERROR);

        finish();
    }
}
