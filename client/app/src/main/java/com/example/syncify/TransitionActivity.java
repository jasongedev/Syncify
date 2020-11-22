package com.example.syncify;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransitionActivity extends AppCompatActivity {

    ObjectAnimator inXAnimator;
    ObjectAnimator inYAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_screen);

        ImageView headphones = findViewById(R.id.headphones);

        inXAnimator = ObjectAnimator.ofFloat(headphones, "scaleX", 0, 1.4f, 1.25f);
        inYAnimator = ObjectAnimator.ofFloat(headphones, "scaleY", 0, 1.4f, 1.25f);

        inXAnimator.setDuration(400);
        inYAnimator.setDuration(400);
        inXAnimator.start();
        inYAnimator.start();


        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.schedule(() -> {
            Intent intent;

            if (getIntent().getBooleanExtra("Host?", true)) {
                intent = new Intent(this, HostActivity.class);
                intent.putExtra("PlaylistUri", getIntent().getStringExtra("PlaylistUri"));
            } else {
                intent = new Intent(this, ListenerActivity.class);
                intent.putExtra("HostKey", getIntent().getStringExtra("HostKey"));
                intent.putExtra("HostName", getIntent().getStringExtra("HostName"));
            }

            startActivity(intent);
            finish();
        }, 1, TimeUnit.SECONDS);
    }
}
