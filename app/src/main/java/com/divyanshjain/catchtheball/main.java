package com.divyanshjain.catchtheball;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView black;
    private ImageView pink;

    //size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //initialize position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    //speed
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;


    //score
    private int score = 0;

    //initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    //Status check
    private boolean action_flg = false;
    private boolean start_flg = false;

    private int adSize;
    private InterstitialAd interstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

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
        AdView adView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("13DD56D2E91F9E096D9A28C3D615181D").build();
        adView.loadAd(adRequest);


        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("13DD56D2E91F9E096D9A28C3D615181D").build();
        interstitial.loadAd(adRequest1);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //Toast.makeText(main.this, "ad closed", Toast.LENGTH_SHORT).show();
                sound.playButtonClickSound();
                Intent intent = new Intent(getApplicationContext() , result.class);
                intent.putExtra("SCORE" , score);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                //Toast.makeText(main.this, "ad failed to load", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(main.this, "ad left application", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                //Toast.makeText(main.this, "ad opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                //Toast.makeText(main.this, "interstitial ad loaded", Toast.LENGTH_SHORT).show();
            }
        });

        sound = new SoundPlayer(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);

        //Typeface face = Typeface.createFromAsset(getAssets(), "funky1.ttf");
        //startLabel.setTypeface(face);

        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        black = (ImageView) findViewById(R.id.black);
        pink = (ImageView) findViewById(R.id.pink);

        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        boxSpeed = Math.round(screenHeight / 90F);
        orangeSpeed = Math.round(screenWidth / 60F);
        pinkSpeed = Math.round(screenWidth / 36F);
        blackSpeed = Math.round(screenWidth / 45F);

        Log.v("SPEED_BOX" , boxSpeed+"");
        Log.v("SPEED_ORANGE" , orangeSpeed+"");
        Log.v("SPEED_PINK" , pinkSpeed+"");
        Log.v("SPEED_BLACK" , blackSpeed+"");

        //Move out of screen
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score : 0");
        adSize = adView.getHeight();
        Log.v("Ad Size" , adSize+"");


    }

    public void changePos() {

        hitCheck();

        //orange
        orangeX -= orangeSpeed;
        if (orangeX < 0) {

            orangeX = screenWidth + 20;
            orangeY = (int)Math.floor(Math.random() * (frameHeight - orange.getHeight()));

        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //black
        blackX -= blackSpeed;
        if (blackX < 0) {

            blackX = screenWidth + 10;
            blackY = (int)Math.floor(Math.random() * (frameHeight - black.getHeight()));

        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= pinkSpeed;
        if (pinkX < 0) {

            pinkX = screenWidth + 5000;
            pinkY = (int)Math.floor(Math.random() * (frameHeight - pink.getHeight()));

        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //move box
        if (action_flg == true) {

            //touching
            boxY -= boxSpeed;

        } else {

            //releasing
            boxY += boxSpeed;

        }

        if(boxY < 0) {
            boxY = 0;
        }

        if (boxY > frameHeight - boxSize ) {
            boxY = frameHeight - boxSize ;
        }

        box.setY(boxY);

        scoreLabel.setText("Score : " +score);
    }

    public void hitCheck() {

        //orange
        int orangeCentreX = orangeX + orange.getWidth() / 2;
        int orangeCentreY = orangeY + orange.getHeight() / 2;

        if(0 <= orangeCentreX && orangeCentreX < boxSize &&
                boxY <= orangeCentreY && orangeCentreY <= boxY + boxSize) {

            score += 10;
            orangeX = -10;
            sound.playHitSound();

        }

        //pink
        int pinkCentreX = pinkX + pink.getWidth() / 2;
        int pinkCentreY = pinkY + pink.getHeight() / 2;

        if(0 <= pinkCentreX && pinkCentreX < boxSize &&
                boxY <= pinkCentreY && pinkCentreY <= boxY + boxSize) {

            score += 30;
            pinkX = -10;
            sound.playHitSound();

        }

        //black
        int blackCentreX = blackX + black.getWidth() / 2;
        int blackCentreY = blackY + black.getHeight() / 2;

        if(0 <= blackCentreX && blackCentreX < boxSize &&
                boxY <= blackCentreY && blackCentreY <= boxY + boxSize || boxY == 0 || boxY == frameHeight - boxSize) {

            sound.playOverSound();
            //stop timer
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            timer = null;

            int prob = (int)Math.floor(Math.random() * 5);

            if (interstitial.isLoaded()) {
                interstitial.show();
            } else {
                //Toast.makeText(main.this, "ad wasn't loaded yet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , result.class);
                intent.putExtra("SCORE" , score);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }


            //show result
            //Intent intent = new Intent(getApplicationContext() , result.class);
            //intent.putExtra("SCORE" , score);
            //startActivity(intent);

        }

    }

    public  boolean onTouchEvent(MotionEvent motionEvent) {

        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            } , 0 , 20);

        } else {

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                action_flg = true;

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                action_flg = false;

            }

        }

        return true;
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
