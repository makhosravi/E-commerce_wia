package com.od.makh.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.MainActivity;
import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        DivarUtils.statusBarColor(getWindow(), R.color.mainColor);

        String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE,null);

        if (phone == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            },2500);
        }
        else {
            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
            finish();
        }

    }
}
