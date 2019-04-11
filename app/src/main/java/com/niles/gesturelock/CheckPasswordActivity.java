package com.niles.gesturelock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.niles.gesture_lock.CheckPasswordCallback;
import com.niles.gesture_lock.GestureLockView;

public class CheckPasswordActivity extends AppCompatActivity {

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
                mTipView.setText("密码错误，还可以再输入n次");
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
}
