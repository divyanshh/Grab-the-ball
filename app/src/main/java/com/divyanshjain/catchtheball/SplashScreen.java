package com.divyanshjain.catchtheball;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(this)
                //.withFullScreen()
                .withTargetActivity(start.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.parseColor("#074E72"))
                //.withBackgroundResource(R.drawable.splash)
                .withLogo(R.drawable.box)
                .withHeaderText("Welcome");

        View view = config.create();
        setContentView(view);

    }
}
