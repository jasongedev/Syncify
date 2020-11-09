package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.*;

public class HostActivity extends MusicPlayerActivity {
    String currentPlaylistName;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
    }

    void play(){
        //
    }
    public void resume(View v){
        //
    }
    public void pause(View v){
        //
    }
    public void skipNext(View v){
        //
    }
    public void skipPrev(View v){
        //
    }
    public void closeRoom(View v){
        //
    }
    void broadCastPlay(String songName, String songArtist, String songURI){
        //User user = new User();
        //user.playingState = "play";
    }
    void broadCastPause(){
        //User user = new User();
        //user.playingState = "pause";
    }
    void broadCastResume(){
        //User user = new User();
        //user.playingState = "resume";
    }
}