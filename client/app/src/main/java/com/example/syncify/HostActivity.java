package com.example.syncify;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HostActivity extends MusicPlayerActivity {
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;
    private Subscription<PlayerState> mSub;
    private ProgressBar progressBar;
    private boolean isPaused;
    private ValueEventListener trackListener;
    private ScheduledExecutorService timeStampUpdater;
    private final DatabaseReference listeners = Session.user.child("listeners");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        songInfoView = findViewById(R.id.songText);
        listenerCount = findViewById(R.id.listenerCount);

        //progressBar = findViewById(R.id.);
        //progressBar.setMin(0);
        Session.user.child("isHosting").setValue(true);
        connectAppRemote();
        trackListenerNum();
    }

    void play(){
        String playlistURI = getIntent().getStringExtra("PlaylistUri");
        playerApi.play(playlistURI).setResultCallback(empty -> {
            isPaused = false;
            playerApi.seekTo(0);
            broadCastPlay();});

        timeStampUpdater = new ScheduledThreadPoolExecutor(1);
        timeStampUpdater.scheduleAtFixedRate(new TimeStampUpdate(), 1, 1, TimeUnit.SECONDS);
    }
    public void togglePause(View v){
        if (isPaused) {
            playerApi.resume();
        } else {
            playerApi.pause();
        }
    }
    public void skipNext(View v){
        playerApi.skipNext();
    }
    public void skipPrev(View v){
        playerApi.skipPrevious();
        playerApi.getPlayerState().setResultCallback(playerState -> updateSongInfo(playerState.track));
    }
    public void closeRoom(View v){
        Session.user.child("isHosting").setValue(false);
        timeStampUpdater.shutdownNow();
        listeners.removeEventListener(trackListener);
        playerApi.pause();
        mSub.cancel();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        clearSongInfo();
        super.onBackPressed();
    }
    void broadCastPlay(){
        playerApi.getPlayerState()
                .setResultCallback(playerState -> updateSongInfo(playerState.track))
                .setErrorCallback(throwable -> Log.e("HostActivity", throwable.getMessage(), throwable));
        mSub = playerApi.subscribeToPlayerState().setEventCallback(playerState -> {
            if(playerState.track != null){
                updateSongInfo(playerState.track);
                //progressBar.setMax((int) playerState.track.duration);
                //progressBar.setProgress((int) playerState.playbackPosition, true);
            }
            Session.user.child("isPlaying").setValue(!playerState.isPaused);
        });
    }

    void updateSongInfo(Track track){
        String songInfo;
        if (track.artist.name == null) {
            songInfo = "Advertisement";
        } else {
            songInfo = track.name + " by " + track.artist.name;
        }
        songInfoView.setText(songInfo);
        Session.user.child("songName").setValue(track.name);
        Session.user.child("songArtist").setValue(track.artist.name);
        Session.user.child("songUri").setValue(track.uri);
    }

    void clearSongInfo(){
        Session.user.child("songName").setValue(null);
        Session.user.child("songArtist").setValue(null);
        Session.user.child("songUri").setValue(null);
        Session.user.child("isPlaying").setValue(false);
        Session.user.child("timestamp").setValue(0);
    }

    private void trackListenerNum() {
        trackListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List<String> keyList = snapshot.getValue(t);
                listenerCount.setText(String.valueOf(keyList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        listeners.addValueEventListener(trackListener);
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
                        play();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Something went wrong when attempting to connect! Handle errors here
                        Log.e("HostActivity", throwable.getMessage(), throwable);
                        Toast toast = Toast.makeText(getApplicationContext(), "Could not connect", Toast.LENGTH_SHORT);
                        toast.show();
                        Session.user.child("isHosting").setValue(false);
                        HostActivity.super.onBackPressed();
                    }
                });
    }
    private class TimeStampUpdate implements Runnable{
        @Override
        public void run() {
            playerApi.getPlayerState()
                    .setResultCallback(playerState -> {
                        //progressBar.setProgress((int) playerState.playbackPosition);
                        Session.user.child("timestamp").setValue(playerState.playbackPosition);
                    })
                    .setErrorCallback(throwable -> Log.e("HostActivity", throwable.getMessage(), throwable));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        closeRoom(new View(this));
    }
}