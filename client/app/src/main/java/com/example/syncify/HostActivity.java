package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.*;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HostActivity extends MusicPlayerActivity {
    SpotifyAppRemote mSpotifyAppRemote;
    PlayerApi playerApi;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Session.user.child("isHosting").setValue(true);
        connectAppRemote();
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
    void broadCastPlay(){
        playerApi.getPlayerState()
                .setResultCallback(playerState -> {
                    Track mTrack = playerState.track;
                    Session.user.child("songName").setValue(mTrack.name);
                    Session.user.child("songArtist").setValue(mTrack.artist.name);
                    Session.user.child("songUri").setValue(mTrack.uri);
                    Session.user.child("timestamp").setValue(0);
                })
                .setErrorCallback(throwable -> {Log.e("HostActivity", throwable.getMessage(), throwable);});
    }
    void broadCastPause(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").child(Session.user.getKey()).child("isPlaying").setValue(false);
    }
    void broadCastResume() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").child(Session.user.getKey()).child("isPlaying").setValue(true);
    }

    void connectAppRemote() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(Session.CLIENT_ID)
                        .setRedirectUri(Session.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        playerApi = mSpotifyAppRemote.getPlayerApi();
                        String playlistURI = getIntent().getStringExtra("PlaylistUri");
                        playerApi.play(playlistURI).setResultCallback(empty -> broadCastPlay());
                        ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1);
                        exec.scheduleAtFixedRate(new TimeStampUpdate(), 1, 1, TimeUnit.SECONDS);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("HostActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }
    private class TimeStampUpdate implements Runnable{
        @Override
        public void run() {
            playerApi.getPlayerState()
                    .setResultCallback(playerState -> {
                        Session.user.child("timestamp").setValue(playerState.playbackPosition);
                    })
                    .setErrorCallback(throwable -> {Log.e("HostActivity", throwable.getMessage(), throwable);});
        }
    }
}