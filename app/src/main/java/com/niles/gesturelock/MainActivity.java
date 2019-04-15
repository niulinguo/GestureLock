package com.niles.gesturelock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SETTING_PASSWORD = 1;
    private static final int RC_CHECK_PASSWORD_FOR_SETTING = 2;
    private static final int RC_CHECK_PASSWORD_FOR_CLOSE = 3;

    private CheckBox mCheckBox;
    private Button mSettingBtn;
    private PasswordManager mPasswordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPasswordManager = new PasswordManager(getApplication());

        mCheckBox = findViewById(R.id.cb_open_close);
        mSettingBtn = findViewById(R.id.btn_gesture_setting);

        refreshUI();

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 打开手势密码
                    if (!mPasswordManager.hasPassword()) {
                        openSettingActivity();
                    }
                } else {
                    // 关闭手势密码
                    if (mPasswordManager.hasPassword()) {
                        checkForClose();
                    }
                }
            }
        });

        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPasswordManager.hasPassword()) {
                    checkForSetting();
                }
            }
        });
    }

    private void openSettingActivity() {
        startActivityForResult(new Intent(this, GestureSettingActivity.class), RC_SETTING_PASSWORD);
    }

    private void refreshUI() {
        if (mPasswordManager.hasPassword()) {
            mCheckBox.setChecked(true);
            mSettingBtn.setVisibility(View.VISIBLE);
        } else {
            mCheckBox.setChecked(false);
            mSettingBtn.setVisibility(View.GONE);
        }
    }

    private void checkForClose() {
        startActivityForResult(new Intent(this, CheckPasswordActivity.class), RC_CHECK_PASSWORD_FOR_CLOSE);
    }

    private void checkForSetting() {
        startActivityForResult(new Intent(this, CheckPasswordActivity.class), RC_CHECK_PASSWORD_FOR_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_SETTING_PASSWORD: {
                // 设置手势密码
                if (resultCode == RESULT_OK && data != null) {
                    byte[] password = data.getByteArrayExtra(GestureSettingActivity.EXTRA_PASSWORD);
                    mPasswordManager.savePassword(password);
                }

                refreshUI();
                break;
            }
            case RC_CHECK_PASSWORD_FOR_SETTING: {
                // 检查手势密码 For 设置手势密码
                if (resultCode == RESULT_OK) {
                    openSettingActivity();
                } else if (resultCode == CheckPasswordActivity.RESULT_ERROR) {
                    passwordCheckFailure();
                }
                break;
            }
            case RC_CHECK_PASSWORD_FOR_CLOSE: {
                // 检查手势免密 For 清除手势密码
                if (resultCode == RESULT_OK) {
                    mPasswordManager.clearPassword();
                } else if (resultCode == CheckPasswordActivity.RESULT_ERROR) {
                    passwordCheckFailure();
                }

                refreshUI();
                break;
            }
        }
    }

    /**
     * 手势密码错误多次
     */
    private void passwordCheckFailure() {
        Toast.makeText(this, "密码错误太多次", Toast.LENGTH_SHORT).show();

        // 清除手势密码
        mPasswordManager.clearPassword();
        refreshUI();
    }
}
