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
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://syncify-bf9e2.firebaseio.com/").getReference();
        rootRef.child("users").child(Session.user.getKey()).child("isPlaying").setValue(true);
    }
    void broadCastPause(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://syncify-bf9e2.firebaseio.com/").getReference();
        rootRef.child("users").child(Session.user.getKey()).child("isPlaying").setValue(false);
    }
    void broadCastResume(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://syncify-bf9e2.firebaseio.com/").getReference();
        rootRef.child("users").child(Session.user.getKey()).child("isPlaying").setValue(true);
}