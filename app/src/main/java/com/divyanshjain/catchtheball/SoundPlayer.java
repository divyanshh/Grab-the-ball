package com.divyanshjain.catchtheball;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by divyanshjain on 27/05/17.
 */

public class SoundPlayer {

    final int SOUND_POOL_MAX = 2;

    private AudioAttributes audioAttributes;
    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private static int buttonSound;

    public SoundPlayer (Context context) {

        //SoundPool is depreciated in API level 21 i.e lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        } else {

            //SoundPool (int maxStreams , int streamType , int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX , AudioManager.STREAM_MUSIC , 0);

        }

        hitSound = soundPool.load( context , R.raw.item_liya , 1);
        overSound = soundPool.load( context , R.raw.game_end , 1);
        buttonSound = soundPool.load( context , R.raw.menu , 1);

    }

    public void playHitSound() {

        //play(int soundId , float leftVolume , float rightVolume , int priority , int loop , float rate)
        soundPool.play(hitSound , 1.0f , 1.0f , 1 , 0 , 1.0f);
    }

    public void playOverSound() {

        soundPool.play(overSound , 1.0f , 1.0f , 1 , 0 , 1.0f);
    }

    public void playButtonClickSound() {

        soundPool.play(buttonSound , 1.0f , 1.0f , 1 , 0 , 1.0f);
    }
}
