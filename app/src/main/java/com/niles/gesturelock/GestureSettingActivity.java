package com.niles.gesturelock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.niles.gesture_lock.CheckPasswordCallback;
import com.niles.gesture_lock.GestureLockView;
import com.niles.gesture_lock.PasswordSettingCallback;

public class GestureSettingActivity extends AppCompatActivity {

    public static final String EXTRA_PASSWORD = "password";

    private GestureLockView mTipGestureView;
    private TextView mTipView;
    private GestureLockView mPasswordGestureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_setting);

        mTipGestureView = findViewById(R.id.glv_tip);
        mTipGestureView.setTouchable(false);

        mTipView = findViewById(R.id.tv_tip);

        mPasswordGestureView = findViewById(R.id.glv_password);
        mPasswordGestureView.setGestureLockListener(new PasswordSettingCallback() {
            @Override
            protected void onPasswordFailure(String msg) {
                mTipView.setText(msg);
            }

            @Override
            protected void onPasswordSuccess(byte[] password) {
                mTipView.setText("再次绘制解锁图案");
                setPasswordAgain(password);
            }
        });
    }

    private void setPasswordAgain(final byte[] password) {
        mTipGestureView.initNumberList(password);

        mPasswordGestureView.reset();
        mPasswordGestureView.setGestureLockListener(new CheckPasswordCallback(password) {
            @Override
            protected void onPasswordSuccess() {
                mTipView.setText("设置成功");
                success(password);
            }

            @Override
            protected void onPasswordFailure() {
                mTipView.setText("与上一次绘制不一致，请重新绘制");
            }

            @Override
            protected void onCancel() {
            }
        });
    }

    private void success(byte[] password) {
        Intent data = new Intent();
        data.putExtra(EXTRA_PASSWORD, password);
        setResult(RESULT_OK, data);

        finish();
    }
}
