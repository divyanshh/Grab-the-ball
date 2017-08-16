package com.divyanshjain.catchtheball;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class start extends AppCompatActivity {

    private SoundPlayer sound;
    private Button rateMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        rateMe = (Button) findViewById(R.id.rateMe);
        Typeface face = Typeface.createFromAsset(getAssets(), "funky1.ttf");
        rateMe.setTypeface(face);

        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, -1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(5000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });
        animator.start();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        sound = new SoundPlayer(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4527272160231444~6064350090");
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("13DD56D2E91F9E096D9A28C3D615181D").build();
        adView.loadAd(adRequest);
    }

    public void startGame(View view) {

        sound.playButtonClickSound();
        startActivity(new Intent(getApplicationContext() , main.class));
        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);

    }

    public void rate(View view) {
        sound.playButtonClickSound();
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.divyanshjain.catchtheball&hl=en")));
    }

    //disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {

                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
