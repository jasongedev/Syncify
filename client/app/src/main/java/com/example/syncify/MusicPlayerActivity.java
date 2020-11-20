package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public abstract class MusicPlayerActivity extends AppCompatActivity {
    TextView songInfoView;
    TextView listenerCount;
    ObjectAnimator[] soundbarAnims;
    private float minScaleY = 10;
    private float maxScaleY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //abstract void connectAppRemote();
    /*private void startSoundBarAnim() {
        // create, store, and start the sound bar object animators
        ViewGroup viewGroup = findViewById(R.id.);
        soundbarAnims = new ObjectAnimator[viewGroup.getChildCount()];
        Random rand = new Random();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View soundBar = viewGroup.getChildAt(i);

            if (rand.nextInt(2) == 0) {
                setAnimator(soundBar, minScaleY, maxScaleY, i);
            } else {
                setAnimator(soundBar, maxScaleY, minScaleY, i);
            }
        }
    }

    protected void toggleSoundBarAnim(boolean animate) {
       // pause/resume the sound bar object animators
        if (animate) {
            for (ObjectAnimator anim : soundbarAnims) {
                anim.resume();
            }
        } else {
            for (ObjectAnimator anim : soundbarAnims) {
                anim.pause();
            }
        }
    }

    private void setAnimator(View view, float initScaleY, float endScaleY, int idx) {
        soundbarAnims[idx] = ObjectAnimator.ofFloat(view, "scaleY", initScaleY, endScaleY);
        soundbarAnims[idx].setRepeatCount(ValueAnimator.INFINITE);
        soundbarAnims[idx].setRepeatMode(ValueAnimator.REVERSE);
        soundbarAnims[idx].setInterpolator(null);
        soundbarAnims[idx].setDuration((long) ((new Random().nextFloat()) * 500) + 400);
        soundbarAnims[idx].start();
    }*/
}