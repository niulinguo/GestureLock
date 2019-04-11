package com.niles.gesturelock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.niles.gesture_lock.CheckPasswordCallback;
import com.niles.gesture_lock.GestureLockView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GestureLockView gestureLockView = findViewById(R.id.gesture_lock_view);
        gestureLockView.initNumberList(new byte[]{6, 3, 4});
//        gestureLockView.setTouchable(false);
        gestureLockView.setGestureLockListener(new CheckPasswordCallback(new byte[]{6, 3, 4, 7}) {
            @Override
            public void onPasswordSuccess() {
                Toast.makeText(MainActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPasswordFailure() {
                Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
