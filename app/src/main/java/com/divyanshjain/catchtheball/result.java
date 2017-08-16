package com.divyanshjain.catchtheball;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class result extends AppCompatActivity {

    private SoundPlayer sound;
    private int scoreCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4527272160231444~6064350090");
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("13DD56D2E91F9E096D9A28C3D615181D").build();
        adView.loadAd(adRequest);

        sound = new SoundPlayer(this);
        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE" , 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE" , 0);
        scoreCopy = highScore;

        if (score > highScore) {

            highScoreLabel.setText("High Score : " +score);
            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE" , score);
            editor.commit();
        } else {

            highScoreLabel.setText("High Score : " +highScore);
        }
    }

    public void tryAgain(View view) {

        sound.playButtonClickSound();
        startActivity(new Intent(getApplicationContext() , start.class));
        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);

    }

    public void shareScore(View view) {

        sound.playButtonClickSound();
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "Hey can you beat my high score in Catch the ball - "
                + scoreCopy +"\nDownload it from https://play.google.com/store/apps/details?id=com.divyanshjain.catchtheball&hl=en" ;

        String shareSub = "Catch the ball";
        myIntent.putExtra(Intent.EXTRA_SUBJECT , shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT , shareBody);
        startActivity(Intent.createChooser(myIntent , "Share Using"));

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
